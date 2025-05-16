(
~prColours = Dictionary.newFrom([
	\colour1, Color.fromHexString("6b4e71"),
	\colour2, Color.fromHexString("3a4454"),
	\colour3, Color.fromHexString("53687e"),
	\colour4, Color.fromHexString("c2b2b4"),
	\colour5, Color.fromHexString("f5dddd"),
	\extreme1, Color.black,
	\extreme2, Color.white,
]);

~prWindow = Window("Sequencer").background_(~prColours[\colour1]).front;
~prWindow.layout = VLayout(
	~prMainHeader = BorderView().background_(~prColours[\colour2]).minHeight_(100).maxHeight_(100).borderWidth_(0),
	HLayout(
		BorderView().background_(~prColours[\colour2]).minSize_(200@200).maxWidth_(200).borderWidth_(0).layout_(VLayout(
			~prLeftPanelHeader = BorderView().background_(~prColours[\colour3]).minHeight_(100).maxHeight_(100).borderWidth_(0),
			ScrollView().canvas_(~prLeftPanelBody = View().background_(~prColours[\colour4]))
		)),
		BorderView().background_(~prColours[\colour2]).minSize_(200@200).maxWidth_(200).borderWidth_(0).layout_(VLayout(
			~prMiddlePanelHeader = BorderView().background_(~prColours[\colour3]).minHeight_(100).maxHeight_(100).borderWidth_(0),
			ScrollView().canvas_(~prMiddlePanelBody = View().background_(~prColours[\colour4])),
		)),
		BorderView().background_(~prColours[\colour2]).minSize_(200@200).borderWidth_(0).layout_(VLayout(
			~prRightPanelHeader = BorderView().background_(~prColours[\colour3]).minHeight_(100).maxHeight_(100).borderWidth_(0),
			ScrollView().canvas_(~prRightPanelBody = View().background_(~prColours[\colour4]))
		))
)).margins_(30).spacing_(20);

StaticText(~prMainHeader, Rect(30, 30, 200, 40)).string_("Sequencer").stringColor_(~prColours[\colour5]).font_(Font(size:32));
StaticText(~prLeftPanelHeader, Rect(30, 30, 200, 40)).string_("Sections").stringColor_(~prColours[\extreme2]).font_(Font(size:24));
StaticText(~prMiddlePanelHeader, Rect(30, 30, 200, 40)).string_("Parts").stringColor_(~prColours[\extreme2]).font_(Font(size:24));
StaticText(~prRightPanelHeader, Rect(30, 30, 200, 40)).string_("Sequences").stringColor_(~prColours[\extreme2]).font_(Font(size:24));
)

(
~prLeftPanelBody.removeAll;
~prLeftPanelBody.layout = VLayout();
[\intro,\supercalifragilisticexpialidocious,\hatsfishdogshorses,\blublublahblahblublublahblah].do({
	|sectionName|
	var text = StaticText().string_(sectionName).stringColor_(~prColours[\extreme1]).maxHeight_(25);
	text.mouseEnterAction = {
		text.background_(~prColours[\extreme2]);
	};
	text.mouseLeaveAction = {
		text.background_(Color.clear);
	};
	text.mouseUpAction = {
		postln(format("Clicked %", sectionName));
	};
	~prLeftPanelBody.layout.add(text);
});
~prLeftPanelBody.layout.add(nil, 1);
)

(
~prMiddlePanelBody.removeAll;
~prMiddlePanelBody.layout = VLayout();
[\unodrum,\unosynth].do({
	|partName|
	var text = StaticText().string_(partName).stringColor_(~prColours[\extreme1]).maxHeight_(25);
	text.mouseEnterAction = {
		text.background_(~prColours[\extreme2]);
	};
	text.mouseLeaveAction = {
		text.background_(Color.clear);
	};
	text.mouseUpAction = {
		postln(format("Clicked %", partName));
	};
	~prMiddlePanelBody.layout.add(text);
});
~prMiddlePanelBody.layout.add(nil, 1);
)

