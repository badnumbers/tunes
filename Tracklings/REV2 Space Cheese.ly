\version "2.20.0"
\language "english"

\header {
  title = "REV2 Space Cheese"
  subtitle = "A♭"
}

\markup "REV2 is using F1 P47. The left split is an arpeggio and makes an 80s octave bass."
\markup "The volume of the left part should be decreased a bit, and the right part increased."

\new GrandStaff <<
  \new Staff \with { instrumentName = "REV2 right" } \relative {
    \key af \major
    \ottava #1 c'''1 | % 1
    df2 c | % 2
    g'1 | % 3
    f2 ef | % 4
    c1 | % 5
    df2 c | % 6
    g'1 | % 7
    f2 ef \break | % 8
    bf1 | % 9
    c2 af | % 10
    f1~ | % 11
    f | % 12
    bf1 | % 13
    c2 af | % 14
    af4. bf8~ bf2 | % 15
    af4 bf b2 | % 16
    c1 | % 17
  }
  \new Staff \with { instrumentName = "REV2 left" } \relative {
    \key af \major
    \clef bass 
    <af~ af'~>1 | % 1
    <af af'> | % 2
    <ef~ ef'~>1 | % 3
    <ef ef'> | % 4
    <af~ af'~>1 | % 5
    <af af'> | % 6
    <ef~ ef'~>1 | % 7
    <ef ef'> | % 8
    <f~ f'~> | % 9
    <f f'> | % 10
    <df~ df'~> | % 11
    <df df'> | % 12
    <f~ f'~> | % 13
    <f f'> | % 14
    <df df'> | % 15
    <e e'> | % 16
    <f f'> | % 17
  }
>>