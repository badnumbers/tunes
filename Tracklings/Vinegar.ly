\version "2.20.0"
\language "english"

\header {
  title = "Vinegar"
}

\markup "JX-03"
\markup "Detuned sawtooths, negative filter envelope, nostalgic pitch wobble"
\markup "Chords look strange but fit perfectly under F, C/E then Bf/D"

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
   d4. f8~ f2~  | % 1
   f1 | % 2
   d4. e8~ e2 | % 3
   c4. bf'8~ bf2 | % 4
  }
>>

\markup "Just play this on JP-08 preset 'Detuned saw glory' and you get the idea"

\new GrandStaff <<
  \new Staff \with { instrumentName = "JP-08" } \relative c' {
   \key d \minor
   <bf a' f' c'>4. <a~ g'~ e'~ c'~>8 <a~ g'~ e'~ c'~>2 | % 1
   <a g' e' c'>1 | % 2
   <bf a' f' c'>4. <a~ g'~ e'~ c'~>8 <a g' e' c'>2 | % 3
   <bf a' f' c'>4. <g~ f'~ d'~ bf'~>8 <g f' d' bf'>2 | % 4
  }
>>