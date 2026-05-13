\version "2.20.0"
\language "english"

\header {
  title = "20260510"
  subtitle = "G♭"
}

\markup "DX7 'Wobbly cheese hat' + chorus + reverb"
\markup "REV2 U1 P8 'Moody Pad BN' + reverb"

\new GrandStaff <<
  \new Staff \with { instrumentName = "DX7" } \relative c''' {
    \time 2/4
    \key gf \major
    bf2 | cf | bf | f | af~ | af | \break
    bf2 | cf | bf | f | af |  ef | gf~ | gf | \break
    af |  ef | gf~ | gf | \break
    af |  ef | gf | df | ef~ | ef~ | ef~ | ef | \break
  }
  \new Staff \with { instrumentName = "REV2" } \relative c'' {
    \key gf \major
    <gf df'>2 | <gf ef'> | <gf df'> | <df bf'> | <f~ df'~> | <f df'> |
    <gf df'>2 | <gf ef'> | <gf df'> | <df bf'> | <ef cf'>  | <af gf'> | <df,~ bf'~> | <df bf'> |
    <ef cf'>  | <af gf'> | <df,~ bf'~> | <df bf'> |
    <ef cf'>  | <af gf'> | <df, bf'> | <af' e'> | <cf,~ gf'~> | <cf~ gf'~> | <ef~ cf'~> | <ef cf'>
  }
>>