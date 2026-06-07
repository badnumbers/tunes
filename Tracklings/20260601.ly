\version "2.20.0"
\language "english"

\header {
  title = "20260601"
  subtitle = "E♭ minor"
}

\markup "REV2 U2 P23 'Quirky Prof BN' mod wheel half up"

\new GrandStaff <<
  \new Staff \with { instrumentName = "REV2" } \relative c'' {
    \time 4/4
    \key ef \minor
    \partial 4.
    f8 gf f |
    cf8 r df ef r f gf r  | % 1
    ef' df r af bf r f gf | % 2
    r f e r c ef r gf | % 3
    a^"Not sure how this rhythm should lead back in" cf4 a af 
  }
  \new Staff \with { instrumentName = "DX7" } \relative c' {
    \key ef \minor
    \clef bass
    r4.
    af,8 af' r cf, cf' r ef, ef' | % 1
    r f, f' r d, d' r ef, | % 2
    ef' r c, c' r cf, cf' r | % 3
    r d, d' r f, f' f, f'
  }
>>