\version "2.20.0"
\language "english"

\header {
  title = "20260429"
  subtitle = "B (F# mixolydian)"
}

\markup "REV2 Bank 2 Patch 24 Prophet AF BN - with reverb added"

\new GrandStaff <<
  \new Staff \with { instrumentName = "JX-03" } \relative c''' {
    \key b \major
    as4 cs, ds2 | % 1
    gs4 ds e b' | % 2
  }
  \new Staff \with { instrumentName = "JX-03" } \relative c, {
    \key b \major
    \clef bass
    fs2 b4 cs | % 1
    e4 fs a d, | % 2
  }
>>