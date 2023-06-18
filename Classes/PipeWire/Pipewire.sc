PipeWire {
	*setSuperColliderAsMixer {
		var pathToPipeWireDotSc, scriptPath, script, pipe, line;
		pathToPipeWireDotSc = PathName(PipeWire.filenameSymbol.asString).pathOnly;
		scriptPath = format("%supercollider.sh", pathToPipeWireDotSc);
		if (File.exists(scriptPath), {
			script = File.readAllString(scriptPath);
			pipe = Pipe.new(format("bash %", scriptPath), "r");
			line = pipe.getLine;                    // get the first line
			while({line.notNil}, {line.postln; line = pipe.getLine; });    // post until l = nil
			pipe.close;
		},{
			Error(format("The file % was not found.", scriptPath)).throw;
		});
	}
}