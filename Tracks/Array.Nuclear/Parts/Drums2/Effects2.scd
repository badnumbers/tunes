var drums2EffectsSections = Array.newClear(~sections.size);

var drums2Effects = Pmono(\drums2Effects, \in, ~drums2EffectsBus, \out, 0, \dur, Pseq([w*4]), \operation, 0);

drums2EffectsSections[0] = Array.newClear(~sections[0].size);
drums2EffectsSections[1] = Array.newClear(~sections[1].size);

drums2EffectsSections[0][0] = nil;
drums2EffectsSections[0][1] = nil;
drums2EffectsSections[0][2] = nil;
drums2EffectsSections[0][3] = drums2Effects;
drums2EffectsSections[0][4] = nil;
drums2EffectsSections[0][5] = nil;
drums2EffectsSections[0][6] = nil;
drums2EffectsSections[0][7] = nil;
drums2EffectsSections[0][8] = nil;
drums2EffectsSections[0][9] = nil;
drums2EffectsSections[0][10] = nil;
drums2EffectsSections[0][11] = nil;

drums2EffectsSections[1][0] = nil;
drums2EffectsSections[1][1] = nil;

drums2EffectsSections;