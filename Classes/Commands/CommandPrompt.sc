CommandPrompt : SCViewHolder {
	var prActiveCommand;
	var prActiveParameter;
	var prAmbientParameters;
	var prCommands;
	var prCommittedArgs;
	var prCommittedTokens;
	var prHighlightSuggestionColour;
	var prInputRowLayout;
	var prInputRowSpacing;
	var prKeyDownString;
	var prMatchingItems;
	var prNormalSuggestionColour;
	var prOverlayParent;
	var prPalette;
	var prSelectedIndex;
	var prStatusIndicator;
	var prSuggestionRows;
	var prSuggestionsView;
	var prTextField;
	var prTextFieldHeight;
	var prViewWidth;

	ambientParameters {
		^prAmbientParameters;
	}

	ambientParameters_ {
		|newAmbientParameters|
		Validator.validateMethodParameterType(newAmbientParameters, Dictionary, "newAmbientParameters", "CommandPrompt", "ambientParameters_", allowNil: true);
		prAmbientParameters = newAmbientParameters ?? { IdentityDictionary.new };
	}

	commands {
		^prCommands;
	}

	commands_ {
		|newCommands|
		prCommands = newCommands;
		this.prUpdateSuggestions;
	}

	init {
		|parent, bounds, commands, palette, ambientParameters, overlayParent|
		var effectiveOverlayParent;
		prCommands = commands;
		prPalette = palette;
		prAmbientParameters = ambientParameters ?? { IdentityDictionary.new };
		prTextFieldHeight = if (bounds.notNil, { bounds.height }, { 28 });
		prViewWidth = if (bounds.notNil, { bounds.width }, { nil });
		prInputRowSpacing = 2;
		prNormalSuggestionColour = Color.gray(0.25);
		prHighlightSuggestionColour = Color.gray(0.45);
		prKeyDownString = "";
		prCommittedTokens = [];
		prCommittedArgs = Dictionary.new;
		prMatchingItems = [];
		prSuggestionRows = [];
		prSelectedIndex = nil;
		prActiveCommand = nil;
		prActiveParameter = nil;

		// Root uses HLayout so input row and text field span full width in any layout.
		this.view = View(parent, bounds).fixedHeight_(prTextFieldHeight)
			.layout_(prInputRowLayout = HLayout().margins_(0).spacing_(prInputRowSpacing));
		prTextField = TextField().fixedHeight_(prTextFieldHeight).minWidth_(0);
		prStatusIndicator = StaticText().fixedWidth_(24).fixedHeight_(prTextFieldHeight)
			.align_(\center).font_(Font.default);
		prInputRowLayout.add(prTextField);
		prInputRowLayout.add(prStatusIndicator);

		// Overlay view created on overlayParent (or this.view if not provided)
		prOverlayParent = overlayParent ?? { this.view };
		prSuggestionsView = View(prOverlayParent, Rect(0, 0, 0, 0))
			.background_(Color.clear)
			.layout_(VLayout().margins_(0).spacing_(prInputRowSpacing))
			.visible_(false);

		if (prViewWidth.notNil, {
			this.view.minWidth_(prViewWidth).maxWidth_(prViewWidth);
		});

		this.prInstallTextFieldKeyHandlers;
		this.prUpdateStatusIndicator;
	}

	*new {
		|parent, bounds, commands, palette, ambientParameters, overlayParent|
		Validator.validateMethodParameterType(commands, Array, "commands", "CommandPrompt", "new");
		commands.do({
			|command|
			Validator.validateMethodParameterType(command, Command, "commands", "CommandPrompt", "new");
		});
		Validator.validateMethodParameterType(palette, GuiPalette, "palette", "CommandPrompt", "new", allowNil: true);
		Validator.validateMethodParameterType(ambientParameters, Dictionary, "ambientParameters", "CommandPrompt", "new", allowNil: true);
		Validator.validateMethodParameterType(overlayParent, View, "overlayParent", "CommandPrompt", "new", allowNil: true);
		^super.new.init(parent, bounds, commands, palette ?? { GuiPalette.default }, ambientParameters ?? { IdentityDictionary.new }, overlayParent);
	}

	palette {
		^prPalette;
	}

	prApplySelection {
		prSuggestionRows.do({
			|row, index|
			row.background_(if (index == prSelectedIndex, {
				prHighlightSuggestionColour;
			}, {
				prNormalSuggestionColour;
			}));
		});
	}

	prClearSuggestions {
		prSuggestionsView.children.copy.do({
			|child|
			child.remove;
		});
		prSuggestionRows = [];
		prMatchingItems = [];
		prSelectedIndex = nil;
		prSuggestionsView.visible = false;
	}

	prCommitHighlightedToken {
		var candidateString, chip, cmd, param, parsedVal, state;
		state = this.prCurrentState;

		if (state == \expectingCommand, {
			if (prSelectedIndex.notNil && { prMatchingItems.size > 0 }, {
				cmd = prMatchingItems[prSelectedIndex];
				chip = this.prMakeTokenChip(cmd.name, prPalette.colour1);
				prInputRowLayout.insert(chip, prCommittedTokens.size);
				prCommittedTokens = prCommittedTokens.add((type: \command, name: cmd.name, view: chip, command: cmd));
				prActiveCommand = cmd;
				if (prAmbientParameters.notNil, {
					cmd.parameters.do({
						|paramItem|
						var supplier = prAmbientParameters[paramItem.name.asSymbol];
						if (supplier.isNil, {
							supplier = prAmbientParameters[paramItem.name.asString];
						});
						if (supplier.notNil, {
							var val = if (supplier.isKindOf(Function), { supplier.value }, { supplier });
							prCommittedArgs[paramItem.name.asSymbol] = val;
						});
					});
				});
				prTextField.string_("");
				this.prClearSuggestions;
				this.prUpdateStatusIndicator;
				this.prUpdateSuggestions;
				prTextField.focus;
			});
		});

		if (state == \expectingParameter, {
			if (prSelectedIndex.notNil && { prMatchingItems.size > 0 }, {
				param = prMatchingItems[prSelectedIndex];
				chip = this.prMakeTokenChip(param.name, prPalette.colour2);
				prInputRowLayout.insert(chip, prCommittedTokens.size);
				prCommittedTokens = prCommittedTokens.add((type: \parameter, name: param.name, view: chip, parameter: param));
				prActiveParameter = param;
				prTextField.string_("");
				this.prClearSuggestions;
				this.prUpdateStatusIndicator;
				this.prUpdateSuggestions;
				prTextField.focus;
			});
		});

		if (state == \expectingArgument, {
			candidateString = prTextField.string.stripWhiteSpace;
			if (candidateString.isEmpty && { prSelectedIndex.notNil } && { prMatchingItems.size > 0 }, {
				candidateString = prMatchingItems[prSelectedIndex].asString;
			});
			if (candidateString.notEmpty && { prActiveParameter.isValid(candidateString) }, {
				parsedVal = prActiveParameter.parse(candidateString);
				chip = this.prMakeTokenChip(candidateString, prPalette.colour3);
				prInputRowLayout.insert(chip, prCommittedTokens.size);
				prCommittedTokens = prCommittedTokens.add((type: \argument, name: candidateString, view: chip, parameter: prActiveParameter, value: parsedVal));
				prCommittedArgs[prActiveParameter.name.asSymbol] = parsedVal;
				prActiveParameter = nil;
				prTextField.string_("");
				this.prClearSuggestions;
				this.prUpdateStatusIndicator;
				this.prUpdateSuggestions;
				prTextField.focus;
			});
		});
	}

	prCurrentState {
		var unassigned;
		if (prActiveCommand.isNil, {
			^\expectingCommand;
		});
		if (prActiveParameter.isNil, {
			unassigned = prActiveCommand.parameters.reject({
				|param|
				prCommittedArgs[param.name.asSymbol].notNil;
			});
			if (unassigned.size > 0, {
				^\expectingParameter;
			}, {
				^\satisfied;
			});
		});
		^\expectingArgument;
	}

	prCurrentWord {
		var words = prTextField.string.split($ );
		^if (words.size > 0, { words[words.size - 1] }, { "" });
	}

	prExecuteCommand {
		if (prActiveCommand.notNil && { prActiveCommand.isValid(prCommittedArgs) }, {
			prActiveCommand.execute(prCommittedArgs);
			this.prResetPrompt;
		});
	}

	prInstallTextFieldKeyHandlers {
		prTextField.keyDownAction_({
			|view, char, modifiers, unicode, keycode, key|
			var isReturnKey = false, keyHandled = nil;
			prKeyDownString = prTextField.string;

			if (char.notNil, {
				if ((char == $\r) || (char == $\n) || (char.ascii == 13) || (char.ascii == 10), {
					isReturnKey = true;
				});
			});

			if (isReturnKey, {
				if (prActiveCommand.notNil && { prActiveCommand.isValid(prCommittedArgs) }, {
					this.prExecuteCommand;
					keyHandled = true;
				});
			});

			if (keyHandled.isNil && { prSuggestionRows.size > 0 }, {
				if (keycode == ViewKeycode.upArrow, {
					this.prMoveSelection(-1);
					keyHandled = true;
				});
				if (keycode == ViewKeycode.downArrow, {
					this.prMoveSelection(1);
					keyHandled = true;
				});
			});

			if (keyHandled.isNil && { (char == $\t) || (keycode == ViewKeycode.tab) }, {
				this.prCommitHighlightedToken;
				keyHandled = true;
			});

			if (keyHandled.isNil
				&& { prTextField.string.size == 0 }
				&& { prCommittedTokens.size > 0 }
				&& { (char == $\b) || (keycode == ViewKeycode.backspace) }, {
				this.prRemoveLastCommittedToken;
				keyHandled = true;
			});

			keyHandled;
		});

		prTextField.keyUpAction_({
			|view, char, modifiers, unicode, keycode, key|
			if ([ViewKeycode.upArrow, ViewKeycode.downArrow, ViewKeycode.tab].includes(keycode).not
				&& { char != $\t }
				&& { prTextField.string != prKeyDownString }, {
				this.prUpdateSuggestions;
			});
			nil;
		});
	}

	prMakeTokenChip {
		|text, colour|
		var width;
		width = text.asString.bounds(Font.default).width + 8;
		^View().fixedWidth_(width).maxWidth_(width).minWidth_(width)
			.fixedHeight_(prTextFieldHeight).layout_(
			HLayout(
				StaticText().font_(Font.default).string_(text.asString)
					.stringColor_(Color.white).align_(\center)
			).margins_(4@0)
		).background_(colour);
	}

	prMoveSelection {
		|direction|
		var size = prSuggestionRows.size;
		if (size == 0, { ^this });
		if (prSelectedIndex.isNil, {
			prSelectedIndex = if (direction > 0, { 0 }, { size - 1 });
		}, {
			prSelectedIndex = (prSelectedIndex + direction).wrap(0, size - 1);
		});
		this.prApplySelection;
	}

	prPositionSuggestionsView {
		|width, height|
		var tfWindowPos, overlayParentWindowPos, tfPosInOverlayParent;
		if (prSuggestionsView.notNil && { prOverlayParent.notNil } && { prTextField.notNil }, {
			tfWindowPos = prTextField.mapToGlobal(0 @ 0);
			overlayParentWindowPos = prOverlayParent.mapToGlobal(0 @ 0);
			tfPosInOverlayParent = tfWindowPos - overlayParentWindowPos;

			prSuggestionsView.bounds = Rect(
				tfPosInOverlayParent.x,
				tfPosInOverlayParent.y + prTextFieldHeight + prInputRowSpacing,
				width,
				height
			);
		});
	}

	prRemoveLastCommittedToken {
		var lastToken;
		if (prCommittedTokens.size == 0, { ^this });
		lastToken = prCommittedTokens.last;
		lastToken[\view].remove;
		prCommittedTokens = prCommittedTokens.copy;
		prCommittedTokens.pop;

		if (lastToken[\type] == \argument, {
			prCommittedArgs.removeAt(lastToken[\parameter].name.asSymbol);
			prActiveParameter = lastToken[\parameter];
		});

		if (lastToken[\type] == \parameter, {
			prActiveParameter = nil;
		});

		if (lastToken[\type] == \command, {
			prActiveCommand = nil;
			prActiveParameter = nil;
			prCommittedArgs = Dictionary.new;
		});

		this.prClearSuggestions;
		this.prUpdateStatusIndicator;
		this.prUpdateSuggestions;
		prTextField.focus;
	}

	prResetPrompt {
		prCommittedTokens.do({ |token| token[\view].remove });
		prCommittedTokens = [];
		prCommittedArgs = Dictionary.new;
		prActiveCommand = nil;
		prActiveParameter = nil;
		prTextField.string_("");
		this.prClearSuggestions;
		this.prUpdateStatusIndicator;
		this.prUpdateSuggestions;
		prTextField.focus;
	}

	prUpdateStatusIndicator {
		var isExecutable = false;
		if (prActiveCommand.notNil && { prActiveCommand.isValid(prCommittedArgs) }, {
			isExecutable = true;
		});

		if (isExecutable, {
			prStatusIndicator.string_("✓").stringColor_(Color.new255(80, 200, 80));
		}, {
			if (prActiveCommand.notNil, {
				prStatusIndicator.string_("…").stringColor_(Color.gray(0.6));
			}, {
				prStatusIndicator.string_("").stringColor_(Color.clear);
			});
		});
	}

	prUpdateSuggestions {
		var currentWord, matchingItems, state, suggestionHeight, suggestionWidth, suggestionsHeight, unassigned, words;
		words = prTextField.string.split($ );
		this.prClearSuggestions;
		matchingItems = [];
		currentWord = if (words.size > 0, { words[words.size - 1] }, { "" });
		state = this.prCurrentState;

		if (state == \expectingCommand, {
			matchingItems = prCommands.select({
				|cmd|
				cmd.name.beginsWith(currentWord);
			});
		});

		if (state == \expectingParameter, {
			unassigned = prActiveCommand.parameters.reject({
				|param|
				prCommittedArgs[param.name.asSymbol].notNil;
			});
			matchingItems = unassigned.select({
				|param|
				param.name.beginsWith(currentWord);
			});
		});

		if (state == \expectingArgument, {
			if (prActiveParameter.notNil && { prActiveParameter.constraint.notNil } && { prActiveParameter.constraint.isKindOf(Collection) }, {
				matchingItems = prActiveParameter.constraint.as(Array).collect(_.asString).select({
					|itemStr|
					itemStr.beginsWith(currentWord);
				});
			});
		});

		prMatchingItems = matchingItems;
		if (matchingItems.size > 0, {
			suggestionHeight = prTextFieldHeight;
			suggestionWidth = matchingItems.collect({
				|item|
				var label = if (item.respondsTo(\name), { item.name }, { item.asString });
				label.bounds(Font.default).width + 8;
			}).maxItem;
			suggestionsHeight = (matchingItems.size * suggestionHeight)
				+ ((matchingItems.size - 1) * prInputRowSpacing);
			this.prPositionSuggestionsView(suggestionWidth, suggestionsHeight);
			matchingItems.do({
				|item|
				var label = if (item.respondsTo(\name), { item.name }, { item.asString });
				var row = View().fixedSize_(suggestionWidth @ suggestionHeight).layout_(
					HLayout(
						StaticText().font_(Font.default).string_(label)
							.stringColor_(Color.white).align_(\left)
					).margins_(4@0)
				).background_(prNormalSuggestionColour);
				prSuggestionsView.layout.add(row);
				prSuggestionRows = prSuggestionRows.add(row);
			});
			prSelectedIndex = 0;
			this.prApplySelection;
			prSuggestionsView.visible = true;
		}, {
			prSuggestionsView.visible = false;
		});

		this.prUpdateStatusIndicator;
	}

	string {
		^prTextField.string;
	}

	string_ {
		|newString|
		prTextField.string_(newString);
		this.prUpdateSuggestions;
	}
}
