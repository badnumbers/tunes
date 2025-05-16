\version "2.20.0"
\language "english"

\header {
  title = "20250319"
  subtitle = "E minor"
}

\markup "Hydrasynth G011 Lost Tape BN"

\new GrandStaff <<
  \new Staff \with { instrumentName = "REV2" } \relative c'' {
    \time 2/2
    \key e \minor
    g2. fs8 g | % 1
    b2 fs | % 2
    e1~ | % 3
    e1 | % 4
    g2. fs8 g | % 5
    b2 d | % 6
    e2. b4 | % 7
    c1 | % 8
    fs2 e | % 9
    b d | % 10
    e,1~ | % 11
    e1 | % 12
    fs'2 e | % 13
    b d | % 14
    e2. b4 | % 15
    c1 | % 16
  }
  \new Staff \with { instrumentName = "Hydrasynth" } \relative c, {
    \key e \minor
    \clef bass
    <e e'>1 | % 1
    <d d'>1 | % 2
    <c c'>1~ | % 3
    <c c'>1 | % 4
    <e e'>1 | % 5
    <d d'>1 | % 6
    <c c'>1~ | % 7
    <c c'>1 | % 8
    <e e'>1 | % 9
    <g g'>1 | % 10
    <c c'>1~ | % 11
    <c c'>1 | % 12
    <e, e'>1 | % 13
    <g g'>1 | % 14
    <c c'>1~ | % 15
    <c c'>1 | % 16
  }
>>