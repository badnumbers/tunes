(
var window = Window("Blah", Rect(0, 0, 1900, 1000));
var scalingFactor = 2;
var algorithms = [
	(
		connections: [2@1, 6@5, 5@4, 4@3],
		feedback: [6],
		number: 1,
		operatorCoordinates: [0@3, 0@2, 1@3, 1@2, 1@1, 1@0],
		width: 2
	),
	(
		connections: [2@1, 6@5, 5@4, 4@3],
		feedback: [2],
		number: 2,
		operatorCoordinates: [0@3, 0@2, 1@3, 1@2, 1@1, 1@0],
		width: 2
	),
	(
		connections: [3@1, 2@1, 6@5, 5@4],
		feedback: [6],
		number: 3,
		operatorCoordinates: [0@3, 0@2, 0@1, 1@3, 1@2, 1@1],
		width: 2
	),
	(
		connections: [3@1, 2@1, 6@5, 5@4],
		feedback: [6,4],
		number: 4,
		operatorCoordinates: [0@3, 0@2, 0@1, 1@3, 1@2, 1@1],
		width: 2
	),
	(
		connections: [2@1, 4@3, 6@5],
		feedback: [6],
		number: 5,
		operatorCoordinates: [0@3, 0@2, 1@3, 1@2, 2@3, 2@2],
		width: 3
	),
	(
		connections: [2@1, 4@3, 6@5],
		feedback: [6,5],
		number: 6,
		operatorCoordinates: [0@3, 0@2, 1@3, 1@2, 2@3, 2@2],
		width: 3
	),
	(
		connections: [2@1, 4@3, 5@3, 6@5],
		feedback: [6],
		number: 7,
		operatorCoordinates: [0@3, 0@2, 1@3, 1@2, 2@2, 2@1],
		width: 3
	),
	(
		connections: [2@1, 4@3, 5@3, 6@5],
		feedback: [4],
		number: 8,
		operatorCoordinates: [0@3, 0@2, 1@3, 1@2, 2@2, 2@1],
		width: 3
	),
	(
		connections: [2@1, 4@3, 5@3, 6@5],
		feedback: [2],
		number: 9,
		operatorCoordinates: [0@3, 0@2, 1@3, 1@2, 2@2, 2@1],
		width: 3
	),
	(
		connections: [5@4, 6@4, 3@2, 2@1],
		feedback: [3],
		number: 10,
		operatorCoordinates: [2@3, 2@2, 2@1, 1@3, 0@2, 1@2],
		width: 3
	),
	(
		connections: [5@4, 6@4, 3@2, 2@1],
		feedback: [6],
		number: 11,
		operatorCoordinates: [2@3, 2@2, 2@1, 1@3, 0@2, 1@2],
		width: 3
	),
	(
		connections: [4@3, 5@3, 6@3, 2@1],
		feedback: [2],
		number: 12,
		operatorCoordinates: [3@3, 3@2, 1@3, 0@2, 1@2, 2@2],
		width: 4
	),
	(
		connections: [4@3, 5@3, 6@3, 2@1],
		feedback: [6],
		number: 13,
		operatorCoordinates: [3@3, 3@2, 1@3, 0@2, 1@2, 2@2],
		width: 4
	),
	(
		connections: [2@1, 5@4, 4@3, 6@4],
		feedback: [6],
		number: 14,
		operatorCoordinates: [0@3, 0@2, 1@3, 1@2, 1@1, 2@1],
		width: 3
	),
	(
		connections: [2@1, 5@4, 4@3, 6@4],
		feedback: [2],
		number: 15,
		operatorCoordinates: [0@3, 0@2, 1@3, 1@2, 1@1, 2@1],
		width: 3
	),
	(
		connections: [2@1, 4@3, 3@1, 6@5, 5@1],
		feedback: [6],
		number: 16,
		operatorCoordinates: [1@3, 0@2, 1@2, 1@1, 2@2, 2@1],
		width: 3
	),
	(
		connections: [2@1, 4@3, 3@1, 6@5, 5@1],
		feedback: [2],
		number: 17,
		operatorCoordinates: [1@3, 0@2, 1@2, 1@1, 2@2, 2@1],
		width: 3
	),
	(
		connections: [2@1, 3@1, 4@1, 5@4, 6@5],
		feedback: [3],
		number: 18,
		operatorCoordinates: [1@3, 0@2, 1@2, 2@2, 2@1, 2@0],
		width: 3
	),
	(
		connections: [3@2, 2@1, 6@4, 6@5],
		feedback: [6],
		number: 19,
		operatorCoordinates: [0@3, 0@2, 0@1, 1@3, 2@3, 1@2],
		width: 3
	),
	(
		connections: [3@1, 3@2, 5@4, 6@4],
		feedback: [3],
		number: 20,
		operatorCoordinates: [0@3, 1@3, 0@2, 3@3, 2@2, 3@2],
		width: 4
	),
	(
		connections: [3@1, 3@2, 6@4, 6@5],
		feedback: [3],
		number: 21,
		operatorCoordinates: [0@3, 1@3, 0@2, 2@3, 3@3, 2@2],
		width: 4
	),
	(
		connections: [2@1, 6@3, 6@4, 6@5],
		feedback: [6],
		number: 22,
		operatorCoordinates: [0@3, 0@2, 1@3, 2@3, 3@3, 2@2],
		width: 4
	),
	(
		connections: [3@2, 6@4, 6@5],
		feedback: [6],
		number: 23,
		operatorCoordinates: [0@3, 1@3, 1@2, 2@3, 3@3, 2@2],
		width: 4
	),
	(
		connections: [6@3, 6@4, 6@5],
		feedback: [6],
		number: 24,
		operatorCoordinates: [0@3, 1@3, 2@3, 3@3, 4@3, 3@2],
		width: 5
	),
	(
		connections: [6@4, 6@5],
		feedback: [6],
		number: 25,
		operatorCoordinates: [0@3, 1@3, 2@3, 3@3, 4@3, 3@2],
		width: 5
	),
	(
		connections: [3@2, 5@4, 6@4],
		feedback: [6],
		number: 26,
		operatorCoordinates: [0@3, 1@3, 1@2, 2@3, 2@2, 3@2],
		width: 4
	),
	(
		connections: [3@2, 5@4, 6@4],
		feedback: [3],
		number: 27,
		operatorCoordinates: [0@3, 1@3, 1@2, 2@3, 2@2, 3@2],
		width: 4
	),
	(
		connections: [2@1, 5@4, 4@3],
		feedback: [5],
		number: 28,
		operatorCoordinates: [0@3, 0@2, 1@3, 1@2, 1@1, 2@3],
		width: 3
	),
	(
		connections: [4@3, 6@5],
		feedback: [6],
		number: 29,
		operatorCoordinates: [0@3, 1@3, 2@3, 2@2, 3@3, 3@2],
		width: 4
	),
	(
		connections: [5@4, 4@3],
		feedback: [5],
		number: 30,
		operatorCoordinates: [0@3, 1@3, 2@3, 2@2, 2@1, 3@3],
		width: 4
	),
	(
		connections: [6@5],
		feedback: [6],
		number: 31,
		operatorCoordinates: [0@3, 1@3, 2@3, 3@3, 4@3, 4@2],
		width: 5
	),
	(
		connections: [],
		feedback: [6],
		number: 32,
		operatorCoordinates: [0@3, 1@3, 2@3, 3@3, 4@3, 5@3, 6@3],
		width: 6
	)
];

// CURRENTLY ONE ALGORITHM IS RENDERING ON TOP OF THE OTHER

var leftPosition = 0;
algorithms.do({
	|algorithm|
	var connectionsDrawFunc = Array.newClear(algorithm.connections.size);
	var feedbackDrawFunc;
	var counter = 0;
	var operatorColour = Color.white;
	var terminalOperatorColour = Color.white;
	var connectionColour = Color.white;
	var algorithmView = UserView(window, Rect(leftPosition,0,algorithm.width * 10 * scalingFactor,scalingFactor * 4 * 10)).background_(Color.black);
	StaticText(algorithmView, Rect(scalingFactor * 2, scalingFactor * 2, scalingFactor * 10, scalingFactor * 5)).string_(algorithm.number).font_(Font(size: scalingFactor * 6, bold: true)).stringColor_(Color.white);
	algorithm.operatorCoordinates.do({
		|operator|
		var opColour, operatorView;
		opColour = operatorColour;
		if (operator.y == 3, { opColour = terminalOperatorColour; });
		counter = counter + 1;
		operatorView = View(algorithmView, Rect((operator.x * (scalingFactor * 10)) + (scalingFactor * 2), (operator.y * (scalingFactor * 10)) + (scalingFactor * 2), (scalingFactor * 6), (scalingFactor * 6))).background_(opColour);
		StaticText(operatorView, Rect(scalingFactor * 1.5, 0, scalingFactor * 5, scalingFactor * 5)).string_(counter).font_(Font().size_(scalingFactor * 5));
	});
	algorithm.connections.do({
		|connection,index|
		connectionsDrawFunc[index] = {
			Pen.width = scalingFactor;
			Pen.strokeColor_(connectionColour);
			Pen.moveTo(algorithm.operatorCoordinates[connection.x - 1] * 10 * scalingFactor + (scalingFactor * 5));
			Pen.lineTo(algorithm.operatorCoordinates[connection.y - 1] * 10 * scalingFactor + (scalingFactor * 5));
			Pen.stroke;
		};

	});
	feedbackDrawFunc = {
		var feedbackHeight = algorithm.operatorCoordinates[algorithm.feedback.wrapAt(-1) - 1].y - algorithm.operatorCoordinates[algorithm.feedback.at(0) - 1].y + 1;
		var currentPoint = algorithm.operatorCoordinates[algorithm.feedback.at(0) - 1] * 10 * scalingFactor + (scalingFactor * 5);
		postln(format("Number %: feedback height is %", algorithm.number, feedbackHeight));
		Pen.width = scalingFactor;
		Pen.strokeColor_(connectionColour);
		Pen.moveTo(currentPoint);
		currentPoint = Point(currentPoint.x, currentPoint.y - (scalingFactor * 5));
		Pen.lineTo(currentPoint);
		currentPoint = Point(currentPoint.x + (scalingFactor * 5), currentPoint.y);
		Pen.lineTo(currentPoint);
		currentPoint = Point(currentPoint.x, currentPoint.y + (scalingFactor * 10 * feedbackHeight));
		Pen.lineTo(currentPoint);
		currentPoint = Point(currentPoint.x - (scalingFactor * 5), currentPoint.y);
		Pen.lineTo(currentPoint);
		currentPoint = Point(currentPoint.x, currentPoint.y - (scalingFactor * 5));
		Pen.lineTo(currentPoint);
		Pen.stroke;
	};
	algorithmView.drawFunc = {
		feedbackDrawFunc.value;
		connectionsDrawFunc.do({|func|func.value();});
	};
	algorithmView.refresh;
	leftPosition = leftPosition + (algorithm.width * 10 * scalingFactor) + (5 * scalingFactor);
});
window.front;
)

(
var moo = [0,1,2,3,4,5,6,7,8,9];
moo.wrapAt(0);
)