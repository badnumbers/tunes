UnoDrum : Synthesizer {
	classvar <kick1NoteNumber = 36;
	classvar <kick2NoteNumber = 35;
	classvar <rimNoteNumber = 37;
	classvar <snareNoteNumber = 38;
	classvar <clapNoteNumber = 39;
	classvar <tom1NoteNumber = 41;
	classvar <closedHhNoteNumber = 42;
	classvar <tom2NoteNumber = 43;
	classvar <openHhNoteNumber = 46;
	classvar <cymbalNoteNumber = 49;
	classvar <rideNoteNumber = 51;
	classvar <cowbellNoteNumber = 56;

	init {
		|id|
		Validator.validateMethodParameterType(id, Symbol, "id", "UnoDrum", "init");
		super.init(id,nil,nil,nil);
	}
}