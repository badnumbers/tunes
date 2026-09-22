\version "2.20.0"
\language "english"

\header {
  title = "20260915"
  subtitle = "E♭ major / F dorian"
}

\markup "SH-01A, 'Mournful flute lead' or one of the other nice leads, reverb"
\markup "Other part sounds fab with Hydrasynth H109 'Enjoyment BN' a couple of octaves down"

\new GrandStaff <<
  \new Staff \with { instrumentName = "SH-01A" } \relative c' {
    \time 4/4
    \key ef \major
    f8 f'4 c bf af8~ | % 1
    af8 bf c g8~ g2 | % 2
    ef8 af4 bf c f,8~ | % 3
    f8 bf c d4 ef8 d bf | \break % 4
    af8 f'4 ef d bf8 | % 5
    c8 f,4 g af bf8 | % 6
    df8 f,4 bf ^"or G" af bf8 | % 7
    c1 | % 8
  }
   \new Staff \with { instrumentName = "???" } \relative c' {
    \time 4/4
    \key ef \major
    \clef bass
    af1 f c' df c af bf f
  }
>>