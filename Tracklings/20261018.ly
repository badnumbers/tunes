\version "2.20.0"
\language "english"

\header {
  title = "20261018"
  subtitle = "F major"
}

\markup "JP-08 Upsetting"
\markup "Really horrible"

\new GrandStaff <<
  \new Staff \with { instrumentName = "JP-08" } \relative c' {
    \time 4/4
    \tempo Grave % very slow
    \key f \major
    c4 bf2. | % 1
    c4 bf df2 | % 2
    c4 bf2. | % 3
    c4 bf df2 | % 4
    e4 d2. | % 5
    e4 df2. | % 6
  }
   \new Staff \with { instrumentName = "JP-08" } \relative c, {
    \time 4/4
    \key f \major
    \clef bass
    f4 df2. | % 1
    f4 df g2 | % 2
    f4 df2. | % 3
    f4 df g2 | % 4
    bf4 a2. | % 5
    bf4 g2. | % 6
  }
>>