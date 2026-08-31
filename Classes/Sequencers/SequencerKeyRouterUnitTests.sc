SequencerKeyRouterUnitTests : BNUnitTest {
	test_recordBindings_ignoredInArrangeContext {
		var router, invoked;
		invoked = false;
		router = SequencerKeyRouter.new;
		router.activeContext_(\arrange);
		router.on(\record, \assignPart1, { invoked = true; });
		router.handleKeyDown($1, 0, $1.ascii, $1.ascii, $1.ascii);
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
