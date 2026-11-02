PatternChip : BorderView {
	classvar prPlayingChip;

	var prSequenceId;
	var prSequencer;
	var prDragAction;
	var prDragging = false;
	var prGrab;
	var prDownParent;

	*new {
		|parent, sequencer, sequenceId|
		var font, label, text;
		Validator.validateMethodParameterType(parent, View, "parent", "PatternChip", "new");
		Validator.validateMethodParameterType(sequencer, Sequencer, "sequencer", "PatternChip", "new");
		Validator.validateMethodParameterType(sequenceId, Symbol, "sequenceId", "PatternChip", "new");
		text = sequenceId.asString;
		font = Font(size: 16);
		label = text.bounds(font);
		^super.new(parent, Rect(0, 0, label.width + 16, label.height + 8)).prInit(sequencer, sequenceId);
	}

	prInit {
		|sequencer, sequenceId|
		var font, text;
		prSequencer = sequencer;
		prSequenceId = sequenceId;
		text = sequenceId.asString;
		font = Font(size: 16);
		this.borderRadius_(5);
		this.background_(GuiPalette.default.colour3);
		this.borderColour_(GuiPalette.default.colour4);
		this.borderWidth_(1);
		StaticText(this.view, Rect(2, 2, this.view.bounds.width - 4, this.view.bounds.height - 4))
			.string_(text)
			.font_(font)
			.align_(\center)
			.stringColor_(GuiPalette.default.extreme2)
			.background_(Color.clear)
			.acceptsMouse_(false);
		this.view.mouseDownAction_({
			|view, x, y, modifiers, buttonNumber, clickCount|
			if (buttonNumber == 0, {
				if (clickCount == 2, {
					this.prTogglePlay;
				});
				prGrab = x@y;
				prDownParent = view.bounds.origin + prGrab;
				prDragging = false;
			});
			nil
		});
		this.view.mouseMoveAction_({
			|view, x, y|
			var maxX, maxY, newOrigin, parentPoint;
			if (prGrab.notNil, {
				parentPoint = view.bounds.origin + (x@y);
				if (prDragging.not && { parentPoint.dist(prDownParent) > 4 }, {
					prDragging = true;
					view.front;
				});
				if (prDragging, {
					newOrigin = parentPoint - prGrab;
					if (view.parent.notNil, {
						maxX = (view.parent.bounds.width - view.bounds.width).max(0);
						maxY = (view.parent.bounds.height - view.bounds.height).max(0);
						newOrigin = newOrigin.x.clip(0, maxX).round.asInteger @ newOrigin.y.clip(0, maxY).round.asInteger;
					});
					view.bounds = view.bounds.moveTo(newOrigin.x, newOrigin.y);
				});
			});
			nil
		});
		this.view.mouseUpAction_({
			|view|
			if (prDragging && { prDragAction.notNil }, {
				prDragAction.value(view.bounds.origin);
			});
			prDragging = false;
			prGrab = nil;
			nil
		});
		^this;
	}

	prTogglePlay {
		if (prPlayingChip === this, {
			prSequencer.stop;
			this.borderColour_(GuiPalette.default.colour4);
			this.borderWidth_(1);
			prPlayingChip = nil;
		}, {
			prSequencer.play(prSequenceId, true);
			if (prPlayingChip.notNil, {
				prPlayingChip.borderColour_(GuiPalette.default.colour4);
				prPlayingChip.borderWidth_(1);
			});
			this.borderColour_(GuiPalette.default.extreme2);
			this.borderWidth_(1);
			prPlayingChip = this;
		});
	}

	dragAction_ {
		|action|
		Validator.validateMethodParameterType(action, Function, "action", "PatternChip", "dragAction_", allowNil: true);
		prDragAction = action;
	}

	moveTo {
		|origin|
		Validator.validateMethodParameterType(origin, Point, "origin", "PatternChip", "moveTo");
		this.view.bounds = this.view.bounds.moveTo(origin.x, origin.y);
	}
}
