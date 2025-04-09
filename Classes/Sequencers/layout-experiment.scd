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
[\intro].do({
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
// Ppar based
~prRightPanelBody.removeAll;
~prRightPanelBody.layout = VLayout();
~prRightPanelBody.layout.add(View().background_(~prColours[\colour1]).minHeight_(25));
~prRightPanelBody.layout.add(nil, 1);
)