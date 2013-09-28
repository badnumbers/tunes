var drums2EffectsPart = Array.newClear(100);

var drums2Effects = Pmono(\drums2Effects, \in, ~drums2EffectsBus, \out, 0, \dur, Pseq([w*4]), \operation, 0);

drums2EffectsPart[0] = nil;
drums2EffectsPart[1] = nil;
drums2EffectsPart[2] = nil;
drums2EffectsPart[3] = drums2Effects;

drums2EffectsPart;