Patch {
	var <>name;
	var <kvps;
	describe {

	}

	set {
		|key, value|
		kvps[key] = value;
		^value;
	}
}