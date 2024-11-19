\version "2.20.0"
\language "english"

\header {
  title = "20241022"
}

\markup "Hydrasynth D080"

\new GrandStaff <<
  \new Staff \with { instrumentName = "Hydrasynth" } \relative c'' {
   \key a \major
   r8. b16~ b8~ b16 cs16~ cs8~ cs16 e~ e4~ | % 1
   e8. b16~ b8~ b16 cs16~ cs8~ cs16 e~ e4 | % 2
   fs1 | % 3
   gs2 e \break | % 4
   r8. b16~ b8~ b16 cs16~ cs8~ cs16 e~ e4~ | % 5
   e8. b16~ b8~ b16 cs16~ cs8~ cs16 e~ e4 | % 6
   fs1 | % 7
   gs2 a \break | % 8
   b8. d,16~ d8~ d16 e16~ e8~ e16 fs~ fs4~ | % 9
   fs8. d16~ d8~ d16 e16~ e8~ e16 fs~ fs4 | % 10
   g1 | % 11
   a1 \break | % 12
   b8. d,16~ d8~ d16 e16~ e8~ e16 fs~ fs4~ | % 13
   fs8. d16~ d8~ d16 e16~ e8~ e16 fs~ fs4 | % 14
   e1 | % 15
   cs1 | % 16
  }
  \new Staff \with { instrumentName = "Hydrasynth
" } \relative c' {
   \key a \major
   \clef bass
   <a a,>1 | % 1
   <a a,>1 | % 2
   <d, d,>1 | % 3
   <e e,>1 | % 4
   <a a,>1 | % 5
   <a a,>1 | % 6
   <ef ef,>1 | % 7
   <d d,>1 | % 8
   <g g,>1 | % 9
   <b b,>1 | % 10
   <a a,>1 | % 11
   <fs fs,>1 | % 12
   <g g,>1 | % 13
   <b b,>1 | % 14
   <g g,>1 | % 15
   <gs gs,>1 | % 16
  }
>>