\version "2.20.0"
\language "english"

\header {
  title = "20251211"
  subtitle = "E♭ major"
}

\markup "Mega FM 01"

\new GrandStaff <<
  \new Staff \with { instrumentName = "Mega FM" } \relative c' {
    \key ef \major
    \repeat volta 2  { 
      r8 ef f g af4. af8 | % 1, 3
      d,4 af'8 c,8~ c2 } | % 2, 4
    r8 ef f g af4. af8 | % 5
    d,4 af'8 c,4 af'8 b,4 | % 6
    bf2~ bf8 bf8 df f | % 7
    c'4. af8~ af2 | % 8 
  }
  \new Staff \with { instrumentName = "???" } \relative c' {
    \key ef \major
    \clef bass
  }
>>