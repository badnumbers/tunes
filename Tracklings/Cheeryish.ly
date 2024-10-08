\version "2.20.0"
\language "english"

\header {
  title = "Cheeryish"
  subtitle = "F♯ / G♭"
}

\markup "Hydrasynth: A101 VHS Dreams"
\markup "REV2: U1P1 Grand Pad BN (turn down the portamento) or possibly U1P34 Nermal Lead BN"
\markup "E♭ minor"

\new GrandStaff <<
  \new Staff \with { instrumentName = "Hydrasynth" } \relative c'''' {
    \key ef \minor
    \ottava 2
    r4 <gf bf>8 <ef af>4 <ef af>4 <gf bf>8~ | % 1 
    <gf bf>8 <gf bf>4 <ef gf>4 <ef gf>4 r8 | % 2
    <df f>4 <df f>8 <ef gf>4 <ef gf>4 <af f>8~ | % 3
    <af f>8 <af f>4 <gf bf>4 <gf bf>4 r8 | % 4
  }
  \new Staff \with { instrumentName = "Hydrasynth" } \relative c' {
    \key ef \minor
    df8 gf af bf8~ bf4 df,8 gf | % 1
    af bf4 df,
    8 gf bf af gf
  }
  \new Staff \with { instrumentName = "REV2" } \relative c'' {
    \key ef \minor
    \ottava 1
    <df-7 gf-3 bf-5>1~ | % 1 
    <df gf bf>1 | % 2
    <gf-3 cf-6 ef-1>1~ | % 3
    <gf cf ef>1 | % 4
    <ef-1 af-4 cf-6>1~ | % 5
    <ef af cf>1 | % 6
    <df-7~ f-2~ af-4>1 | % 7
    <df f bf-5>1 | % 8
  }
  \new Staff \with { instrumentName = "REV2" } \relative c' {
    \key ef \minor
    \clef bass
    \ottava 1
    <bf bf'-5>1 | % 1
    <ef, ef'-1>1 | % 2
    <af af'-4>1 | % 3
    <ef' ef'-1>1 | % 4
    <cf cf'-6>1 | % 5
    <bf bf'-5>1 | % 6
    <af af'-4>1 | % 7
    <f f'-2>1 | % 8
  }
  \new Staff \with { instrumentName = "Microvolt" } \relative c {
    \key ef \minor
    \clef bass
    gf8-3 af-4 bf-5 ef,-1~ ef4 r4 | % 1
    gf8-3 af-4 bf-5 ef4-1 df8-7 bf4-5 | % 2
    bf4-5 af8-4 bf4-5 cf8-6 gf4-3 | % 3
    r2 gf4-3 af-4 | % 4
    af2-4 r2 | % 5
    ef8-1 af-4 bf-5 ef4-1 gf8-3 ef4-1 | % 6
    df4-7 bf8-5 df4-7 ef8-1 af4-4 | % 7
    r1 | % 8
  }
>>

\markup "This sounds wonderful with the same bassline under it"
\markup "JP-08: Duran pluck"

\new GrandStaff <<
  \new Staff \with { instrumentName = "JP08" } \relative c'' {
    \key ef \minor
    \time 22/8
    bf8[ df ef] af,[ bf df] gf,[ af bf] ef,[ gf af] ef[ gf af] ef[ gf af] f[ gf bf df] |
  }
>>