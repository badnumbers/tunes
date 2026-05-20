\version "2.20.0"
\language "english"

\header {
  title = "2026015"
  subtitle = "B♭ minor"
}

\markup "DX7 FLUTE 1"
\markup "REV2 U2 P35 'Tinkle Lead BN' + mod wheel"

\new GrandStaff <<
  \new Staff \with { instrumentName = "REV2" } \relative c'''' {
    \time 4/4
    \key bf \minor
    \ottava 2
    bf8 f bf f bf f af ef | % 1
    f c f c f c df bf | % 2
    ef bf ef bf ef bf f' c | % 3
    gf' bf, gf' bf, gf' bf, af' c, | % 4
  }
  \new Staff \with { instrumentName = "DX7" } \relative c'' {
    \key bf \minor
    df8. ef16 df8 c bf4 cf | % 1
    af8. bf16 af8 g f2 | % 2
    gf4 f8 gf ef4 af | % 3
    bf8. af16 bf8 c df4 e | % 4
  }
>>

\markup "Strange chromatic alternative"

\new GrandStaff <<
  \new Staff \with { instrumentName = "REV2" } \relative c'''' {
    \time 4/4
    \key bf \minor
    \ottava 2
    bf8 f bf f bf f af ef | % 1
    f c f c f c df bf | % 2
    ef bf ef bf ef bf f' c | % 3
    gf' bf, gf' bf, gf' bf, af' c, | % 4
  }
  \new Staff \with { instrumentName = "DX7" } \relative c'' {
    \key bf \minor
    df2 c4 cf | % 1
    bf2 bff4 af | % 2
    gf2 af4 bff | % 3
    bf2 cf4 c | % 4
  }
>>

\markup "Sloond? On b♭ an octave down?"