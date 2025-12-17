\version "2.20.0"
\language "english"

\header {
  title = "20251127"
  subtitle = "A major"
}

\markup "REV2 U1 P41 Cheezy Twinkz BN"

\new GrandStaff <<
  \new Staff \with { instrumentName = "REV2" } \relative c'' {
    \key a \major
    <cs fs gs cs>1~ | % 1
    <cs fs gs cs>~ | % 2
    <cs fs gs cs>~ | % 3
    <cs fs gs cs> | % 4
  }
  \new Staff \with { instrumentName = "REV2" } \relative c' {
    \key a \major
    \clef bass
    a1 | % 1
    gs | % 2
    e | % 3
    fs | % 4
  }
>>