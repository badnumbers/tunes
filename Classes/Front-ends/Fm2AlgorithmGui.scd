(
var window = Window("Blah", Rect(0, 0, 1600, 1000));
var scalingFactor = 20;
var algorithms = [
	(
		connections: [2@1, 6@5, 5@4, 4@3],
		number: 1,
		operatorCoordinates: [0@3, 0@2, 1@3, 1@2, 1@1, 1@0],
		width: 2
	),
	(
		connections: [2@1, 4@3, 6@5],
		number: 5,
		operatorCoordinates: [0@3, 0@2, 1@3, 1@2, 2@3, 2@2],
		width: 3
	),
	(
		connections: [2@1, 4@3, 5@3, 6@5],
		number: 7,
		operatorCoordinates: [0@3, 0@2, 1@3, 1@2, 2@2, 2@1],
		width: 3
	),
	(
		connections: [4@3, 5@3, 6@3, 2@1],
		number: 12,
		operatorCoordinates: [3@3, 3@2, 1@3, 0@2, 1@2, 2@2],
		width: 4
	),
	(
		connections: [2@1, 3@1, 4@1, 5@4, 6@5],
		number: 18,
		operatorCoordinates: [1@3, 0@2, 1@2, 2@2, 2@1, 2@0],
		width: 3
	),
	(
		connections: [6@3, 6@4, 6@5],
		number: 24,
		operatorCoordinates: [0@3, 1@3, 2@3, 3@3, 4@3, 3@2],
		width: 5
	),
	(
		connections: [],
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
	var counter = 0;
	var algorithmView = UserView(window, Rect(leftPosition,0,algorithm.width * 10 * scalingFactor,scalingFactor * 4 * 10)).background_(Color.black);
	StaticText(algorithmView, Rect(scalingFactor * 2, scalingFactor * 2, scalingFactor * 10, scalingFactor * 5)).string_(algorithm.number).font_(Font().size_(scalingFactor * 5)).stringColor_(Color.white);
	algorithm.operatorCoordinates.do({
		|operator|
		var operatorColour, operatorView;
		operatorColour = Color.blue;
		if (operator.y == 3, { operatorColour = Color.red; });
		counter = counter + 1;
		operatorView = View(algorithmView, Rect((operator.x * (scalingFactor * 10)) + (scalingFactor * 2), (operator.y * (scalingFactor * 10)) + (scalingFactor * 2), (scalingFactor * 6), (scalingFactor * 6))).background_(operatorColour);
		StaticText(operatorView, Rect(scalingFactor * 2, 0, scalingFactor * 5, scalingFactor * 5)).string_(counter).font_(Font().size_(scalingFactor * 5));
	});
	algorithm.connections.do({
		|connection,index|
		connectionsDrawFunc[index] = {
			Pen.strokeColor_(Color.green);
			postln(format("Number %: go from % to %", algorithm.number, algorithm.operatorCoordinates[connection.x - 1], algorithm.operatorCoordinates[connection.y - 1]));
			Pen.moveTo(algorithm.operatorCoordinates[connection.x - 1]);
			Pen.lineTo(algorithm.operatorCoordinates[connection.y - 1]);
			Pen.stroke;
		};

	});
	algorithmView.drawFunc = {
		connectionsDrawFunc.do({|func|func.value();});
	};
	algorithmView.refresh;
	leftPosition = leftPosition + (algorithm.width * 10 * scalingFactor) + (5 * scalingFactor);
});
window.front;
)