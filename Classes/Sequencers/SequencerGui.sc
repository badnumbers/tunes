SequencerGui {
	var prDocument;
	var prMainHeader;
	var prLeftPanelBody;
	var prLeftPanelHeader;
	var prMiddlePanelBody;
	var prMiddlePanelHeader;
	var prRightPanelBody;
	var prRightPanelHeader;
	var prSequencer;
	var prSequencerData;
	var prWindow;
	var prColours;

	init {
		|sequencer,privateSequencerData|
		var window, red, green, blue, scrollview;
		Validator.validateMethodParameterType(sequencer, Sequencer, "sequencer", "SequencerGui", "init");
		prSequencer = sequencer;
		prSequencerData = privateSequencerData;

		prDocument = Document.open(thisProcess.nowExecutingPath);

		prColours = Dictionary.newFrom([
			\colour1, Color.fromHexString("6b4e71"),
			\colour2, Color.fromHexString("3a4454"),
			\colour3, Color.fromHexString("53687e"),
			\colour4, Color.fromHexString("c2b2b4"),
			\colour5, Color.fromHexString("f5dddd"),
			\extreme1, Color.black,
			\extreme2, Color.white,
		]);

		prWindow = Window("Sequencer").background_(prColours[\colour1]).front;
		prWindow.layout = VLayout(
			prMainHeader = BorderView().background_(prColours[\colour2]).minHeight_(100).maxHeight_(100).borderWidth_(0),
			HLayout(
				BorderView().background_(prColours[\colour2]).minSize_(200@200).maxWidth_(200).borderWidth_(0).layout_(VLayout(
					prLeftPanelHeader = BorderView().background_(prColours[\colour3]).minHeight_(100).maxHeight_(100).borderWidth_(0),
					ScrollView().canvas_(prLeftPanelBody = View().background_(prColours[\colour4]))
				)),
				BorderView().background_(prColours[\colour2]).minSize_(200@200).maxWidth_(200).borderWidth_(0).layout_(VLayout(
					prMiddlePanelHeader = BorderView().background_(prColours[\colour3]).minHeight_(100).maxHeight_(100).borderWidth_(0),
					ScrollView().canvas_(prMiddlePanelBody = View().background_(prColours[\colour4])),
				)),
				BorderView().background_(prColours[\colour2]).minSize_(200@200).borderWidth_(0).layout_(VLayout(
					prRightPanelHeader = BorderView().background_(prColours[\colour3]).minHeight_(100).maxHeight_(100).borderWidth_(0),
					ScrollView().canvas_(prRightPanelBody = View().background_(prColours[\colour4]))
				))
		)).margins_(30).spacing_(20);

		StaticText(prMainHeader, Rect(30, 30, 200, 40)).string_("Sequencer").stringColor_(prColours[\colour5]).font_(Font(size:32));
		StaticText(prLeftPanelHeader, Rect(30, 30, 200, 40)).string_("Sections").stringColor_(prColours[\extreme2]).font_(Font(size:24));
		StaticText(prMiddlePanelHeader, Rect(30, 30, 200, 40)).string_("Parts").stringColor_(prColours[\extreme2]).font_(Font(size:24));
		StaticText(prRightPanelHeader, Rect(30, 30, 200, 40)).string_("Sequences").stringColor_(prColours[\extreme2]).font_(Font(size:24));

		this.prLoadSections();
	}

	*new {
		|sequencer,privateSequencerData|
		Validator.validateMethodParameterType(sequencer, Sequencer, "sequencer", "SequencerGui", "new");
		^super.new.init(sequencer,privateSequencerData);
	}

	prDisplayPattern {
		|parentView,pattern|
		var newView = View().minHeight_(25).minWidth_(25);
		switch (pattern.class,
			Pseq,   {
				parentView.layout.add(BorderView().background_(prColours[\colour3]).borderColor_(prColours[\extreme1]).layout_(
					VLayout(
						StaticText().string_("Pseq").stringColor_(prColours[\extreme1]).minHeight_(25),
						newView
					)
				));
				newView.layout_(HLayout());
			},
			Ppar,   {
				parentView.layout.add(BorderView().background_(prColours[\colour2]).borderColor_(prColours[\extreme1]).layout_(
					VLayout(
						StaticText().string_("Ppar").stringColor_(prColours[\extreme1]).minHeight_(25),
						newView
					)
				));
				newView.layout_(VLayout());
			},
			Pbind, {
				parentView.layout.add(BorderView().background_(prColours[\colour1]).borderColor_(prColours[\extreme1]).layout_(
					VLayout(
						StaticText().string_("Pbind").stringColor_(prColours[\extreme1]).minHeight_(25),
						newView
					)
				));
				newView.layout_(HLayout());
				this.prDisplayPbind(newView, pattern);
			},
			{
				Error(format("I don't know how to render a display for the pattern type %.", pattern.class)).throw;
			}
		);
		^newView;
	}

	prDisplayPbind {
		|parentView,pbind|
		var nameView, valueView, isEven = true;
		parentView.layout.add(nameView = View().layout_(VLayout()));
		parentView.layout.add(valueView = View().layout_(VLayout()));
		pbind.patternpairs.do({
			|item|
			if (isEven, {
				nameView.layout.add(StaticText().string_(item.asString).stringColor_(prColours[\extreme1]).minHeight_(25));
			}, {
				valueView.layout.add(StaticText().string_(item.asString).stringColor_(prColours[\extreme1]).minHeight_(25));
			});
			isEven = isEven.not; // invert the value
		});
		nameView.layout.add(nil, 1);
		valueView.layout.add(nil, 1);
	}

	prLoadParts {
		|sectionName, section|
		prMiddlePanelBody.removeAll;
		prMiddlePanelBody.layout = VLayout();
		section.keys.do({
			|partName|
			var text = StaticText().string_(partName).stringColor_(prColours[\extreme1]).maxHeight_(25);
			text.mouseEnterAction = {
				text.background_(prColours[\extreme2]);
			};
			text.mouseLeaveAction = {
				text.background_(Color.clear);
			};
			text.mouseUpAction = {
				var regex = format("~seq.addMidiPart%( *%%, *%% *,", "\\", "\\\\", sectionName, "\\\\", partName);
				var location = prDocument.getText.findRegexp(regex);
				if (location.size == 0,{
					warn(format("Could not find the location in document %.", prDocument.path));
				},{
					if (location.size > 1,{
						warn(format("Found more than one location in document %: see lines %.", prDocument.path, location.collect(_[0])));
					},{
						prDocument.selectRange(location[0][0],location[0][1].size);
					});
				});
				this.prLoadSequences(section[partName][1]);
			};
			prMiddlePanelBody.layout.add(text);
		});
		prMiddlePanelBody.layout.add(nil, 1);
	}

	prLoadSections {
		prLeftPanelBody.removeAll;
		prLeftPanelBody.layout = VLayout();
		prSequencerData.sections.keys.do({
			|sectionName|
			var text = StaticText().string_(sectionName).stringColor_(prColours[\extreme1]).maxHeight_(25);
			text.mouseEnterAction = {
				text.background_(prColours[\extreme2]);
			};
			text.mouseLeaveAction = {
				text.background_(Color.clear);
			};
			text.mouseUpAction = {
				this.prLoadParts(sectionName, prSequencerData.sections[sectionName]);
			};
			prLeftPanelBody.layout.add(text);
		});
		prLeftPanelBody.layout.add(nil, 1);
	}

	prLoadSequences {
		|pattern|
		var traversePatternGraph = {
			|pattern,parentView|
			switch (pattern.class,
				Pseq,   {
					var newview = this.prDisplayPattern(parentView,pattern);
					pattern.list.do({|subpat|traversePatternGraph.value(subpat,newview)});
					newview.layout.add(nil, 1);
				},
				Ppar,   {
					var newview = this.prDisplayPattern(parentView,pattern);
					pattern.list.do({|subpat|traversePatternGraph.value(subpat,newview)});
					newview.layout.add(nil, 1);
				},
				Pbind, {
					var newview = this.prDisplayPattern(parentView,pattern);
					newview.layout.add(nil, 1);
				},
				{
					postln(format("Unexpectedly this was a %.", pattern.class));
				}
			);
		};
		prRightPanelBody.removeAll;
		prRightPanelBody.layout = VLayout();
		traversePatternGraph.value(pattern,prRightPanelBody);
		prRightPanelBody.layout.add(nil, 1);
	}
}