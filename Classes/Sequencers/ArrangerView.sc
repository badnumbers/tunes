Arranger : SCViewHolder {
	var prSequenceNames;
	var prSequencer;

	addSequence {
		|name|
		var text;
		Validator.validateMethodParameterType(name, Symbol, "name", "Arranger", "addSequence");
		text = name.asString;
		if (prSequenceNames.includes(text), { ^this });
		prSequenceNames.add(text);
		PatternChip(this.view, prSequencer, name);
	}

	init {
		|sequencer|
		prSequencer = sequencer;
		prSequenceNames = Set();
		this.view = View().background_(GuiPalette.default.colour2);
		this.view.addFlowLayout(10@10, 10@10);
	}

	*new {
		|sequencer|
		Validator.validateMethodParameterType(sequencer, Sequencer, "sequencer", "Arranger", "new");
		^super.new.init(sequencer);
	}

}
