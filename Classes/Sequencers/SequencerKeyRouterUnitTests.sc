SequencerKeyRouterUnitTests : BNUnitTest {
	test_ctrlShiftZ_invokesRedoNotUndo {
		var redo, router, undo;
		redo = false;
		undo = false;
		router = SequencerKeyRouter.new;
		router.activeContext_(\record);
		router.on(\record, \undo, { undo = true });
		router.on(\record, \redo, { redo = true });
		router.handleKeyDown(
			nil,
			ViewKeyModifier.ctrl | ViewKeyModifier.shift,
			ViewUnicode.none,
			ViewKeycode.none,
			ViewQtKey.z
		);
		this.assertEquals(undo, false);
		this.assertEquals(redo, true);
	}

	test_ctrlY_invokesRedo {
		var invoked, result, router;
		invoked = false;
		router = SequencerKeyRouter.new;
		router.activeContext_(\record);
		router.on(\record, \redoY, { invoked = true });
		result = router.handleKeyDown(
			nil,
			ViewKeyModifier.ctrl,
			ViewUnicode.none,
			ViewKeycode.none,
			ViewQtKey.y
		);
		this.assertEquals(invoked, true);
		this.assertEquals(result.isHandled, true);
		this.assertEquals(result.consume, true);
	}

	test_ctrlZ_invokesUndoInRecordAndDrumsButNotArrange {
		var arrangeInvoked, drumsInvoked, recordInvoked, router;
		recordInvoked = false;
		drumsInvoked = false;
		arrangeInvoked = false;
		router = SequencerKeyRouter.new;
		router.activeContext_(\record);
		router.on(\record, \undo, { recordInvoked = true });
		router.on(\drums, \undo, { drumsInvoked = true });
		router.on(\arrange, \undo, { arrangeInvoked = true });
		router.handleKeyDown(nil, ViewKeyModifier.ctrl, ViewUnicode.none, ViewKeycode.none, ViewQtKey.z);
		router.activeContext_(\drums);
		router.handleKeyDown(nil, ViewKeyModifier.ctrl, ViewUnicode.none, ViewKeycode.none, ViewQtKey.z);
		router.activeContext_(\arrange);
		router.handleKeyDown(nil, ViewKeyModifier.ctrl, ViewUnicode.none, ViewKeycode.none, ViewQtKey.z);
		this.assertEquals(recordInvoked, true);
		this.assertEquals(drumsInvoked, true);
		this.assertEquals(arrangeInvoked, false);
	}

	test_deleteKey_invokesDeleteBinding {
		var invoked, result, router;
		invoked = false;
		router = SequencerKeyRouter.new;
		router.activeContext_(\record);
		router.on(\record, \delete, { invoked = true });
		result = router.handleKeyDown(nil, 0, ViewUnicode.none, ViewKeycode.none, ViewQtKey.delete);
		this.assertEquals(invoked, true);
		this.assertEquals(result.isHandled, true);
		this.assertEquals(result.consume, true);
	}

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
