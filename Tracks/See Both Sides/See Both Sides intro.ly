\version "2.24.4"
\language "english"

\header {
  title = "See Both Sides intro"
  subtitle = "DX7 electric piano"
}

music = \relative
{
\new PianoStaff <<
  \new Staff = "up" {
    \key e \major
    \partial 8 r8 | % anacrusis
    r4 r8 <fs' b ds>\staccato r8 <fs b ds>8 r4 | % 1
    <gs cs fs>4. r8 r4 e'4 | % 2
    << { r4 r8 fs8~ fs4 } \\ { <fs, b d>2~ <fs b d>4 } >> r4 | % 3
    <gs b ds>2. r4 | % 4
    r4 r8 <fs b ds>\staccato r8 <fs b ds>8 r4 | % 5
    <gs cs fs>4. r8 r4 e'4 | % 6
    <fs, a d>2. r4 | % 7
    <fs b e>1 \break | % 8
    << { <ds e b'>1 } \\ { r2 r4 fs4 } >> | % 9
    << { < gs cs e >1 } \\ { r2 r4 fs4 } >> | % 10
    << { < gs cs e >1 } \\ { r2 r4 fs4 } >> | % 11
    << { < a cs e >1 } \\ { r2 r4 fs4 } >> | % 12
    << { < fs b e >1 } \\ { r2 r4 a4 } >> | % 13
    << { < gs b ds >1 } \\ { r2 r4 a4 } >> | % 14
    < a cs e >1 | % 15
    < fs b ds >1 | % 16
  }
  \new Staff = "down" {
    \clef bass
    \key e \major
    \partial 8 fs,,8 | % anacrusis
    gs4. gs'8~ gs4 ds4 | % 1
    e4. b8~ b2 | % 2 
    d2~ d4 a' | % 3 
    e2. a,8 fs| % 4
    gs4. gs'8~ gs4 ds4 | % 5
    e4. b8~ b2 | % 6
    b'4. d,8~ d4 e4 | % 7
    gs2 e2 | % 8
    gs,2 gs'2 | % 9
    a,2 a'2 | % 10
    b,2 b'2 | % 11
    cs,2 cs'2 | % 12
    ds,2 ds'2 | % 13
    e,2 e'2 | % 14
    << { r4 r8 b4. bf8 } \\ { fs1 } >> | % 15
    a1 | % 16
    \bar "|."
  }
>>
}

\score {
  \music
  \layout {}
  \midi {
    \tempo 4 = 110
    \context {
      \Score
      midiChannelMapping = #'instrument
    }
  }
}