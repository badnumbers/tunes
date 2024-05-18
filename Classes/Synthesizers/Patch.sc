Patch {
	var <>name;
	var <kvps;

	describe {
		postln(format("Don't know how to describe a %.", this.class.name));
	}

	set {
		|key, value|
		kvps[key] = value;
		^value;
	}
}