(
~renderPseq = {
	|parentLayout,index|
	var prColours = ~prColours;
	var newLayout;
	parentLayout.insert(BorderView().background_(prColours[\colour3]).borderColor_(prColours[\extreme1]).layout_(
		VLayout(
			~renderPseqHeader.value(),
			View().layout_(newLayout = HLayout([nil, s: 1])),
			[nil, s: 1]
		)
	),index);
	newLayout;
};
~renderPseqHeader = {
	var prColours = ~prColours;
	StaticText().string_("Pseq").stringColor_(prColours[\extreme1]).minHeight_(25).mouseUpAction_({
		postln("Clicked on Pseq");
	})
};
~renderPpar = {
	|parentLayout,index|
	var prColours = ~prColours;
	var newLayout;
	parentLayout.insert(BorderView().background_(prColours[\colour2]).borderColor_(prColours[\extreme1]).layout_(
		VLayout(
			~renderPparHeader.value(),
			View().layout_(newLayout = VLayout([nil, s: 1])),
			[nil, s: 1]
		)
	),index);
	newLayout;
};
~renderPparHeader = {
	var prColours = ~prColours;
	StaticText().string_("Ppar").stringColor_(prColours[\extreme1]).minHeight_(25).mouseUpAction_({
		postln("Clicked on Ppar");
	})
};
~renderPbind = {
	|parentLayout,index|
	var prColours = ~prColours;
	var newLayout;
	parentLayout.insert(BorderView().background_(prColours[\colour1]).borderColor_(prColours[\extreme1]).layout_(
		VLayout(
			~renderPbindHeader.value(),
			View().layout_(newLayout = VLayout()),
			[nil, s: 1]
		)
	),index);
	~renderPbindPairsAsGrid.value(newLayout);
};
~renderPbindHeader = {
	var prColours = ~prColours;
	StaticText().string_("Pbind").stringColor_(prColours[\extreme1]).minHeight_(25).mouseUpAction_({
		postln("Clicked on Pbind");
	})
};
~renderPbindPairs = {
	|parentLayout|
	var prColours = ~prColours;
	var nameLayout, valueLayout, isEven = true;
	var pbind = Pbind(
		\degree, Pseq([1,2,3,4,5,6,7,8]),
		\amp, Pseq([0,1,0,1,0,1,0,1]),
		\dur, 0.25
	);
	parentLayout.add(View().layout_(nameLayout = VLayout()));
	parentLayout.add(View().layout_(valueLayout = VLayout()));
	pbind.patternpairs.do({
		|item|
		if (isEven, {
			nameLayout.add(StaticText().string_(item.asString).stringColor_(prColours[\extreme1]).minHeight_(25));
		}, {
			valueLayout.add(StaticText().string_(item.asString).stringColor_(prColours[\extreme1]).minHeight_(25));
		});
		isEven = isEven.not; // invert the value
	});
	nameLayout.add(nil, 1);
	valueLayout.add(nil, 1);
};
~renderPbindPairsAsGrid = {
	|parentLayout|
	var prColours = ~prColours;
	var pbind = Pbind(
		\degree, Pseq([1,2,3,4,5,6,7,8]),
		\amp, Pseq([0,1,0,1,0,1,0,1]),
		\dur, 0.25
	);
	var gridLayout,numRows=0,numColumns=0, isEven = true;
	parentLayout.add(View().layout_(gridLayout = GridLayout()));
	pbind.patternpairs.do({
		|item,index|
		if (isEven, {
			warn(format("index/2:%",(index/2).asInteger));
			gridLayout.add(StaticText().string_(item.asString),(index/2).asInteger,0);
		}, {
			gridLayout.add(TextField().value_(item.asString),(index/2).asInteger,1);
		});
		isEven = isEven.not; // invert the value
	});
};
)

(
// Add patterns based on Pseq
var containerLayout, pseqLayout, pparLayout1, pparLayout2, pbindLayout1, pbindLayout2, pbindLayout3, pbindLayout4;
var prColours = ~prColours;
var childIndex = 0;

// Container
~prRightPanelBody.removeAll;
~prRightPanelBody.layout_(HLayout(View().layout_(containerLayout = VLayout([nil, s: 1])),[nil, s: 1]));
// Pseq
pseqLayout = ~renderPseq.value(containerLayout,0);
pparLayout1 = ~renderPpar.value(pseqLayout,0);
pparLayout2 = ~renderPpar.value(pseqLayout,1);
(2..31).do({
	|index|
	~renderPpar.value(pseqLayout,index);
});
pbindLayout1 = ~renderPbind.value(pparLayout1,0);
pbindLayout2 = ~renderPbind.value(pparLayout1,1);
pbindLayout3 = ~renderPbind.value(pparLayout2,0);
pbindLayout4 = ~renderPbind.value(pparLayout2,1);
)