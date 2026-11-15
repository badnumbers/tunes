SetupTestMidiOut {
	doesNotUnderstand {
		|selector ... args, kwargs|
		^this
	}

	next {
		|inval|
		^this
	}

	uid {
		^0
	}
}
