WriteCommandUnitTests : BNUnitTest {
	test_actionCallback_isInvoked {
		var invoked = false;
		var cmd = WriteCommand({ |args| invoked = true; });
		cmd.execute(());
		this.assertEquals(invoked, true);
	}

	test_name_isWrite {
		var cmd = WriteCommand.new;
		this.assertEquals(cmd.name, "write");
	}

	test_isValid_withNoParameters {
		var cmd = WriteCommand.new;
		this.assertEquals(cmd.isValid(()), true);
		this.assertEquals(cmd.isValid(nil), true);
	}
}
