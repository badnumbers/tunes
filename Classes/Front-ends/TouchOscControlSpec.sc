TouchOscControlSpec : ControlSpec {
	var <parametername;
	var <label;
	var <oscPath;

	*new { arg parametername, label, oscPath, minval = (0.0), maxval = (1.0), warp = ('lin'), step = (0.0), default, units, grid;
		^super.newCopyArgs(minval, maxval, warp, step, default, units, grid).init(parametername, label, oscPath);
	}

	init {
		|parameternameb, labelb, oscPathb|
		super.init;
		parametername = parameternameb;
		label = labelb;
		oscPath = oscPathb;
	}
}