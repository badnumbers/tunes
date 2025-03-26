SequencerGui {
	var prSequencer;
	var prMainView;
	var prPartsBodyView;
	var prSectionWidths;
	var prSequencerData;
	var prSequencesBodyView;
	var prWindowHeight = 400;
	var prWindowWidth = 800;

	init {
		|sequencer,privateSequencerData|
		var window;
		Validator.validateMethodParameterType(sequencer, Sequencer, "sequencer", "SequencerGui", "init");

		prSequencer = sequencer;
		prSequencerData = privateSequencerData;
		window = Window("Sequencer", Rect(0, 0, prWindowWidth, prWindowHeight)).background_(Color.black).front;
		prMainView = View(window, Rect(25,25,prWindowWidth-50,prWindowHeight-50)).resize_(4);
		prSectionWidths = Dictionary.newFrom([\sections, 200, \parts, 200]);
		this.prAddSectionsSection();
		this.prAddPartsSection();
		this.prAddSequencesSection();
	}

	*new {
		|sequencer,privateSequencerData|
		Validator.validateMethodParameterType(sequencer, Sequencer, "sequencer", "SequencerGui", "new");
		^super.new.init(sequencer,privateSequencerData);
	}

	prAddMainViewSection {
		|bounds,resize|
		View(prMainView,Rect(bounds.left,bounds.top,bounds.width,bounds.height)).background_(Color.white).resize_(resize);
		View(prMainView,Rect(bounds.left+1,bounds.top+1,bounds.width-2,bounds.height-2)).background_(Color.black).resize_(resize);
		^View(prMainView,Rect(bounds.left+25,bounds.top+25,bounds.width-50,bounds.height-50));
	}

	prAddPartsSection {
		var view, resize = 5;
		view = this.prAddMainViewSection(Rect(prSectionWidths[\sections] + 25,0,prSectionWidths[\parts],prMainView.bounds.height),resize);
		StaticText(view,Rect(0,0,180,25)).string_("Parts").stringColor_(Color.white);
		prPartsBodyView = View(view,Rect(0,25,180,prMainView.bounds.height-25)).resize_(resize);
	}

	prAddSectionsSection {
		var view, bodyView, currentTop = 0, resize = 5;
		view = this.prAddMainViewSection(Rect(0,0,prSectionWidths[\sections],prMainView.bounds.height),resize);
		StaticText(view,Rect(0,0,180,25)).string_("Sections").stringColor_(Color.white);
		bodyView = View(view,Rect(0,25,180,prMainView.bounds.height-25)).resize_(resize);
		prSequencerData.sections.keys.do({
			|sectionName|
			var sectionView, text;
			sectionView = View(bodyView,Rect(0,currentTop,180,25));
			text = StaticText(sectionView,Rect(0,0,180,25)).string_(sectionName).stringColor_(Color.white);
			sectionView.mouseEnterAction = {
				sectionView.background_(Color.white);
				text.stringColor_(Color.black);
			};
			sectionView.mouseLeaveAction = {
				sectionView.background_(Color.black);
				text.stringColor_(Color.white);
			};
			sectionView.mouseUpAction = {
				this.prUpdatePartsList(prSequencerData.sections[sectionName]);
			};
			currentTop = currentTop + 25;
		});
	}

	prAddSequencesSection {
		var view, resize = 5;
		view = this.prAddMainViewSection(Rect(prSectionWidths[\sections] + 25 + prSectionWidths[\parts] + 25,0,prMainView.bounds.width-prSectionWidths[\sections]-25-prSectionWidths[\parts]-25,prMainView.bounds.height),resize);
		StaticText(view,Rect(0,0,180,25)).string_("Sequences").stringColor_(Color.white);
		prSequencesBodyView = View(view,Rect(0,25,380,prMainView.bounds.height-25)).resize_(resize);
	}

	prUpdatePartsList {
		|section|
		var currentTop = 0;
		section.keys.do({
			|partName|
			var partView, text;
			partView = View(prPartsBodyView,Rect(0,currentTop,180,25));
			text = StaticText(partView,Rect(0,0,180,25)).string_(partName).stringColor_(Color.white);
			partView.mouseEnterAction = {
				partView.background_(Color.white);
				text.stringColor_(Color.black);
			};
			partView.mouseLeaveAction = {
				partView.background_(Color.black);
				text.stringColor_(Color.white);
			};
			partView.mouseUpAction = {
				postln(format("User clicked on part %.", partName));
			};
			currentTop = currentTop + 25;
		});
	}
}