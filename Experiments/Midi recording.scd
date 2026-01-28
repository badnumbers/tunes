Config.openConfigFile
FxBank()
TempoClock.tempo_(1.5)
Setup.midi
(
 ~seq = Sequencer2();
~seq.showGui;
)
Synths(\jx03).showGui

(
~childViews = List();
~addChildView = {
	|child|
	~childViews.add(Dictionary.newFrom([\view, child, \originalBounds, child.bounds]););
	/*child.mouseWheelAction_({
		|view, x, y, modifiers, xDelta, yDelta|
		postln("child mouse wheel action");
		child.parent.mouseWheelAction.value(view, x, y, modifiers, xDelta, yDelta);
	});*/
	child.mouseDownAction_({
		if (child.borderWidth == 0, {
			child.borderWidth_(2);
		}, {
			child.borderWidth_(0);
		});
	});
};
~resizeChildren = {
	|scale|
	~childViews.do({
		|child|
		child[\view].bounds = Rect(child[\originalBounds].left*scale,child[\originalBounds].top*scale,child[\originalBounds].width*scale,child[\originalBounds].height*scale);
	});
};

~window = Window("Zooming and scrolling", Rect(50,50,800,600));
~scrollview = ScrollView(~window, Rect(50, 50, ~originalWidth, ~originalHeight)).mouseWheelAction_({});
~scale = 1.0;
~minscale = 1.0;
~maxscale = 5.0;
~originalHeight = 500;
~originalWidth = 500;
~zoomme = DragBoth(~scrollview, Rect(0, 0, ~originalWidth*2, ~originalHeight*2)).background_(Color.black).beginDragAction_({|me,x,y|me.object=x@y}).receiveDragHandler_({
	|me,x,y|
	postln("Drag operation ended.");
}).canReceiveDragHandler_({
	|me,x,y|
	var left,top,width,height;
	postln([x,y]);
	if ((me.object.x) < x,{
		left = me.object.x;
		width = x - (me.object.x);
	},{
		left = x;
		width = (me.object.x) - x;
	});
	if ((me.object.y) < y,{
		top = me.object.y;
		height = y - (me.object.y);
	},{
		top = y;
		height = (me.object.y) - y;
	});
	~selectionView.bounds = Rect(left,top,width,height);
});
/*.mouseWheelAction_({
	|view, x, y, modifiers, xDelta, yDelta|
	//[view, x, y, modifiers, xDelta, yDelta].postln;
	~scale = ~scale * 1 + (yDelta*0.005);
	if (~scale < ~minscale, { ~scale = ~minscale; });
	if (~scale > ~maxscale, { ~scale = ~maxscale; });
	postln(format("scale: %", ~scale));
	~zoomme.resizeTo(~originalWidth * ~scale, ~originalHeight * ~scale);
	~resizeChildren.value(~scale);
}).mouseDownAction_({postln("I'm black!")});*/
~selectionView = BorderView(~zoomme,Rect(10,10,10,10)).background_(Color.clear).borderColour_(Color.magenta).borderWidth_(2).acceptsMouse_(false);
~lefts = ((1..10)*50).scramble;
~tops = ((1..10)*50).scramble;
~lefts.size.do({
	|index|
	~addChildView.value(BorderView(~zoomme,Rect(~lefts[index],~tops[index],20,20)).background_(Color.rand).borderColour_(Color.white));
});

/*~addChildView.value(BorderView(~zoomme,Rect(40,20,20,20)).background_(Color.blue).borderColour_(Color.white));
~addChildView.value(BorderView(~zoomme,Rect(70,40,20,20)).background_(Color.green).borderColour_(Color.white));
*/
~window.front;
)

Config.openConfigFile