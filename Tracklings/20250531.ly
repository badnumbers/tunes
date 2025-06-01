\version "2.20.0"
\language "english"

\header {
  title = "20250531"
  subtitle = "C# minor"
}

\markup "REV2 U1 P30 Soft Melty BN"

\new GrandStaff <<
  \new Staff \with { instrumentName = "REV2" } \relative c''' {
    \key cs \minor
    \repeat volta 2 {
    gs4. fs8~ fs2 | % 1
    gs4. ds8~ ds2 | % 2
    cs1 | % 3
    r1 | \break % 4
    }
    b1 | % 9
    ds1 | % 10
    cs1 | % 11
    r1 \break | % 12
    b1 | % 13
    ds1 | % 14
    \key af \major f1 | % 15
    g1 \break | % 16
    af4.^"Main theme 1" bf4. f4~ | % 17
    f1 | % 18
    c'4. bf8~ bf2 | % 19 Not sure if this is a# Think we might be in like Ab now
    c4. g8~ g2 \break | % 20
    af4. g4. ef4 | % 21
    f4 af4~ af2 | % 22
    g4. ef8~ ef2 | % 23
    f4 c'4~ c2 \break | % 24
    af4. bf4. f4~ | % 25
    f1 | % 26
    c'4. ef8~ ef2 | % 27
    bf4. df8~ df2 \break | % 28
    c4. bf8~ bf2 | % 29
    \key a \major gs4. fs8~ fs2 | % 30
    e1 | % 31
    d1 \break | % 32
    cs1 | % 33
    r1 | % 34
    \key e \major \repeat volta 2 {
    gs'4. fs8~ fs2 | % 35
    gs4. ds8~ ds2 | % 36
    cs1 | % 37
    r1 | \break % 38
    }
  }
  \new Staff \with { instrumentName = "REV2" } \relative c' {
    \key cs \minor
    \clef bass
    cs,8 cs' cs, cs' cs, cs' cs, cs' | % 1
    cs,8 cs' cs, cs' cs, cs' cs, cs' | % 2
    a,8 a' a, a' a, a' a, a' | % 3
    a,8 a' a, a' a, a' a, a' | % 4
    gs,8 gs' gs, gs' gs, gs' gs, gs' | % 9
    b,8 b' b, b' b, b' b, b' | % 10
    a,8 a' a, a' a, a' a, a' | % 11
    a,8 a' a, a' a, a' a, a' | % 12
    gs,8 gs' gs, gs' gs, gs' gs, gs' | % 13
    b,8 b' b, b' b, b' b, b' | % 14
    \key af \major df,8 df' df, df' df, df' df, df' | % 15
    ef,8 ef' ef, ef' ef, ef' ef, ef' | % 16
    f,8 f' f, f' f, f' f, f' | % 17
    df,8 df' df, df' df, df' df, df' | % 18
    af8 af' af, af' af, af' af, af' | % 19
    ef,8 ef' ef, ef' ef, ef' ef, ef' | % 20
    f,8 f' f, f' f, f' f, f' | % 21
    df,8 df' df, df' df, df' df, df' | % 22
    af8 af' af, af' af, af' af, af' | % 23
    ef,8 ef' ef, ef' ef, ef' ef, ef' | % 24
    f,8 f' f, f' f, f' f, f' | % 25
    df,8 df' df, df' df, df' df, df' | % 26
    af8 af' af, af' af, af' af, af' | % 27
    ef,8 ef' ef, ef' ef, ef' ef, ef' | % 28
    f,8 f' f, f' f, f' f, f' | % 29
    \key a \major e,8 e' e, e' e, e' e, e' | % 30
    gs,8 gs' gs, gs' gs, gs' gs, gs' | % 31
    fs,8 fs' fs, fs' fs, fs' fs, fs' | % 32
    d,8 d' d, d' d, d' d, d' | % 33
    d,8 d' d, d' d, d' d, d' | % 34
    a8 a' a, a' a, a' a, a' | % 35
    a,8 a' a, a' a, a' a, a' | % 36
    e,8 e' e, e' e, e' e, e' | % 37
    e,8 e' e, e' e, e' e, e' | % 38
  }
>>

\new GrandStaff <<
  \new Staff \with { instrumentName = "REV2" } \relative c''' {
    \key a \major
    gs4. a4. e4~ | % 1
    e1 | % 2
    cs'4. fs,8~ fs2 | % 3
    b4. fs8~ fs2 | % 4
    gs4. a8~ a2 | % 5
    e'4. cs8~ cs2 | % 6
    g4.^"Dissonant G is optional but sounds quite cool" fs8~ fs2 | % 7 
    e1 | % 8
  }
  \new Staff \with { instrumentName = "REV2" } \relative c' {
    \key a \major
    \clef bass
    e,8 e' e, e' e, e' e, e' | % 1
    cs,8 cs' cs, cs' cs, cs' cs, cs' | % 2
    a, a' a, a' a, a' a, a' | % 3
    d, d' d, d' d, d' d, d' \break | % 4
    cs,8 cs' cs, cs' cs, cs' cs, cs' | % 5
    a^"Or maybe an octave lower" a' a, a' a, a' a, a' | % 6
    d,,8 d' d, d' d, d' d, d' | % 7
    b,8 b' b, b' b, b' b, b' | % 8
  }
>>