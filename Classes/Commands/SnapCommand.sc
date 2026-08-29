SnapCommand : Command {
	*new {
		|action|
		Validator.validateMethodParameterType(action, Function, "action", "SnapCommand", "new", allowNil: true);
		^super.new("snap", [
			Parameter("selectedNotes", Array),
			Parameter("resolution", Integer, constraint: { |v| v.inclusivelyBetween(1, 16) })
		], {
			|args|
			var resolution = args[\resolution];
			var selectedNotes = args[\selectedNotes] ? [];
			var gridSpacing;
			if (resolution.notNil && { resolution.isKindOf(Integer) } && { resolution.inclusivelyBetween(1, 16) }, {
				gridSpacing = 1.0 / resolution;
				selectedNotes.do({
					|note|
					if (note.respondsTo(\snapToGrid), {
						note.snapToGrid(gridSpacing);
					});
				});
			});
			if (action.notNil, {
				action.value(args);
			});
		});
	}
}
