SequencerKeyRouterUnitTests : BNUnitTest {
	test_metronomeToggle_matchesCtrlMQtKey {
		var router, result;
		router = SequencerKeyRouter.new;
		router.activeContext_(\record);
		result = router.handleKeyDown(
			nil,
			ViewKeyModifier.ctrl,
			ViewUnicode.none,
			ViewKeycode.none,
			ViewQtKey.m
		);
		this.assertEquals(result.isHandled, true);
	}

	test_metronomeToggle_matchesCtrlMCarriageReturnChar {
		var router, result;
		router = SequencerKeyRouter.new;
		router.activeContext_(\record);
		result = router.handleKeyDown(
			Char.ret,
			ViewKeyModifier.ctrl,
			ViewUnicode.carriageReturn,
			ViewKeycode.none,
			ViewQtKey.m
		);
		this.assertEquals(result.isHandled, true);
	}

	test_snapNotes_matchesS {
		var router, result, invoked;
		invoked = false;
		router = SequencerKeyRouter.new;
		router.activeContext_(\record);
		router.on(\record, \snapNotes, { invoked = true; });
		result = router.handleKeyDown($s, 0, $s.ascii, $s.ascii, $s.ascii);
		this.assertEquals(result.isHandled, true);
		this.assertEquals(invoked, true);
	}

	test_nudgeLeft_requiresCtrlAndLeftArrowKeycode {
		var router, result, invoked;
		invoked = false;
		router = SequencerKeyRouter.new;
		router.activeContext_(\record);
		router.on(\record, \nudgeLeft, { invoked = true; });
		result = router.handleKeyDown(
			nil,
			ViewKeyModifier.ctrl,
			ViewUnicode.none,
			ViewKeycode.leftArrow,
			ViewQtKey.left
		);
		this.assertEquals(result.isHandled, true);
		this.assertEquals(result.consume, true);
		this.assertEquals(invoked, true);
	}

	test_plainLeftArrow_doesNotInvokeNudge_returnsConsumeFalse {
		var router, result, invoked;
		invoked = false;
		router = SequencerKeyRouter.new;
		router.activeContext_(\record);
		router.on(\record, \nudgeLeft, { invoked = true; });
		result = router.handleKeyDown(
			nil,
			0,
			ViewUnicode.none,
			ViewKeycode.leftArrow,
			ViewQtKey.left
		);
		this.assertEquals(result.isHandled, false);
		this.assertEquals(result.consume, false);
		this.assertEquals(invoked, false);
	}

	test_gridResolutionEntry_accumulatesDigitsUntilGKeyUp {
		var router, digitsLog, committed;
		digitsLog = List.new;
		router = SequencerKeyRouter.new;
		router.activeContext_(\record);
		router.on(\record, \gridResolutionDigitsChanged, { |digits| digitsLog.add(digits); });
		router.on(\record, \gridResolutionCommitted, { |denominator| committed = denominator; });
		router.handleKeyDown($g, 0, $g.ascii, $g.ascii, $g.ascii);
		router.handleKeyDown($1, 0, $1.ascii, $1.ascii, $1.ascii);
		router.handleKeyDown($6, 0, $6.ascii, $6.ascii, $6.ascii);
		router.handleKeyUp($g, 0, $g.ascii, $g.ascii, $g.ascii);
		this.assertEquals(digitsLog.asArray, ["1", "16"]);
		this.assertEquals(committed, 16);
	}

	test_recordBindings_ignoredInArrangeContext {
		var router, invoked;
		invoked = false;
		router = SequencerKeyRouter.new;
		router.activeContext_(\arrange);
		router.on(\record, \snapNotes, { invoked = true; });
		router.handleKeyDown($s, 0, $s.ascii, $s.ascii, $s.ascii);
		this.assertEquals(invoked, false);
	}

	test_bindingWithNilModifiers_doesNotError {
		var router, result;
		router = SequencerKeyRouter.new;
		router.activeContext_(\record);
		result = router.handleKeyDown(
			nil,
			ViewKeyModifier.ctrl,
			ViewUnicode.none,
			ViewKeycode.control,
			ViewQtKey.control
		);
		this.assertEquals(result.isHandled, false);
	}

	test_unhandledKey_returnsEventNotRouter {
		var router, result;
		router = SequencerKeyRouter.new;
		router.activeContext_(\record);
		result = router.handleKeyDown($`, 0, $`.ascii, $`.ascii, $`.ascii);
		this.assertEquals(result.isKindOf(Event), true);
		this.assertEquals(result.isHandled, false);
	}

	test_unhandledKey_keyDownReturnValue_doesNotError {
		var router, returnValue;
		router = SequencerKeyRouter.new;
		router.activeContext_(\record);
		returnValue = router.keyDownReturnValue($`, 0, $`.ascii, $`.ascii, $`.ascii);
		this.assertEquals(returnValue.isNil, true);
	}
}
