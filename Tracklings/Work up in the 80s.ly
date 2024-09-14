\version "2.20.0"
\language "english"

\header {
  title = "Woke up in the 80s"
  subtitle = "C"
}

\markup "JX-03 'Woke up in the 80s'"

\new GrandStaff <<
  \new Staff \with { instrumentName = "JX-03" } \relative c'' {
    \key c \major
    r4 r8 d8->~ d4~ d8 e8->~ | % 1
    e4~ e8 b8->~ b4~ b8 c8->~ | % 2
    c4~ c8 d8->~ d4~ d8 e8->~ | % 3
    e4~ e8 b8->~ b4~ b8 c8->~ | % 4
    c4~ c8 d8->~ d4~ d8 e8->~ | % 5
    e4~ e8 b8->~ b4~ b8 c8->~ | % 6
    c4~ c8 d8->~ d4~ d8 e8->~ | % 7
    e4~ e8 b8->~ b4~ b8 c8->~ | % 8
    c4~ c8 d8->~ d4~ d8 e8->~ | % 9
    e4~ e8 b8->~ b4~ b8 c8->~ | % 10
    c4~ c8 d8->~ d4~ d8 e8->~ | % 11
    e4~ e8 b8->~ b4~ b8 c8->~ | % 12
  }
  \new Staff \with { instrumentName = "JX-03" } \relative c' {
    \key c \major
    \clef bass
    c,8 g' c r c,8 g' c r | % 1
    c,8 g' c r c,8 g' c r | % 2
    c,8 g' c r c,8 g' c r | % 3
    c,8 g' c r c,8 g' c r | % 4
    a,8 e' a r a,8 e' a r | % 5
    a,8 e' a r a,8 e' a r | % 6
    a,8 e' a r a,8 e' a r | % 7
    a,8 e' a r a,8 e' a r | % 8
    f,8 c' f r f,8 c' f r | % 9
    f,8 c' f r f,8 c' f r | % 10
    g,8 d' g r g,8 d' g r | % 11
    g,8 d' g r g,8 d' g r | % 12
  }
>>