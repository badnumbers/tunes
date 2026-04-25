\version "2.20.0"
\language "english"

\header {
  title = "20260308     "
  subtitle = "G♭ major"
}

\markup "JX-03 Rueful 80s"

\new GrandStaff <<
  \new Staff \with { instrumentName = "JX-03" } \relative c'' {
    \key gf \major
    ef2~ ef8 ef8 af gf | % 1
    f8. d bf d f8 gf | % 2
  }
  \new Staff \with { instrumentName = "JX-03" } \relative c {
    \key gf \major
    \clef bass
    cf8. bf af8~ af4 df8 bf | % 1
    cf8. af f af df8 ef | % 2
  }
>>