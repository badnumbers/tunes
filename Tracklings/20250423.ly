\version "2.20.0"
\language "english"

\header {
  title = "20240423"
}

\markup "Just a rhythm in 7/4 that got stuck in my head"

\new GrandStaff <<
  \new Staff \relative c''' {
   \key g \major
   \time 7/4
   a4 a4. g4. a4 a c | % 1
   a4 a4. g4 a4. a4 f | % 2  Slightly alternative rhythm
  }
  \new Staff \relative c' {
   \key g \major
   \clef bass
   \time 7/4
  }
>>