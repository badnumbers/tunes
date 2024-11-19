Project {
	var pr_Condition;
	var pr_Files;

	includeFile {
		|filename, openInIde = true, runContents = true|
		var filenameparts;

		Validator.validateMethodParameterType(filename, String, "filename", "Project", "includeFile");
		if (filename.split($/).size > 1, {
			Error(postln(format("The filename '%' provided to Project.includeFile must be just a filename, not a path.", filename))).throw;
		});

		filenameparts = filename.split($.);
		if (filenameparts.size == 1, {
			filename = filename ++ ".scd";
		});
		if (filenameparts.size > 1, {
			if (filenameparts[filenameparts.size - 1] != "scd", {
				Error(postln(format("The filename '%' provided to Project.includeFile must be a .scd file.", filename))).throw;
			});
		});

		if (File.exists("%/%".format(thisProcess.nowExecutingPath.dirname, filename)) == false, {
			Error(postln(format("The filename '%' provided to Project.includeFile does not exist.", filename))).throw;
		});

		pr_Files = pr_Files.add((
			filename: filename,
			openInIde: openInIde,
			runContents: runContents
		));
	}

	init {
		pr_Condition = Condition();
		pr_Files = Array();
	}

	*new {
		^super.new.init();
	}

	setupProject {
		{
			Setup.server(pr_Condition);
			Setup.midi;
			pr_Files.do({
				|file|
				if (file.runContents, {
					var setupfunc = file.filename.loadRelative[0];
					if (setupfunc.isMemberOf(Function), {
						// If the script returns a function, evaluate it and pass it the Condition
						// This allows handling of asynchronous operations against the server
						setupfunc.value(pr_Condition);
					});
				});

				if (file.openInIde, {
					Document.open("%/%".format(thisProcess.nowExecutingPath.dirname, file.filename));
				});
			});
			postln("PROJECT SETUP COMPLETE");
		}.fork;
	}
}