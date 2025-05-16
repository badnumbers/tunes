\version "2.20.0"
\language "english"

\header {
  title = "20240422"
}

\new GrandStaff <<
  \new Staff \relative c''' {
   \key g \major
   e8-5 d-4 e4.-5 a,-1 | % 1
   b-2 c-3 b4~-2 | % 2
   b8 a4.-1 fs-3 g8~-4 | % 3
   g4 a4.-5 e-2 \break | % 4
   e'8-5 d-4 e4.-5 a,-2 | % 5
   b-2 c-3 b4~-2 | % 6
   b8 a4.-1 fs-3 g8~-4 | % 7
   g4 a4.-5 d,-1 \break | % 8
   c2 b2~ | % 9
   b2 d4 e | % 10
   fs2 e4 fs | % 11
   a1 \break | % 12
   b2 a4 g | % 13
   fs2 e | % 14
   fs1 | % 15
   e1 \break | % 16
   cs2 b2~ | % 17
   b2 e4 g | % 18
   b2 a | % 19
   g f \break | % 20
   e1 | % 21
   d | % 22
   e | % 23
   d | % 24
  }
  \new Staff \relative c' {
   \key g \major
   \clef bass
   g8 c e c g c e c | % 1
   g c e c g c e c | % 2
   a c e c a c e c | % 3
   a c e c a c e c | % 4
   g8 c e c g c e c | % 5
   g c e c g c e c | % 6
   fs, a d a fs a d a | % 7
   fs a d a fs a d a | % 8
   g b d b g b d b | % 9
   g b d b g b d b | % 10
   fs b d b fs b d b | % 11
   fs cs' e cs fs, cs' e cs | % 12
   d, g b g d g b g | % 13
   e as d as e as d as | % 14
   fs a d a fs a d a | % 15
   e g b g e g cs a | % 16
   g8 b d b g b d b | % 17
   g8 b d b g b e b | % 18
   a c e c a c e c | % 19
   a cs f cs a cs f cs | % 20
   g cs e cs g cs e cs | % 21
   g b d b g b d b | % 22
   g cs e cs g cs e cs | % 23
   fs, b d b fs b d b | % 24
  }
>>