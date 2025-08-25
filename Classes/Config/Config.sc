Config {
	classvar configFileName = "config.yaml";
	classvar prHardwareSynthesizers;
	classvar prMidi;
	classvar prServer;
	classvar prConfigLoaded = false;

	*hardwareSynthesizers {
		if (prConfigLoaded == false, {
			this.load;
		});

		if (prHardwareSynthesizers.isNil, {
			Error(format("Config.hardwareSynthesizers was called but the configuration file at % was not found or did not contain a valid section called HardwareSynthesizers. See the help file for the Config class for guidance.", Platform.userConfigDir +/+ configFileName)).throw;
		});

		^prHardwareSynthesizers;
	}

	*load {
		var configFilePath, config, setMidiConfig = true, setServerConfig = true;
		if (prConfigLoaded == false, {
			postln("Loading configuration...");
			configFilePath = Platform.userConfigDir +/+ "config.yaml";
			if ((File.exists(configFilePath) == true), {
				config = File.readAllString(configFilePath).parseYAML;

				prHardwareSynthesizers = Dictionary();
				if (config["HardwareSynthesizers"].isNil, {

				},{
					if (config["HardwareSynthesizers"].isMemberOf(Array) == false, {
						warn(format("The configuration file at % has a section called 'HardwareSynthesizers', but this section is expected to be a YAML collection. See the help file for the Config class for guidance.", Platform.userConfigDir +/+ configFileName));
					},{
						// First, check none of the IDs are duplicated
						var dict = Dictionary();
						config["HardwareSynthesizers"].do({
							|item, index|
							if (item.isMemberOf(Dictionary), {
								if (item["Id"].notNil, {
									if (item["Id"].isMemberOf(String), {
										if (dict.includesKey(item["Id"]), {
											Error(format("The config cannot be loaded because the configuration file contains more than one hardware synthesizer with the Id of '%'.", item["Id"])).throw;
										});
										dict.put(item["Id"], 0);
									});
								});
							});
						});
						// Then process them and add them to the collection of HardwareSynthesizerConfig
						config["HardwareSynthesizers"].do({
							|item, index|
							var addSynthesizer = true;
							var synthesizerClass;
							if (item.isMemberOf(Dictionary) == false, {
								warn(format("Item % in the 'HardwareSynthesizers' section of the configuration file at % is not a YAML mapping. This item will not be added to the configuration. See the help file for the Config class for guidance.", index + 1, Platform.userConfigDir +/+ configFileName));
								addSynthesizer = false;
							}, {
								["Id","Class"].do({
									|key|
									if (item[key].isNil, {
										warn(format("Item % in the 'HardwareSynthesizers' section of the configuration file at % does not have a property called '%'. This item will not be added to the configuration. See the help file for the Config class for guidance.", index + 1, Platform.userConfigDir +/+ configFileName, key));
										addSynthesizer = false;
									},{
										if (item[key].isMemberOf(String) == false, {
											warn(format("Item % in the 'HardwareSynthesizers' section of the configuration file at % has a '%' property but its value is expected to be a String. This item will not be added to the configuration. See the help file for the Config class for guidance.", index + 1, Platform.userConfigDir +/+ configFileName, key));
											addSynthesizer = false;
										});
									});
								});
								["MIDIChannels","InputBusChannels"].do({
									|key|
									var lowerlimit, upperlimit;
									if (key == "MIDIChannels", {
										lowerlimit = 1;
										upperlimit = 16;
									}, {
										lowerlimit = 0;
										upperlimit = 99;
									});
									if (item[key].isNil, {
										warn(format("Item % in the 'HardwareSynthesizers' section of the configuration file at % does not have a property called '%'. This item will not be added to the configuration. See the help file for the Config class for guidance.", index + 1, Platform.userConfigDir +/+ configFileName, key));
								addSynthesizer = false;
									},{
										if (item[key].isMemberOf(Array) == false, {
											warn(format("Item % in the 'HardwareSynthesizers' section of the configuration file at % has a '%' property but its value is expected to be an Array. This item will not be added to the configuration. See the help file for the Config class for guidance.", index + 1, Platform.userConfigDir +/+ configFileName, key));
											addSynthesizer = false;
										},{
											item[key].do({
												|maybeanumber|
												var number;
												if (maybeanumber.isMemberOf(String), {
													number = maybeanumber.asInteger;
													if ((number < lowerlimit) || (number > upperlimit), {
														warn(format("Item % in the 'HardwareSynthesizers' section of the configuration file at % has a '%' property but not everything in it is in the range % to %. This item will not be added to the configuration. See the help file for the Config class for guidance.", index + 1, Platform.userConfigDir +/+ configFileName, key, lowerlimit, upperlimit));
														addSynthesizer = false;
													});
												}, {
													warn(format("Item % in the 'HardwareSynthesizers' section of the configuration file at % has a '%' property but not everything in it is a number. This item will not be added to the configuration. See the help file for the Config class for guidance.", index + 1, Platform.userConfigDir +/+ configFileName, key));
													addSynthesizer = false;
												});
											});
										});
									});
								});
								synthesizerClass = Class.allClasses.select({|class|class.name==item["Class"].asSymbol});
								if (synthesizerClass.size != 1,{
									warn(format("Item % in the 'HardwareSynthesizers' section of the configuration file at % has a 'Class' property of '%' but no such class was found in SuperCollider.", index + 1, Platform.userConfigDir +/+ configFileName, item["Class"]));
									addSynthesizer = false;
								});
							});
							if (addSynthesizer, {
								prHardwareSynthesizers.put(item["Id"].asSymbol, HardwareSynthesizerConfig(item["Id"].asSymbol,synthesizerClass[0], item["MIDIChannels"].collect(_.asInteger - 1), item["InputBusChannels"].collect(_.asInteger)));
							});
						});
					});
				});

				if (config["MIDI"].isNil, {
					warn(format("The configuration file at % is missing the section called 'MIDI'. The MIDI configuration will not be set. See the help file for the Config class for guidance.", Platform.userConfigDir +/+ configFileName));
					setMidiConfig = false;
				},{
					["DeviceName","PortName"].do({
						|key|
						if (config["MIDI"][key].isNil, {
							warn(format("The configuration file at % has the section called 'MIDI' but it does not have a property called '%'. The MIDI configuration will not be set. See the help file for the Config class for guidance.", Platform.userConfigDir +/+ configFileName, key));
							setMidiConfig = false;
						},{
							if (config["MIDI"][key].isMemberOf(String) == false, {
								warn(format("The configuration file at % has the section called 'MIDI' but its '%' property is expected to be a String. The MIDI configuration will not be set. See the help file for the Config class for guidance.", Platform.userConfigDir +/+ configFileName, key));
								setMidiConfig = false;
							});
						});
					});
				});

				if (setMidiConfig, {
					prMidi = MIDIConfig(config["MIDI"]["DeviceName"], config["MIDI"]["PortName"]);
				});

				if (config["Server"].isNil, {
					warn(format("The configuration file at % is missing the section called 'Server'. The server configuration will be set to the default values of 2 input bus channels and 2 output bus channels. See the help file for the Config class for guidance.", Platform.userConfigDir +/+ configFileName));
					setServerConfig = false;
				},{
					["NumInputBusChannels","NumOutputBusChannels"].do({
						|key|
						var number;
						if (config["Server"][key].isNil, {
							warn(format("The configuration file at % has the section called 'Server' but it does not have a property called '%'. The server configuration will be set to the default values of 2 input bus channels and 2 output bus channels. See the help file for the Config class for guidance.", Platform.userConfigDir +/+ configFileName, key));
							setServerConfig = false;
						},{
							if (config["Server"][key].isMemberOf(String), {
								number = config["Server"][key].asInteger;
								if ((number < 1) || (number > 100), {
									warn(format("The configuration file at % has the section called 'Server' but its '%' property is expected to be a number between 1 and 100. The server configuration will be set to the default values of 2 input bus channels and 2 output bus channels. See the help file for the Config class for guidance.", Platform.userConfigDir +/+ configFileName, key));
									setServerConfig = false;
								});
							},{
								warn(format("The configuration file at % has the section called 'Server' but its '%' property is expected to be an number. The server configuration will be set to the default values of 2 input bus channels and 2 output bus channels. See the help file for the Config class for guidance.", Platform.userConfigDir +/+ configFileName, key));
								setServerConfig = false;
							});
						});
					});
				});

				if (setServerConfig, {
					prServer = ServerConfig(config["Server"]["NumInputBusChannels"].asInteger, config["Server"]["NumOutputBusChannels"].asInteger);
				},{
					prServer = ServerConfig(2, 2);
				});

				prConfigLoaded = true;
			},{
				warn(format("The configuration file was not found at %. See the help file for the Config class for guidance.", configFilePath));
				prServer = ServerConfig(2, 2);
			});
		});
	}

	*midi {
		if (prConfigLoaded == false, {
			this.load;
		});

		if (prMidi.isNil, {
			Error(format("Config.midi was called but the configuration file at % was not found or did not contain a valid section called MIDI. See the help file for the Config class for guidance.", Platform.userConfigDir +/+ configFileName)).throw;
		});

		^prMidi;
	}

	*openConfigFile {
		var configFilePath = Platform.userConfigDir +/+ "config.yaml";
		if ((File.exists(configFilePath) == true), {
			Document.open(configFilePath);
		},{
			warn(format("The configuration file was not found at %. See the help file for the Config class for guidance.", configFilePath));
		});
	}

	*server {
		if (prConfigLoaded == false, {
			this.load;
		});

		if (prServer.isNil, {
			Error(format("Config.server was called but the configuration file at % was not found or did not contain a valid section called Server. See the help file for the Config class for guidance.", Platform.userConfigDir +/+ configFileName)).throw;
		});

		^prServer;
	}
}
