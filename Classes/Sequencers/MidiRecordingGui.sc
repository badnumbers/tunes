MidiRecordingGui : SCViewHolder {
	init {
		|parent,bounds|
		this.view = View();
		this.view.background_(Color.white);
	}

	*new {
		|parent,bounds|
		^super.new.init(parent,bounds);
	}
}