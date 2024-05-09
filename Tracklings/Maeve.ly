\version "2.20.0"
\language "english"

\header {
  title = "Maeve"
}

\markup "Right hand is Microvolt using the fold oscillator and the envelope is folding it slightly. Some GVerb works great."
\markup "Left hand was the hydra doing two sawtooths with a lot of analogue feel, a slow filter envelope and some room reverb."
\markup "I guess it's in B dorian."

\new GrandStaff <<
  \new Staff \with { instrumentName = "Microvolt" } \relative {
    \key a \major
    \time 3/4
    \repeat volta 2 { b'8 cs fs a fs cs | % 1
    b8 cs fs a fs cs | % 2
    b8 cs fs a fs cs | % 3
    b gs fs gs b d } \break | % 4
    b8 cs fs a fs cs | % 9
    b8 cs fs a fs cs | % 10
    b8 cs fs a fs cs | % 11
    b gs fs gs b d \break  | % 12
    b8 cs fs a fs cs | % 13
    b8 cs fs a fs cs | % 14
    b d fs a fs d | % 15
    b gs fs gs b d \break | % 16
    b8 cs fs a fs cs | % 17
    b8 cs fs a fs cs | % 18
    f fs a gs fs f | % 19
    d cs gs a cs d \break | % 20
    b8 cs fs a fs cs | % 21
    b a gs fs gs a | % 22
    fs2.~ | % 23
    fs \break | % 24
    \key g \major
    \time 7/8
    r8 g a4 g8 a b | % 25
    r8 fs a4 fs8 a d | % 26
  }
  \new Staff \with { instrumentName = "Hydrasynth" } \relative {
    \key a \major
    \clef bass
    R2. | % 1 - (R is a full-bar rest)
    R2. | % 2
    R2. | % 3
    R2. | % 4
    fs2 cs8 e | % 9
    fs2 a8 fs | % 10
    e4 b cs | % 11
    d e gs | % 12
    fs2 cs8 e | % 13
    fs2 a8 fs | % 14
    g4 a b | % 15
    cs ds f | % 16
    \clef treble
    fs4 cs e | % 17
    fs gs a | % 18
    b f gs | % 19
    f d gs, | % 20
    \clef bass
    fs2 cs8 e | % 21
    gs4 d e | % 22
    fs2.~ | % 23
    fs | % 24
    \key g \major
    e4. c2 | % 25
    d4. b2 | % 26
  }
>>