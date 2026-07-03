SequencerKeyRouter {
	var prActiveContext = \record;
	var prBindings;
	var prGridEntryActive = false;
	var prGridEntryDigits = "";
	var prHandlers;

	activeContext {
		^prActiveContext;
	}

	activeContext_ {
		|context|
		Validator.validateMethodParameterType(context, Symbol, "context", "SequencerKeyRouter", "activeContext_");
		prActiveContext = context;
	}

	gridEntryActive {
		^prGridEntryActive;
	}

	gridEntryDigits {
		^prGridEntryDigits;
	}

	handleKeyDown {
		|char, modifiers, unicode, keycode, key|
		var action;

		if (prActiveContext != \record, {
			^(this.prUnmatchedKeyDown(char, modifiers, keycode));
		});

		if (char == $g, {
			prGridEntryActive = true;
			prGridEntryDigits = "";
			this.prInvokeHandler(\gridResolutionEntryBegan);
			^(isHandled: true, consume: true);
		});

		if (prGridEntryActive, {
			if (char.notNil && { char.ascii.inclusivelyBetween(48, 57) }, {
				prGridEntryDigits = prGridEntryDigits ++ char.asString;
				this.prInvokeHandler(\gridResolutionDigitsChanged, prGridEntryDigits);
				^(isHandled: true, consume: true);
			});
			^(isHandled: true, consume: true);
		});

		action = this.prMatchingAction(char, modifiers, keycode, key);
		if (action.notNil, {
			this.prInvokeHandler(action);
			^(isHandled: true, consume: (prBindings[action][\consume] == true));
		});

		^(this.prUnmatchedKeyDown(char, modifiers, keycode));
	}

	handleKeyUp {
		|char, modifiers, unicode, keycode, key|
		var denominator;

		if ((prActiveContext == \record) && (char == $g) && prGridEntryActive, {
			prGridEntryActive = false;
			denominator = if (prGridEntryDigits.size > 0, { prGridEntryDigits.asInteger }, { nil });
			prGridEntryDigits = "";
			this.prInvokeHandler(\gridResolutionCommitted, denominator);
			^(isHandled: true, consume: true);
		});

		^(isHandled: false, consume: nil);
	}

	init {
		prBindings = this.class.defaultBindings;
		prHandlers = IdentityDictionary.new;
	}

	keyDownReturnValue {
		|char, modifiers, unicode, keycode, key|
		var result;
		result = this.handleKeyDown(char, modifiers, unicode, keycode, key);
		^(this.prReturnValueFromResult(result));
	}

	keyUpReturnValue {
		|char, modifiers, unicode, keycode, key|
		var result;
		result = this.handleKeyUp(char, modifiers, unicode, keycode, key);
		^(this.prReturnValueFromResult(result));
	}

	on {
		|context, action, handler|
		Validator.validateMethodParameterType(context, Symbol, "context", "SequencerKeyRouter", "on");
		Validator.validateMethodParameterType(action, Symbol, "action", "SequencerKeyRouter", "on");
		Validator.validateMethodParameterType(handler, Function, "handler", "SequencerKeyRouter", "on");
		if (prHandlers[context].isNil, {
			prHandlers[context] = IdentityDictionary.new;
		});
		prHandlers[context][action] = handler;
	}

	*defaultBindings {
		^(
			\metronomeToggle: (
				contexts: [\record],
				modifiers: [\ctrl],
				qtKeys: [ViewQtKey.m, ViewQtKey.return],
				charAscii: [ViewUnicode.carriageReturn],
				consume: true
			),
			\snapNotes: (
				contexts: [\record],
				char: $s,
				consume: true
			),
			\nudgeLeft: (
				contexts: [\record],
				modifiers: [\ctrl],
				keycodes: [ViewKeycode.leftArrow],
				consume: true
			),
			\nudgeRight: (
				contexts: [\record],
				modifiers: [\ctrl],
				keycodes: [ViewKeycode.rightArrow],
				consume: true
			),
			\assignPart1: (
				contexts: [\record],
				char: $1,
				consume: true
			),
			\assignPart2: (
				contexts: [\record],
				char: $2,
				consume: true
			),
			\assignPart3: (
				contexts: [\record],
				char: $3,
				consume: true
			),
			\assignPart4: (
				contexts: [\record],
				char: $4,
				consume: true
			)
		);
	}

	*new {
		^super.new.init;
	}

	prBindingMatches {
		|action, char, modifiers, keycode, key|
		var binding, contexts;

		binding = prBindings[action];
		if (binding.isNil, { ^false });

		contexts = binding[\contexts];
		if (contexts.notNil && { contexts.includes(prActiveContext).not }, { ^false });
		if (this.prModifiersMatch(modifiers, binding[\modifiers]).not, { ^false });

		if (binding[\char].notNil && (char == binding[\char]), { ^true });
		if (binding[\charAscii].notNil && char.notNil && { binding[\charAscii].includes(char.ascii) }, { ^true });
		if (binding[\qtKeys].notNil && { binding[\qtKeys].includes(key) }, { ^true });
		if (binding[\keycodes].notNil && { binding[\keycodes].includes(keycode) }, { ^true });

		^false;
	}

	prInvokeHandler {
		|action, handlerArg|
		var handler, contextHandlers;

		contextHandlers = prHandlers[prActiveContext];
		if (contextHandlers.notNil, {
			handler = contextHandlers[action];
			if (handler.notNil, {
				if (handlerArg.notNil, { handler.value(handlerArg); }, { handler.value; });
			});
		});
	}

	prMatchingAction {
		|char, modifiers, keycode, key|
		var match;

		prBindings.keysValuesDo({
			|action, binding|
			if (match.isNil && { this.prBindingMatches(action, char, modifiers, keycode, key) }, {
				match = action;
			});
		});

		^match;
	}

	prModifiersMatch {
		|modifiers, requiredModifiers|
		if (requiredModifiers.isNil, { ^true });
		if (requiredModifiers.size == 0, { ^true });
		^requiredModifiers.every({
			|modifier|
			switch (modifier,
				\ctrl, { modifiers.isCtrl },
				\shift, { modifiers.isShift },
				\alt, { modifiers.isAlt },
				{ false }
			);
		});
	}

	prReturnValueFromResult {
		|result|
		if (result.isNil, { ^nil });
		if (result.consume == true, { ^true });
		if (result.consume == false, { ^false });
		^nil;
	}

	prUnmatchedKeyDown {
		|char, modifiers, keycode|
		if ([ViewKeycode.leftArrow, ViewKeycode.rightArrow].includes(keycode) && modifiers.isCtrl.not, {
			^(isHandled: false, consume: false);
		});
		^(isHandled: false, consume: nil);
	}
}
