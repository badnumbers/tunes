\version "2.20.0"
\language "english"

\header {
  title = "20251230"
  subtitle = "B major"
}

\markup "Hydrasynth H128 Chorus Pad TC"

\new GrandStaff <<
  \new Staff \with { instrumentName = "Hydrasynth" } \relative c'' {
    \key b \major
    <cs as'>4 <ds b'>2. | % 1
    <e cs'>2 <ds b'> | % 2
    <cs as'>4 <b fs'>2. | % 3
    <b gs'>2 <ds b'> | % 4
    <cs a'> <fs cs'> | % 5
  }
  \new Staff \with { instrumentName = "Hydrasynth" } \relative c {
    \key b \major
    \clef bass
    <as as'>4 <fs fs'>2. | % 1
    <gs gs'>2 <e e'>4 <gs gs'> | % 2
    <fs fs'>4 <ds ds'>2. | % 3
    <e e'>2 <ds ds'>4 <b b'> | % 4
    <d d'>4 <fs fs'> <a a'>2 | % 5
  }
>>