LeftCommand : Command {
	execute {
		|args|
		var resolution = args[\resolution];
		var selectedNotes = args[\selectedNotes] ? [];
		var gridSpacing;
		if (resolution.notNil && { resolution.isKindOf(Integer) } && { resolution.inclusivelyBetween(1, 16) }, {
			gridSpacing = 1.0 / resolution;
			selectedNotes.do({
				|note|
				if (note.respondsTo(\nudgeLeft), {
					note.nudgeLeft(gridSpacing);
				});
			});
		});
	}

	*new {
		^super.new("left", [
			Parameter("selectedNotes", Array),
			Parameter("resolution", Integer, constraint: { |v| v.inclusivelyBetween(1, 16) })
		]);
	}
}
