\version "2.20.0"
\language "english"

\header {
  title = "Calming"
}

\markup "It's just the same 5 note sequence in 3 octaves, but it's hard to spot that."
\markup "Used the TX7 with the preset CMB23 Not Subtle."
\markup "Lots of reverb from NHHall."
\markup "Played quite fast so it takes on rhythms of its own."

\new GrandStaff <<
  \new Staff \with { instrumentName = "DX7" } \relative {
    %\key a \major
    \time 15/16
    gf16[ b ef b' df]
    gf,[ b ef b' df]
    gf,[ b ef b' df]
  }
>>