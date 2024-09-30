\version "2.20.0"
\language "english"

\header {
  title = "Final peace"
  subtitle = "A"
}

\markup "Hydrasynth D080"

\new GrandStaff <<
  \new Staff \with { instrumentName = "Hydrasynth" } \relative c' {
    \key a \major
    d2 fs | % 1
    gs e | % 2
    g g4 fs | % 3
    e1 | % 4
    fs2 gs | % 5
    b as | % 6
    gs e | % 7
    d1 | % 8
    
  }
  \new Staff \with { instrumentName = "Hydrasynth" } \relative {
    \key a \major
    \clef bass
    fs2 d | % 1
    e cs | % 2
    d4 cs d a | % 3
    cs1 | % 4
    ds2 e | % 5
    gs d | % 6
    e cs | % 7
    fs1 | % 8
  }
>>