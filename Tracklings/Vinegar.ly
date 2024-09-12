\version "2.20.0"
\language "english"

\header {
  title = "Vinegar"
}

\markup "JX-03"
\markup "Detuned sawtooths, negative filter envelope, nostalgic pitch wobble"

\new GrandStaff <<
  \new Staff \with { instrumentName = "JX-03" } \relative c' {
    \key d \minor
   <bf a'>4. <a~ g'~>8 <a~ g'~>2 | % 1
   <a g'>1 | % 2
   <bf a'>4. <a~ g'~>8 <a g'>2 | % 3
   <bf a'>4. <g~ f'~>8 <g f'>2 | % 4
  }
  \new Staff \with { instrumentName = "JX-03" } \relative c, {
    \key d \minor
    \clef bass
   d4. | % 1
  }
>>