\version "2.20.0"
\language "english"

\header {
  title = "20260915"
  subtitle = "E♭ major / F dorian"
}

\markup "SH-01A, 'Mournful flute lead' or one of the other nice leads, reverb"

\new GrandStaff <<
  \new Staff \with { instrumentName = "REV2" } \relative c' {
    \time 4/4
    \key ef \major
    f8( f'4 c bf af8~ | % 1
    af8 bf c g8~ g2) | % 2
    ef8( af4 bf c) f,8~( | % 3
    f8 bf c d4 ef8 d bf) | % 4
  }
>>