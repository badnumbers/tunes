MelodyAlgorithm {
	*tonic { | chords, rhythm |
		var melody = Array(10);
		var currentNote;
		var currentChordDuration;
		var currentNoteStartBeat = 0;
		var currentNoteEndBeat = 0;
		var currentChord = 0;
		var currentChordPositionInBeats = 0;

		if (chords.isNil,
			{
				Error("The chords parameter passed to MelodyAlgorithm.tonic() cannot be nil.").throw;
			},
			{
				if (chords.isArray,
					{
						if (chords.size != 0,
							{
								if (chords.any({|chord|chord.class != Event}),
									{
										Error(format("The chords parameter passed to MelodyAlgorithm.tonic() must be an Array of Events. The value % was received.", chords)).throw;
									},
									{
										if (chords.every({|chord|chord.keys.includes('tonic')}),
											{
												if (chords.any({|chord|chord.tonic.isNil}),
													{
														Error(format("The chords parameter passed to MelodyAlgorithm.tonic() must be an Array of Events containing a key called 'tonic' which is not nil. The value % was received.", chords)).throw;
													},
													{
														if (chords.every({|chord|chord.tonic.isInteger}),
															{
																if (chords.every({|chord|chord.tonic >= 0}),
																	{
																		if (chords.every({|chord|chord.tonic <= 6}),
																			{

																			},
																			{
																				Error(format("The chords parameter passed to MelodyAlgorithm.tonic() must be an Array of Events containing a key called 'tonic' which is less than or equal to 6. The value % was received.", chords)).throw;
																			}
																		);
																	},
																	{
																		Error(format("The chords parameter passed to MelodyAlgorithm.tonic() must be an Array of Events containing a key called 'tonic' which is greater than or equal to 0. The value % was received.", chords)).throw;
																	}
																);
															},
															{
																Error(format("The chords parameter passed to MelodyAlgorithm.tonic() must be an Array of Events containing a key called 'tonic' which is an integer. The value % was received.", chords)).throw;
															}
														);
													}
												);
											},
											{
												Error(format("The chords parameter passed to MelodyAlgorithm.tonic() must be an Array of Events containing a key called 'tonic'. The value % was received.", chords)).throw;
											}
										);
										if (chords.every({|chord|chord.keys.includes('triad')}),
											{
												if (chords.any({|chord|chord.triad.isNil}),
													{
														Error(format("The chords parameter passed to MelodyAlgorithm.tonic() must be an Array of Events containing a key called 'triad' which is not nil. The value % was received.", chords)).throw;
													},
													{
														if (chords.every({|chord|chord.triad.isArray}),
															{
																if (chords.every({|chord|chord.triad.size == 3}),
																	{
																		if (chords.every({|chord|chord.triad.every({|note|note.isInteger})}),
																			{
																				if (chords.every({|chord|chord.triad.every({|note|note >= 0})}),
																					{
																						if (chords.every({|chord|chord.triad.every({|note|note <= 6})}),
																							{

																							},
																							{
																								Error(format("The chords parameter passed to MelodyAlgorithm.tonic() must be an Array of Events containing a key called 'triad' which is an array of 3 integers less than or equal to 6. The value % was received.", chords)).throw;
																							}
																						);
																					},
																					{
																						Error(format("The chords parameter passed to MelodyAlgorithm.tonic() must be an Array of Events containing a key called 'triad' which is an array of 3 integers greater than or equal to 0. The value % was received.", chords)).throw;
																					}
																				);
																			},
																			{
																				Error(format("The chords parameter passed to MelodyAlgorithm.tonic() must be an Array of Events containing a key called 'triad' which is an array of 3 integers. The value % was received.", chords)).throw;
																			}
																		);
																	},
																	{
																		Error(format("The chords parameter passed to MelodyAlgorithm.tonic() must be an Array of Events containing a key called 'triad' which is an array of 3 integers. The value % was received.", chords)).throw;
																	}
																);
															},
															{
																Error(format("The chords parameter passed to MelodyAlgorithm.tonic() must be an Array of Events containing a key called 'triad' which is an array. The value % was received.", chords)).throw;
															}
														);
													}
												);
											},
											{
												Error(format("The chords parameter passed to MelodyAlgorithm.tonic() must be an Array of Events containing a key called 'triad'. The value % was received.", chords)).throw;
											}
										);
									}
								);
							},
							{
								Error("The chords parameter passed to MelodyAlgorithm.tonic() must be an Array of size greater than 0.").throw;
							}
						);
					},
					{
						Error("The chords parameter passed to MelodyAlgorithm.tonic() must be an Array.").throw;
					}
				);
			}
		);
		if (rhythm.isNil,
			{
				Error("The rhythm parameter passed to MelodyAlgorithm.tonic() cannot be nil.").throw;
			},
			{
				if (rhythm.class == Event,
					{
						if (rhythm.keys.includes('amp'),
							{
								if (rhythm.amp.isArray,
									{
										if (rhythm.amp.size != 0,
											{
												if (rhythm.amp.every({|val|val.isNumber}),
													{
														if (rhythm.amp.every({|val|val >= 0}),
															{

															},
															{
																Error(format("The rhythm parameter passed to MelodyAlgorithm.tonic() must be an Event containing a key called 'amp' which is an Array of numbers greater than or equal to 0. The value % was received.", rhythm)).throw;
															}
														);
													},
													{
														Error(format("The rhythm parameter passed to MelodyAlgorithm.tonic() must be an Event containing a key called 'amp' which is an Array of numbers. The value % was received.", rhythm)).throw;
													}
												);
											},
											{
												Error(format("The rhythm parameter passed to MelodyAlgorithm.tonic() must be an Event containing a key called 'amp' which is an Array with a size greater than 0. The value % was received.", rhythm)).throw;
											}
										);
									},
									{
										Error(format("The rhythm parameter passed to MelodyAlgorithm.tonic() must be an Event containing a key called 'amp' which is an Array. The value % was received.", rhythm)).throw;
									}
								);
							},
							{
								Error(format("The rhythm parameter passed to MelodyAlgorithm.tonic() must be an Event containing a key called 'amp'. The value % was received.", rhythm)).throw;
							}
						);
						if (rhythm.keys.includes('dur'),
							{
								if (rhythm.dur.isArray,
									{
										if (rhythm.dur.size != 0,
											{
												if (rhythm.dur.every({|val|val.isNumber}),
													{
														if (rhythm.dur.every({|val|val > 0}),
															{

															},
															{
																Error(format("The rhythm parameter passed to MelodyAlgorithm.tonic() must be an Event containing a key called 'dur' which is an Array of numbers greater than 0. The value % was received.", rhythm)).throw;
															}
														);
													},
													{
														Error(format("The rhythm parameter passed to MelodyAlgorithm.tonic() must be an Event containing a key called 'dur' which is an Array of numbers. The value % was received.", rhythm)).throw;
													}
												);
											},
											{
												Error(format("The rhythm parameter passed to MelodyAlgorithm.tonic() must be an Event containing a key called 'dur' which is an Array with a size greater than 0. The value % was received.", rhythm)).throw;
											}
										);
									},
									{
										Error(format("The rhythm parameter passed to MelodyAlgorithm.tonic() must be an Event containing a key called 'dur' which is an Array. The value % was received.", rhythm)).throw;
									}
								);
							},
							{
								Error(format("The rhythm parameter passed to MelodyAlgorithm.tonic() must be an Event containing a key called 'dur'. The value % was received.", rhythm)).throw;
							}
						);
						if (rhythm.keys.includes('legato'),
							{
								if (rhythm.legato.isArray,
									{
										if (rhythm.legato.size != 0,
											{
												if (rhythm.legato.every({|val|val.isNumber}),
													{
														if (rhythm.legato.every({|val|val > 0}),
															{

															},
															{
																Error(format("The rhythm parameter passed to MelodyAlgorithm.tonic() must be an Event containing a key called 'legato' which is an Array of numbers greater than 0. The value % was received.", rhythm)).throw;
															}
														);
													},
													{
														Error(format("The rhythm parameter passed to MelodyAlgorithm.tonic() must be an Event containing a key called 'legato' which is an Array of numbers. The value % was received.", rhythm)).throw;
													}
												);
											},
											{
												Error(format("The rhythm parameter passed to MelodyAlgorithm.tonic() must be an Event containing a key called 'legato' which is an Array with a size greater than 0. The value % was received.", rhythm)).throw;
											}
										);
									},
									{
										Error(format("The rhythm parameter passed to MelodyAlgorithm.tonic() must be an Event containing a key called 'legato' which is an Array. The value % was received.", rhythm)).throw;
									}
								);
							},
							{
								Error(format("The rhythm parameter passed to MelodyAlgorithm.tonic() must be an Event containing a key called 'legato'. The value % was received.", rhythm)).throw;
							}
						);
						if (rhythm.keys.includes('amp') && rhythm.keys.includes('dur') && rhythm.keys.includes('legato')
							&& rhythm.amp.isArray && rhythm.dur.isArray && rhythm.legato.isArray,
							{
								if ((rhythm.amp.size == rhythm.dur.size) && (rhythm.dur.size == rhythm.legato.size),
									{},
									{
										Error(format("The rhythm parameter passed to MelodyAlgorithm.tonic() must be an Event containing keys called 'amp', 'dur' and 'legato' which are Arrays of the same length. The value % was received.", rhythm)).throw;
									}
								);
							},
							{

							}
						);
					},
					{
						Error("The rhythm parameter passed to MelodyAlgorithm.tonic() must be an Event.").throw;
					}
				);
			}
		);

		currentChordDuration = 4;
		currentChordPositionInBeats = 0;
		rhythm.dur.do({
			|currentNoteDuration|
			postln(format("currentNoteEndBeat (start): %", currentNoteEndBeat));
			currentNoteEndBeat = currentNoteEndBeat + currentNoteDuration;
			postln(format("currentNoteEndBeat (end): %", currentNoteEndBeat));
			postln(format("currentChordPositionInBeats + currentChordDuration: %", currentChordPositionInBeats + currentChordDuration));
			if (currentNoteEndBeat > (currentChordPositionInBeats + currentChordDuration),
				{
					currentChordPositionInBeats = currentChordPositionInBeats + currentChordDuration;
					currentChord = currentChord + 1; // Not quite right, because the length of the current note might actually mean we should skip a chord
				}
			);
			currentNote = chords.wrapAt(currentChord).tonic;
			melody = melody.add(currentNote);
			postln(format("note written: %", currentNote));
		});
		^melody;
	}
}