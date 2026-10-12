SwitchCommandTestStep {
	var prStartTime;
	var prValue;

	init {
		|startTime|
		prStartTime = startTime;
	}

	*new {
		|startTime|
		^super.new.init(startTime);
	}

	startTime {
		^prStartTime;
	}

	switch_ {
		|value|
		prValue = value;
	}

	value {
		^prValue;
	}
}

SwitchCommandUnitTests : BNUnitTest {
	prStep {
		|startTime = 0|
		^SwitchCommandTestStep(startTime);
	}

	test_allNotesInRowParameter_isAnArray {
		var cmd = SwitchCommand.new;
		var param = cmd.getParameter("allNotesInRow");
		this.assert(param.notNil, "allNotesInRow parameter should exist", true);
		this.assertEquals(param.type, Array);
		this.assertEquals(param.isArray, false);
	}

	test_execute_setsTheGivenValueAgain {
		var cmd, step;
		cmd = SwitchCommand.new;
		step = this.prStep(0);
		cmd.execute((allNotesInRow: [step], values: [1]));
		this.assertEquals(step.value, 1);
		cmd.execute((allNotesInRow: [step], values: [1]));
		this.assertEquals(step.value, 1);
		cmd.execute((allNotesInRow: [step], values: [0]));
		this.assertEquals(step.value, 0);
	}

	test_execute_sharedStartTime_sharesArrayValue {
		var cmd, steps;
		cmd = SwitchCommand.new;
		steps = [
			this.prStep(0),
			this.prStep(0.25),
			this.prStep(0.25),
			this.prStep(0.5),
			this.prStep(0.75)
		];
		cmd.execute((allNotesInRow: steps, values: [1, 0]));
		this.assertEquals(steps[0].value, 1);
		this.assertEquals(steps[1].value, 0);
		this.assertEquals(steps[2].value, 0);
		this.assertEquals(steps[3].value, 1);
		this.assertEquals(steps[4].value, 0);
	}

	test_execute_sortsByStartTimeBeforeWrapping {
		var cmd, steps;
		cmd = SwitchCommand.new;
		steps = [
			this.prStep(0.5),
			this.prStep(0),
			this.prStep(0.25)
		];
		cmd.execute((allNotesInRow: steps, values: [1, 0]));
		this.assertEquals(steps[1].value, 1);
		this.assertEquals(steps[2].value, 0);
		this.assertEquals(steps[0].value, 1);
	}

	test_execute_wrapsValuesOverDistinctStartTimes {
		var cmd, steps;
		cmd = SwitchCommand.new;
		steps = [
			this.prStep(0),
			this.prStep(0.25),
			this.prStep(0.5),
			this.prStep(0.75),
			this.prStep(1)
		];
		cmd.execute((allNotesInRow: steps, values: [1, 0, 0, 0]));
		this.assertEquals(steps[0].value, 1);
		this.assertEquals(steps[1].value, 0);
		this.assertEquals(steps[2].value, 0);
		this.assertEquals(steps[3].value, 0);
		this.assertEquals(steps[4].value, 1);
	}

	test_name_isSwitch {
		var cmd = SwitchCommand.new;
		this.assertEquals(cmd.name, "switch");
	}

	test_valuesParameter_parsesZerosAndOnes {
		var cmd = SwitchCommand.new;
		var param = cmd.getParameter("values");
		this.assert(param.notNil, "values parameter should exist", true);
		this.assertEquals(param.type, Integer);
		this.assertEquals(param.isArray, true);
		this.assertEquals(param.isValid("1 0 0 0"), true);
		this.assertEquals(param.isValid("0"), true);
		this.assertEquals(param.isValid("1"), true);
		this.assertEquals(param.isValid(""), false);
		this.assertEquals(param.isValid("2"), false);
		this.assertEquals(param.isValid("true"), false);
		this.assertEquals(param.isValid("1.5"), false);
	}
}
