PatternStructureUnitTests : BNUnitTest {
	test_from_hidesSingleRepeatAndKeepsInf {
		var once, forever, sequencer;
		sequencer = Sequencer();
		once = PatternStructure.from(Pseq([Pbind(\dur, 1)], 1), sequencer);
		forever = PatternStructure.from(Pn(Pbind(\dur, 1), inf), sequencer);
		this.assertEquals(once.repeats, nil, "a repeat count of 1 is omitted");
		this.assertEquals(forever.repeats, inf, "an infinite repeat count is kept");
		this.assertEquals(forever.direction, \single, "Pn is a filter around one child");
		this.assertEquals(forever.label, "Pn", "the chip label is the class name");
	}

	test_from_pbindIsOpaque {
		var sequencer, structure;
		sequencer = Sequencer();
		structure = PatternStructure.from(Pbind(\midinote, sequencer.pat(\drums)), sequencer);
		this.assertEquals(structure.direction, \leaf, "a Pbind is a leaf");
		this.assertEquals(structure.label, "Pbind", "the leaf is labeled with the class name");
		this.assertEquals(structure.children.size, 0, "a Pbind's keys are not opened");
		this.assertEquals(structure.sequenceId, nil, "an anonymous pattern has no sequence id");
	}

	test_from_directionsRepeatsAndNamedLeaves {
		var chain, row, sequencer, structure;
		sequencer = Sequencer();
		structure = PatternStructure.from(Ppar([
			Pseq([sequencer.pat(\drums), 99, sequencer.pat(\bass)], 4),
			Pchain(Pbind(\dur, 1), Pbind(\dur, 2)),
			Ptpar([sequencer.pat(\hats)])
		]), sequencer);
		this.assertEquals(structure.direction, \vertical, "Ppar stacks its children");
		this.assertEquals(structure.label, "Ppar", "the chip label is the class name");
		row = structure.children[0];
		this.assertEquals(row.direction, \horizontal, "Pseq lays its list out in a row");
		this.assertEquals(row.repeats, 4, "a repeat count other than 1 is kept");
		this.assertEquals(row.children[0].sequenceId, \drums, "a SeqPat leaf stores the id");
		this.assertEquals(row.children[0].label, "drums", "a SeqPat leaf is labeled with the id");
		this.assertEquals(row.children[0].direction, \leaf, "a MIDI reference is not expanded");
		this.assertEquals(row.children[1].label, "99", "a non-pattern list item uses its string");
		this.assertEquals(row.children[2].sequenceId, \bass, "each named list item keeps its own id");
		chain = structure.children[1];
		this.assertEquals(chain.direction, \vertical, "Pchain stacks its patterns");
		this.assertEquals(chain.label, "Pchain", "Pchain is labeled with its class");
		this.assertEquals(chain.children.size, 2, "Pchain keeps each pattern");
		this.assertEquals(structure.children[2].direction, \vertical, "Ptpar stacks its children");
		this.assertEquals(structure.children[2].label, "Ptpar", "Ptpar keeps its own class name");
	}

	test_patternStructure_expandsNestedCompositionAndCutsCycles {
		var inner, leaf, sequencer, structure;
		sequencer = Sequencer();
		sequencer.compose(\verse, Pseq([sequencer.pat(\a)]));
		sequencer.compose(\song, Ppar([sequencer.pat(\verse), sequencer.pat(\song)]));
		structure = sequencer.patternStructure(\song);
		this.assertEquals(structure.direction, \vertical, "the composition's pattern is the root");
		this.assertEquals(structure.children[1].direction, \leaf, "a reference to the composition being walked is a leaf");
		this.assertEquals(structure.children[1].sequenceId, \song, "the cycle leaf keeps the id");
		this.assertEquals(structure.children[1].children.size, 0, "the cycle is not walked again");
		inner = structure.children[0];
		this.assertEquals(inner.direction, \single, "a nested composition is a chip around its pattern");
		this.assertEquals(inner.label, "verse", "the nested chip is labeled with the id");
		this.assertEquals(inner.sequenceId, \verse, "the nested chip selects that composition");
		this.assertEquals(inner.children[0].label, "Pseq", "the nested composition's pattern is walked");
		leaf = inner.children[0].children[0];
		this.assertEquals(leaf.sequenceId, \a, "a named leaf inside the nested composition is kept");
		this.assertEquals(leaf.direction, \leaf, "a missing MIDI id stays a leaf");
	}
}
