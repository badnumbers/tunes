MelodyAlgorithm {
	*tonic { | chords, rhythm |
		if (chords.isNil,
			{
				Error("The chords parameter passed to MelodyAlgorithm.tonic() cannot be nil.").throw;
			},
			{
				if (chords.isArray,
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

			}
		);
	}
}