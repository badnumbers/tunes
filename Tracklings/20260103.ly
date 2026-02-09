\version "2.20.0"
\language "english"

\header {
  title = "20260103     "
  subtitle = "D major?"
}

\markup "Hydrasynth E097 Archaic BN"

\new GrandStaff <<
  \new Staff \with { instrumentName = "Hydrasynth" } \relative c {
    \key d \major
    \ottava #-1
    fs4 f2. | % 1
    b2 bf | % 2
    gs4 g2. | % 3
    d'2            cs | % 4
    b1 | % 5
    d2 cs | % 6
    bf1 | % 7
    g | % 8
  }
  \new Staff \with { instrumentName = "Hydrasynth" } \relative c, {
    \key d \major
    \clef bass
    d1 | % 1
    g | % 2
    b | % 3
    e | % 4
    f2 e^"The passing notes are optional" | % 5
    b a | % 6
    gs fs | % 7
    e ef % 8    
  }
>>