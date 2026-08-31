WriteCommandTestDocument : SequencerDocument {
	var <>lastInserted;

	insertPattern {
		|string|
		lastInserted = string;
		^this;
	}

	*new {
		^super.newCopyArgs;
	}
}

WriteCommandUnitTests : BNUnitTest {
	test_execute_nilDocument_doesNotError {
		var cmd = WriteCommand.new;
		cmd.execute((loopStart: 0, loopEnd: 8));
		this.assertEquals(cmd.name, "write");
	}

	test_execute_withDocument_insertsPattern {
		var document, cmd;
		document = WriteCommandTestDocument.new;
		cmd = WriteCommand(document);
		cmd.execute((loopStart: 0, loopEnd: 8));
		this.assertEquals(document.lastInserted, "// PianoRoll write (dummy); loopStart=0, loopEnd=8, length=8");
	}

	test_isValid_requiresLoopBounds {
		var cmd = WriteCommand.new;
		this.assertEquals(cmd.isValid(()), false);
		this.assertEquals(cmd.isValid(nil), false);
		this.assertEquals(cmd.isValid((loopStart: 0, loopEnd: 8)), true);
	}

	test_loopParameterTypes {
		var cmd = WriteCommand.new;
		var loopStart = cmd.getParameter("loopStart");
		var loopEnd = cmd.getParameter("loopEnd");
		this.assertNotNil(loopStart);
		this.assertNotNil(loopEnd);
		this.assertEquals(loopStart.type, Number);
		this.assertEquals(loopEnd.type, Number);
	}

	test_name_isWrite {
		var cmd = WriteCommand.new;
		this.assertEquals(cmd.name, "write");
	}
}
