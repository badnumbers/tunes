\version "2.20.0"
\language "english"

\header {
  title = "20250101"
  subtitle = "A"
}

\markup "REV2 U1 P36 Broken Horn BN"
\markup "Hydrasynth F008 Detuned Keys BN"

\new GrandStaff <<
  \new Staff \with { instrumentName = "REV2" } \relative c'' {
    \key a \major
    b1 | % 1
    cs4. e8~ e2 | % 2
    fs4. d8~ d2 | % 3
    fs4. cs8~ cs2 \break | % 4
    b1 | % 5
    cs4. e8~ e2 | % 6
    fs4. cs8~ cs2 | % 7 
    gs'4. e8~ e2 \break | % 8
    a4. gs4. e4 | % 9
    gs4. fs4. b,4 | % 10
    d4. e8~ e2 | % 11
    fs4. gs8~ gs2 | % 12
    a4. gs4. e4 | % 13
    gs4. fs4. b,4 | % 14
    \key d \major
    d4. e8~ e2 | % 15
    d4. e8~ e2 | % 16
    g4. fs4. d4 | % 17
    fs4. e4. a,4 | % 18
    \key c \major
    c4. d8~ d2 | % 19
    c4. d8~ d2 | % 20
    c4. d8~ d2 | % 21
    c4. d8~ d2 | % 22
    e1 | % 23
    \key e \major
    cs1 | % 24
  }
  \new Staff \with { instrumentName = "Hydrasynth" } \relative c' {
    \key a \major
    \clef bass
    d,8 b' d, b' d, b' d, b' | % 1
    d, b' d, b' d, b' d, b' | % 2
    d, b' d, b' d, b' d, b' | % 3
    d, b' d, b' d, b' d, b' | % 4
    e, b' e, b' e, b' e, b' | % 5
    d, b' d, b' d, b' d, b' | % 6
    cs, a' cs, a' cs, a' cs, a' | % 7
    d, b' d, b' d, b' d, b' | % 8
    fs cs' fs, cs' fs, cs' fs, cs' | % 9
    e, b' e, b' e, b' e, b' | % 10
    fs d' fs, d' fs, d' fs, d' | % 11
    d, a' d, a' d, a' d, a' | % 12
    fs cs' fs, cs' fs, cs' fs, cs' | % 13
    e, b' e, b' e, b' e, b' | % 14
    \key d \major
    a fs' a, fs' a, fs' a, fs' | % 15
    g, e' g, e' g, e' g, e' | % 16
    e, b' e, b' e, b' e, b' | % 17
    d, a' d, a' d, a' d, a' | % 18
    \key c \major
    g e' g, e' g, e' g, e' | % 19
    f,^"Going up a note here also sounds good" d' f, d' f, d' f, d' | % 20
    g, e' g, e' g, e' g, e' | % 21
    f, d' f, d' f, d' f, d' | % 22
    c, g' c, g' c, g' c, g' | % 23
    \key e \major
    cs, gs' cs, gs' cs, gs' cs, gs' | % 24
  }
>>

\markup "C♯ minor"
\new GrandStaff <<
  \new Staff \with { instrumentName = "REV2" } \relative c'' {
    \key e \major
    <ds gs cs>1 | % 1
    <ds gs cs>4. <ds gs cs>8~ <ds gs cs>2 | % 2
    <ds gs cs>1 | % 3
    <ds gs cs>4. <ds gs cs>8~ <ds gs cs>2 | % 4
  }
  \new Staff \with { instrumentName = "REV2" } \relative c {
    \key e \major
    \clef bass
    <cs cs'>1 | % 1
    <cs cs'>4. <a a'>8~ <a a'>2 | % 2
    <e' e'>1 | % 3
    <e e'>4. <b b'>8~ <b b'>2 | % 4
  }
>>

\markup "C♯ minor"
\new GrandStaff <<
  \new Staff \with { instrumentName = "REV2" } \relative c'' {
    \key e \major
    gs16' ds cs ds gs ds cs ds gs ds cs ds gs ds cs ds | % 1
    gs ds cs ds gs ds cs ds gs ds cs ds gs ds cs ds | % 2
    gs ds cs ds gs ds cs ds gs ds cs ds gs ds cs ds | % 3
    gs ds cs ds gs ds cs ds gs ds cs ds gs ds cs ds | % 4
  }
  \new Staff \with { instrumentName = "REV2" } \relative c {
    \key e \major
    \clef bass
    <cs cs'>1 | % 1
    <a a'> | % 2
    <e' e'>1 | % 3
    <b b'> | % 4
  }
>>

\markup "Hydrasynth D113 80s Shimmer BN"
\markup "A major"
\new GrandStaff <<
  \new Staff \with { instrumentName = "REV2" } \relative c'' {
    \key a \major
    <b e gs>4~ <b e gs>8.  <cs e fs>16~ <cs e fs>2 | % 1
    <a d fs>4~ <a d fs>8.  <b d e>16~ <b d e>2 | % 2
    <e, a cs>4~ <e a cs>8.  <fs a b>16~ <fs a b>2 | % 3
    <g c e>4~ <g c e>8.  <a c d>16~ <a c d>2 | % 4
  }
  \new Staff \with { instrumentName = "REV2" } \relative c {
    \key a \major
    \clef bass
    <a a'>4~ <a a'>8. <a a'>16~ <a a'>2 | % 1 Tonic
    <g g'>4~ <g g'>8. <g g'>16~ <g g'>2 | % 2 Flat 7
    <d d'>4~ <d d'>8. <d d'>16~ <d d'>2 | % 3 4
    <f f'>4~ <f f'>8. <f f'>16~ <f f'>2 | % 4 -ve 5
  }
>>