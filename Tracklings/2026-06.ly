\version "2.20.0"
\language "english"

\header {
  title = "2026-06"
  subtitle = "B"
}

\markup "REV2 U2 P5 Starsky Test BN"

\new GrandStaff <<
  \new Staff \with { instrumentName = "REV2" } \relative c'' {
    \time 4/4
    \key b \major
    <cs fs b>8. <cs fs b>8. <cs fs b>8~ <cs fs b>2~ | % 1
    <cs fs b>1 | % 2
    <cs fs b>8. <cs fs b>8. <cs fs b>8~ <cs fs b>2~ | % 3
    <cs fs b>1 | % 4
  }
  \new Staff \with { instrumentName = "DX7" } \relative c {
    \key b \major
    \clef bass
    <e e'>8. <e e'>8. <ds ds'>8~ <ds ds'>2~ | % 1
    <ds ds'>1 | % 2
    <cs cs'>8. <cs cs'>8. <ds ds'>8~ <ds ds'>2~ | % 3
    <ds ds'>1 | % 4
  }
>>

\markup "Unosynth Wobbly 70s"

\new GrandStaff <<
  \new Staff \with { instrumentName = "Unosynth" } \relative c {
    \time 4/4
    \key b \major
    \clef bass
    ds16 es fs as~ as4~ as2 | % 1
    ds,16 es fs as~ as4~ as2 | % 2
    b16 as gs e~ e4~ e2 | % 3
    b'16 as gs e~ e4~ e2 | % 4
  }
>>

\new GrandStaff <<
  \new Staff \with { instrumentName = "Unosynth" } \relative c {
    \time 3/4
    \key b \major
    \clef bass
    b8 d fs d fs d | % 1
    b8 d fs d fs d | % 2
    b8 ds g ds g ds | % 3
    b8 ds g ds a' ds, | % 4
  }
>>

\new GrandStaff <<
  \new Staff \with { instrumentName = "Unosynth" } \relative c' {
    \key b \major
    \clef bass
    b,8 c' b,8 c' b,8 c' b,8 c' | % 1
  }
>>

\markup "JX-03 Saw reso lead, JP-08 Aphex pad '85"

\new GrandStaff <<
  \new Staff \with { instrumentName = "JX-03" } \relative c''' {
    \time 4/4
    \key b \major
    gs8( as ds) r r2 | % 1
    r1 | % 2
    fs4.-. cs8-. r2 | % 3 
    es4.-. as,8-. r2 | % 4
  }
  \new Staff \with { instrumentName = "JP-08" } \relative c' {
    \key b \major
    <fs b>1^"These parallel fourths sound really good" | % 1
    <ds gs>1 | % 2
    <es as>1 | % 3
    <gs cs> | % 4
  }
>>

\new GrandStaff <<
  \new Staff \with { instrumentName = "JX-03" } \relative c''' {
    \time 4/4
    \key b \major
    gs8( as ds) r r2 | % 1
    r1 | % 2
    fs4.-. cs8-. r2 | % 3
    es4.-. as,8-. r2 | % 4
    ds4.-. gs,8-. r2 | % 5
    r1 | % 6
    r2 gs8( as cs) r | % 7
    es,4-. r4 r2 \break | % 8
    gs8( as ds) r r2 | % 9
    r1 | % 10
    fs4.-. cs8-. r2 | % 11
    es4.-. as,8-. r2 | % 12
    e'4.-. gs,8-. r2 | % 13
    r1 | % 14
    as4-. r4 r2 | % 15
    r4. es4.( f4) | % 16
  }
  \new Staff \with { instrumentName = "JP-08" } \relative c' {
    \key b \major
    <b ds b'>1 | % 1
    <ds gs>1 | % 2
    <cs es as>1 % 3
    << { cs'1 } \\ { ds,2 es } >> | % 4
    <gs, ds' fs>1 | % 5
    <b gs'>1 | % 6
    <gs ds' es>1 | % 7
    <as cs>1 | % 8
    <b ds b'>1 | % 9
    <ds gs>1 | % 10
    <cs es as>1 % 11
    << { cs'1 } \\ { ds,2 cs } >> | % 12
    << { b'1 } \\ { <b, e>1 }~  >> | % 13
    << { fs'2 gs2 } \\ { <b, e>1 } >> | % 14
    <fs ds' as'>1 | % 15
    <cs' es>1 | % 16
  }
>>

\new GrandStaff <<
  \new Staff \with { instrumentName = "JP-08" } \relative c' {
    \key b \major
    <es as>1^"These parallel fourths sound really good" | % 1
    <gs cs> | % 2
    <fs b>1 | % 3
    <ds gs>1 | % 4
  }
  \new Staff \with { instrumentName = "Unosynth" } \relative c {
    \time 4/4
    \key b \major
    \clef bass
    ds8 es fs as~ as2 | % 1
    ds,8 es fs as~ as2 | % 2
    b8 as gs e~ e2 | % 3
    b'8 as gs e~ e2 | % 4
  }
>>