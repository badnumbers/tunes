CompositionChip : BorderView {
	var prArmClick = false;
	var prDownParent;
	var prDragAction;
	var prDragging = false;
	var prGrab;
	var prNested;
	var prSelectAction;
	var prSequenceId;
	var prSequencer;
	var prStructure;
	var prTitleHeight;
	var prCtrlClick = false;
	var prOpenAction;

	applyPlaying {
		|playingId|
		var idleColour, playingColour;
		playingColour = GuiPalette.default.extreme2;
		idleColour = GuiPalette.default.colour4;
		if (prSequenceId == playingId, {
			this.borderColour_(playingColour);
		}, {
			this.borderColour_(idleColour);
		});
		prNested.do({
			|item|
			if (item.id == playingId, {
				item.view.borderColour_(playingColour);
			}, {
				item.view.borderColour_(idleColour);
			});
		});
	}

	dragAction_ {
		|action|
		Validator.validateMethodParameterType(action, Function, "action", "CompositionChip", "dragAction_", allowNil: true);
		prDragAction = action;
	}

	openAction_ {
		|action|
		Validator.validateMethodParameterType(action, Function, "action", "CompositionChip", "openAction_", allowNil: true);
		prOpenAction = action;
	}

	moveTo {
		|origin|
		Validator.validateMethodParameterType(origin, Point, "origin", "CompositionChip", "moveTo");
		this.view.bounds = this.view.bounds.moveTo(origin.x, origin.y);
	}

	*new {
		|parent, sequencer, sequenceId, structure, selectAction|
		Validator.validateMethodParameterType(parent, View, "parent", "CompositionChip", "new");
		Validator.validateMethodParameterType(sequencer, Sequencer, "sequencer", "CompositionChip", "new");
		Validator.validateMethodParameterType(sequenceId, Symbol, "sequenceId", "CompositionChip", "new");
		Validator.validateMethodParameterType(structure, PatternStructure, "structure", "CompositionChip", "new");
		Validator.validateMethodParameterType(selectAction, Function, "selectAction", "CompositionChip", "new");
		^super.new(parent, Rect(0, 0, 1, 1)).prInit(sequencer, sequenceId, structure, selectAction);
	}

	prBuild {
		|font|
		var frameHeight, frameWidth, root, title, titleHeight, titleSize, titleView;
		title = prSequenceId.asString;
		titleSize = title.bounds(font);
		titleHeight = titleSize.height + 8;
		root = this.prLayout(prStructure, font, prSequenceId);
		frameWidth = max(titleSize.width + 16, root.chipWidth + 16);
		frameHeight = titleHeight + root.chipHeight + 8;
		this.view.bounds = Rect(0, 0, frameWidth, frameHeight);
		this.background_(GuiPalette.default.colour3);
		this.borderColour_(GuiPalette.default.colour4);
		this.borderWidth_(1);
		this.borderRadius_(5);
		prTitleHeight = titleHeight;
		titleView = StaticText(this.view, Rect(2, 2, frameWidth - 4, titleHeight - 4))
			.string_(title)
			.font_(font)
			.align_(\center)
			.stringColor_(GuiPalette.default.extreme2)
			.background_(Color.clear)
			.acceptsMouse_(false);
		this.prPlace(root, this.view, ((frameWidth - root.chipWidth) / 2).asInteger, titleHeight);
		this.prWireFrame;
	}

	prInit {
		|sequencer, sequenceId, structure, selectAction|
		prSequencer = sequencer;
		prSequenceId = sequenceId;
		prStructure = structure;
		prSelectAction = selectAction;
		prNested = [];
		this.prBuild(Font(size: 16));
		^this;
	}

	prLayout {
		|structure, font, ownerId|
		var childLayouts, chipText, direction, innerHeight, innerWidth, selectId, textSize;
		selectId = structure.sequenceId ? ownerId;
		chipText = structure.label;
		if (structure.repeats.notNil, {
			chipText = format("% x %", chipText, structure.repeats.asString);
		});
		textSize = chipText.bounds(font);
		direction = structure.direction;
		if (direction == \leaf, {
			^(
				chipWidth: textSize.width + 16,
				chipHeight: textSize.height + 8,
				chipText: chipText,
				chipTextHeight: textSize.height + 4,
				chipSequenceId: structure.sequenceId,
				chipSelectId: selectId,
				chipChildren: []
			);
		});
		childLayouts = structure.children.collect({
			|child|
			this.prLayout(child, font, selectId);
		});
		innerWidth = 0;
		innerHeight = 0;
		if (direction == \horizontal, {
			childLayouts.do({
				|child, index|
				child.put(\chipX, 8 + innerWidth);
				child.put(\chipY, textSize.height + 18);
				innerWidth = innerWidth + child.chipWidth;
				if (index < (childLayouts.size - 1), {
					innerWidth = innerWidth + 10;
				});
				innerHeight = max(innerHeight, child.chipHeight);
			});
		}, {
			childLayouts.do({
				|child, index|
				child.put(\chipX, 8);
				child.put(\chipY, textSize.height + 18 + innerHeight);
				innerHeight = innerHeight + child.chipHeight;
				if (index < (childLayouts.size - 1), {
					innerHeight = innerHeight + 10;
				});
				innerWidth = max(innerWidth, child.chipWidth);
			});
		});
		^(
			chipWidth: max(textSize.width + 16, innerWidth + 16),
			chipHeight: textSize.height + 26 + innerHeight,
			chipText: chipText,
			chipTextHeight: textSize.height + 4,
			chipSequenceId: structure.sequenceId,
			chipSelectId: selectId,
			chipChildren: childLayouts
		);
	}

	prPlace {
		|layout, parent, x, y|
		var chip, textView;
		chip = BorderView(parent, Rect(x, y, layout.chipWidth, layout.chipHeight));
		chip.background_(GuiPalette.default.colour3);
		chip.borderColour_(GuiPalette.default.colour4);
		chip.borderWidth_(1);
		chip.borderRadius_(5);
		textView = StaticText(chip.view, Rect(2, 2, layout.chipWidth - 4, layout.chipTextHeight))
			.string_(layout.chipText)
			.font_(Font(size: 16))
			.align_(\center)
			.stringColor_(GuiPalette.default.extreme2)
			.background_(Color.clear)
			.acceptsMouse_(false);
		this.prWire(chip, layout);
		prNested = prNested.add((view: chip, id: layout.chipSequenceId));
		layout.chipChildren.do({
			|child|
			this.prPlace(child, chip.view, child.chipX, child.chipY);
		});
	}

	prTogglePlay {
		|id|
		if (prSequencer.playingId == id, {
			prSequencer.stop;
		}, {
			prSequencer.play(id, true);
		});
	}

	prWire {
		|chip, layout|
		var arm, openClick;
		arm = false;
		openClick = false;
		chip.view.mouseDownAction_({
			|view, x, y, modifiers, buttonNumber, clickCount|
			if (buttonNumber == 0, {
				openClick = modifiers.isCtrl and: { clickCount == 1 };
				if (clickCount == 2 and: { modifiers.isCtrl.not }, {
					this.prTogglePlay(layout.chipSequenceId ? layout.chipSelectId);
				});
				arm = true;
			});
			nil
		});
		chip.view.mouseUpAction_({
			if (arm and: { openClick } and: { layout.chipSequenceId.notNil } and: { prOpenAction.notNil }, {
				prOpenAction.value(layout.chipSequenceId);
			}, {
				if (arm and: { openClick.not } and: { layout.chipSelectId.notNil }, {
					prSelectAction.value(layout.chipSelectId);
				});
			});
			arm = false;
			openClick = false;
			nil
		});
	}

	prWireFrame {
		this.view.mouseDownAction_({
			|view, x, y, modifiers, buttonNumber, clickCount|
			if (buttonNumber == 0, {
				prCtrlClick = modifiers.isCtrl and: { clickCount == 1 };
				if (clickCount == 2 and: { modifiers.isCtrl.not }, {
					this.prTogglePlay(prSequenceId);
				});
				if (y < prTitleHeight, {
					prGrab = x@y;
					prDownParent = view.bounds.origin + prGrab;
					prDragging = false;
				});
				prArmClick = true;
			});
			nil
		});
		this.view.mouseMoveAction_({
			|view, x, y|
			var maxX, maxY, newOrigin, parentPoint;
			if (prGrab.notNil, {
				parentPoint = view.bounds.origin + (x@y);
				if (prDragging.not and: { parentPoint.dist(prDownParent) > 4 }, {
					prDragging = true;
					prArmClick = false;
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
			if (prDragging and: { prDragAction.notNil }, {
				prDragAction.value(this.view.bounds.origin);
			}, {
				if (prArmClick and: { prCtrlClick } and: { prOpenAction.notNil }, {
					prOpenAction.value(prSequenceId);
				}, {
					if (prArmClick, {
						prSelectAction.value(prSequenceId);
					});
				});
			});
			prArmClick = false;
			prCtrlClick = false;
			prDragging = false;
			prGrab = nil;
			nil
		});
	}

	sequenceId {
		^prSequenceId;
	}
}
