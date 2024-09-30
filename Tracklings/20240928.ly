\version "2.20.0"
\language "english"

\header {
  title = "20240928"
  subtitle = "E (C# minor)"
}

\markup "JP-08 46"

\new GrandStaff <<
  \new Staff \with { instrumentName = "JP-08" } \relative c' {
    \key e \major
    <<
      { gs'1~ }
      \\
      { ds4 b a2 }
    >> |
    <<
      { gs'1 }
      \\
      { ds4 b a2 }
    >> |
  }
  \new Staff \with { instrumentName = "JP-08" } \relative c {
    \key e \major
    \clef bass
    b4 ds cs2 |
    b4 ds cs e |
  }
>>

\new GrandStaff <<
  \new Staff \with { instrumentName = "JP-08" } \relative c' {
    \key e \major
    <<
      { gs'2~ gs4 fs4 }
      \\
      { ds4 b a2 }
    >> |
    <<
      { gs'2~ gs4 ds4 }
      \\
      { ds4 b a2 }
    >> |
  }
  \new Staff \with { instrumentName = "JP-08" } \relative c {
    \key e \major
    \clef bass
    b4 ds cs2 |
    b4 ds cs2 |
  }
>>

\new GrandStaff <<
  \new Staff \with { instrumentName = "JP-08" } \relative c' {
    \key e \major
    <<
      { e1~ }
      \\
      { b4 a gs a }
    >> |
    <<
      { e'1 }
      \\
      { b4 a gs a }
    >> |
    <<
      { ds1~ }
      \\
      { g, }
    >> |
    <<
      { ds'1~ }
      \\
      { fs, }
    >> |
  }
  \new Staff \with { instrumentName = "JP-08" } \relative c' {
    \key e \major
    \clef bass
    g4 cs, ds cs |
    g' cs, ds cs |
    b1~ |
    b1 |
  }
>>

\new GrandStaff <<
  \new Staff \with { instrumentName = "JP-08" } \relative c'' {
    \key e \major
    <<
      { gs4 fs g fs }
      \\
      { b,1 }
    >> |
  }
  \new Staff \with { instrumentName = "JP-08" } \relative c {
    \key e \major
    \clef bass
    ds1 |
  }
>>

\new GrandStaff <<
  \new Staff \with { instrumentName = "JP-08" } \relative c'' {
    \key e \major
    <<
      { a2~ a4 b }
      \\
      { e,1~ }
    >> |
    <<
      { a2 b4 as^"| a♮ also works for this cadence, a bit calmer..." }
      \\
      { e1 }
    >> |
    <<
      { gs1 }
      \\
      { ds1 }
    >> |
  }
  \new Staff \with { instrumentName = "JP-08" } \relative c {
    \key e \major
    \clef bass
    cs4 fs cs2 |
    cs4 fs cs bs |
    b1 |
  }
>>