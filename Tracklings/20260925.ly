\version "2.20.0"
\language "english"

\header {
  title = "20260925"
  subtitle = "G♭ major"
}

\markup "JX-03 20260925"
\markup "Just very nice indeed"

\new GrandStaff <<
  \new Staff \with { instrumentName = "JX-03" } \relative c'' {
    \time 4/4
    \key gf \major
    <af df gf>1 | % 1
    <gf bf f'>1 | % 2
    <ef af df>1 | % 3
    << { <f bf>1 } \\ { ef'2 df2 } >> | % 4
  }
   \new Staff \with { instrumentName = "JX-03" } \relative c' {
    \time 4/4
    \key gf \major
    \clef bass
    gf1 | % 1
    ef1 | % 2
    cf1 | % 3
    df | % 4
  }
>>

\new GrandStaff <<
  \new Staff \with { instrumentName = "JX-03" } \relative c'' {
    \time 4/4
    \key gf \major
    << af1~ \\  gf'1 >> | % 1
    << af,1 \\ f' >> | % 2
    << gf,1~ \\  df'1 >> | % 3
    << gf,1 \\ ef' >> | % 4
    << ef,1~ \\  cf'1 >> | % 5
    << ef,1 \\ df' >> | % 6
    <df, bf'>1 | % 7
    <df bf'>1 | % 8
  }
   \new Staff \with { instrumentName = "JX-03" } \relative c {
    \time 4/4
    \key gf \major
    \clef bass
    <cf af'>1^"These chords should be held across the bars" | % 1
    <cf af'>1 | % 2
    <bf af'>1 | % 3
    <bf af'>1 | % 4
    <af gf'>1 | % 5
    <af gf'>1 | % 6
    << bf1~ \\  af'1 >> | % 7
    << bf,1 \\ gf' >> | % 8
  }
>>

\markup "Spookiness"
\new GrandStaff <<
  \new Staff \with { instrumentName = "JX-03" } \relative c' {
    \time 4/4
    \key gf \major
    <df bf'>1 | % 1
    <c a'>1 | % 2
    <df bf'>1 | % 3
    <ef cf'>1 | % 4
  }
>>