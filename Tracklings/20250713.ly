\version "2.20.0"
\language "english"

\header {
  title = "20250713"
  subtitle = "C"
}

\markup "Hydrasynth H121 Final Bell BN"

\new GrandStaff <<
  \new Staff \with { instrumentName = "REV2" } \relative c' {
    \key c \major
    \time 3/4
    c16 e g e c d g e c e g e | % 1
    c16 e g e c d g e c e g e | % 2
    c16 e g e c d g e c e g e | % 3
    c16 e g e c d g e c e g e | % 4
  }
  \new Staff \with { instrumentName = "REV2" } \relative c, {
    \key c \major
    \clef bass
    f4 g2 | % 1
    f4 g2 | % 2
    f4 g2 | % 3
    e4 d2 | % 4
  }
>>