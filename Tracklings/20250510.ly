\version "2.20.0"
\language "english"

\header {
  title = "20250510"
  subtitle = "C lydian"
}

\markup "Hydrasynth H087 Comtemplative BN, Unosynth 01"

\new GrandStaff <<
  \new Staff \with { instrumentName = "Hydrasynth" } \relative c' {
    \key g \major
    c1 | % 1
    d | % 2
    e~ | % 3
    e | \break % 4
    c1 | % 5
    d | % 6
    e~ | % 7
    e | \break % 8
    g | % 9
    b | % 10
    a~ | % 11
    a2 b | \break % 12
    c1 | % 13
    b | % 14
    a~ | % 15
    a | \break % 16
    g | % 17
    a | % 18
    b~ | % 19
    b | \break % 20
    g | % 21
    a | % 22
    b~ | % 23
    b | \break % 24
    c | % 25
    g | % 26
    a | % 27
    g | \break % 28
    f | % 29
    bf | % 30
    ef,~ | % 31
    ef | % 32
  }
  \new Staff \with { instrumentName = "Unosynth" } \relative c {
    \key g \major
    \clef bass
    c8 c c c c c c c | % 1
    c8 c c c c c c c | % 2
    c8 c c c c c c c | % 3
    c8 c c c c c c c | % 4
    c8 c c c c c c c | % 5
    c8 c c c c c c c | % 6
    c8 c c c c c c c | % 7
    c8 c c c c c c c | % 8
    b8 b b b b b b b | % 9
    g8 g g g g g g g | % 10
    fs8 fs fs fs fs fs fs fs | % 11
    d8 d d d d d d d | % 12
    e8 e e e e e e e | % 13
    a8 a a a a a a a | % 14
    d8 d d d d d d d | % 15
    fs8, fs fs fs fs fs fs fs | % 16
    e8 e e e e e e e | % 17
    e8 e e e e e e e | % 18
    c'8 c c c c c c c | % 19
    c8 c c c c c c c | % 20
    g8 g g g g g g g | % 21
    g8 g g g g g g g | % 22
    fs8 fs fs fs fs fs fs fs | % 23
    fs8 fs fs fs fs fs fs fs | % 24
    ef8 ef ef ef ef ef ef ef | % 25
    ef8 ef ef ef ef ef ef ef | % 26
    bf'8 bf bf bf bf bf bf bf | % 27
    bf8 bf bf bf bf bf bf bf | % 28
    d,8 d d d d d d d | % 29
    d8 d d d d d d d | % 30
    af'8 af af af af af af af | % 31
    af8 af af af af af af af | % 32
  }
>>