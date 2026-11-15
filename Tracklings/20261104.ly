\version "2.20.0"
\language "english"

\header {
  title = "20261104"
  subtitle = "B♭"
}

\markup "JX-03 Guitar? - don't be shy with the reverb either"
\markup "I think this uses every note in the chromatic scale"

\new GrandStaff <<
  \new Staff \with { instrumentName = "JX-03" } \relative c''' {
    \time 4/4
    \key bf \major
    bf4. a f4~ | % 1
    f8 g4. d4 ef | % 2
    bf'4. a f4~ | % 3
    f8 g4. d4 ef | % 4
    gf4. af f4~ | % 5
    f8 gf4. d4 ef | % 6
    gf4. af f4~ | % 7
    f8 bf4. df4 c | % 8
  }
   \new Staff \with { instrumentName = "JX-03" } \relative c' {
    \time 4/4
    \key bf \major
    \clef bass
    bf8 ef bf ef bf ef bf ef | % 1
    bf8 ef bf ef bf ef bf ef | % 2
    bf8 ef bf ef bf ef bf ef | % 3
    bf8 ef bf ef bf ef bf ef | % 4
    a,8 d a d a d a d | % 5
    a,8 d a d g c g c | % 6
    bf8 ef bf ef bf ef b gf' | % 7
    b,8 gf' b, gf' a, e' a, e' | % 7
  }
>>

\markup "Hydrasynth H113 Evensynth BN with lots of reverb"

\new GrandStaff <<
  \new Staff \with { instrumentName = "Hydrasynth" } \relative c''' {
    \time 4/4
    \key bf \major
    a4 g d2 | % 1
    e4 g d2 | % 2
    e4 g d2 | % 3
    
  }
   \new Staff \with { instrumentName = "Hydrasynth" } \relative c' {
    \time 4/4
    \key bf \major
    \clef bass
    d4 ef bf2 | % 1
    c4 a bf f | % 2
    g4 a bf c | % 3
  }
>>

\markup "Haunting and beautiful"

\new GrandStaff <<
  \new Staff \with { instrumentName = "Hydrasynth" } \relative c''' {
    \time 4/4
    \key bf \major
    a4 g e f | % 1
    d1 | % 2
  }
   \new Staff \with { instrumentName = "Hydrasynth" } \relative c' {
    \time 4/4
    \key bf \major
    \clef bass
    c4 e a, c | % 1
    f2 bf, | % 2
  }
>>

\markup "Like cathedrale engloutie - very slow"

\new GrandStaff <<
  \new Staff \with { instrumentName = "Hydrasynth" } \relative c'' {
    \time 4/4
    \key bf \major
    d1~ | % 1
    d1~ | % 2
    d1 | % 3
  }
   \new Staff \with { instrumentName = "Hydrasynth" } \relative c, {
    \time 4/4
    \key bf \major
    \clef bass
    r1 | % 1
    e2 a | % 2
    f d | % 3
  }
>>

\markup "MEGA FM 63"
\markup "Why do I like it so much?"

\new GrandStaff <<
  \new Staff \with { instrumentName = "Hydrasynth" } \relative c'''' {
    \time 4/4
    \key bf \major
    \clef "treble^(8)" % Notes are an octave higher
    a2 f | % 1
    g1 | % 2
  }
   \new Staff \with { instrumentName = "Hydrasynth" } \relative c'' {
    \time 4/4
    \key bf \major
    f2 bf, | % 1
    ef1 | % 2
  }
>>

\markup "SH-01A Dirty ol' funk + reverb"
\new GrandStaff <<
  \new Staff \with { instrumentName = "SH-01A" } \relative c''' {
    \time 4/4
    \key bf \major
    \partial 8 g8\staccato |
    g8 a16 bf8. d,8~ d4. c16 d | % 1
    e2~ e4. g8\staccato | % 2
    g8 a16 bf8. d,8 d4. c16 d | % 3
    e4~ e16 c e f g4. g8 | % 2
  }
>>

\markup "Instant Aphex - just add reverb"
\new GrandStaff <<
  \new Staff \with { instrumentName = "SH-01A" } \relative c'' {
    \time 4/4
    \key bf \major
    \partial 4 ef8 f |
    bf4.\staccato a\staccato g4\staccato | % 1
    r8 d\staccato r d\staccato r d ef bf\staccato | % 2
    r8 bf f g bf\staccato r g c | % 3
    e g f d g, bf ef f | % 4
  }
  \new Staff \with { instrumentName = "SH-01A" } \relative c'' {
    \time 4/4
    \key bf \major
    \partial 4 r4
    r8 d( g,) r c( f,) r bf( | % 1
    ef,) r f r g r4. | % 2
    d8\staccato r8 d ef d bf\staccato r4 | % 3
    c4 d ef f | % 4
  }
>>