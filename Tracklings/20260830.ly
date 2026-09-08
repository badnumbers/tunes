\version "2.20.0"
\language "english"

\header {
  title = "20260830"
  subtitle = "B♭ minor"
}

\markup "REV2 U3 P5 'Ancient Sigh BN' with FX Bank chorus and reverb"

\new GrandStaff <<
  \new Staff \with { instrumentName = "REV2" } \relative c'' {
    \time 4/4
    \key bf \minor
    bf4 df4 f2 | % 1
    g4 af gf4. f8 | % 2
    f2. af4 | % 3
    bf2 c | % 4
    df2 f, | % 5
    bf2 f4 gf | % 6
    bf2 f4 gf | % 7
    ef2. gf4 | % 8
    af1 | % 9
  }
  \new Staff \with { instrumentName = "DX7" } \relative c' {
    \key bf \minor
    \clef bass
    gf4 ef bf2 | % 1
    c4 ef c af | % 2
    df4 c bf af | % 3
    gf4 gf' f df | % 4
    bf4 bf' af f | % 5
    g4 bf ef,2 | % 6
    gf4 bf df,2 | % 7
    gf4 af bf cf | % 8
    df1 | % 9
  }
>>