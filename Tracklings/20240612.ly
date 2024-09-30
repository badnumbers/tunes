\version "2.20.0"
\language "english"

\header {
  title = "20240612"
}

music = \relative
{
\new PianoStaff
<<
  \new Staff = "up" {
    \accidentalStyle forget
    as''16 fs as,8 r2 fs16 as ds as' | % 1
    gs16 ds fs,8 r4 r2 | % 2
    as'16 fs as,8 r2 fs16 as ds as' | % 3
    gs16 ds fs,8 r4 r2 | % 4
    ds'4 cs4 fs,8 gs as cs | % 5
    ds4 c cs as | % 6
    b gs as8 gs fs f | % 7
    ds4 gs fs2 | % 8
    as8 gs fs f ds4 gs8 fs16 gs | % 9
    fs1 | % 10
    as4~ as8 d,8 as'4 fs | % 11
    e4 fs8 gs as4 b4 | % 12
    cs4 a b fs | % 13
    gs f e cs | % 14
    
  }
  \new Staff = "down" {
    \accidentalStyle forget
    \clef bass
    ds,16 fs cs' fs, ds fs cs' fs, ds fs cs' fs, ds fs cs' ds| % 1
    ds, gs b gs ds gs b gs ds gs b gs ds gs b ds| % 2
    ds, fs cs' fs, ds fs cs' fs, ds fs cs' fs, ds fs cs' ds| % 3
    ds, gs b gs ds gs b gs ds gs b gs ds fs b ds| % 4
    fs, as f' as, f as ds as d, fs cs' fs, cs fs as fs | % 5
    c ds as' ds, a c fs c as ds fs as cs, fs as cs | % 6
    d, fs cs' fs, b, ds gs ds cs fs as fs c ds as' ds, | % 7
    b ds gs ds cs f b f ds fs as c cs4 | % 8
    cs,16 fs as fs c ds as' ds, b ds as' ds, d fs cs' fs, | % 9
    as, cs fs as cs4~ cs2 | % 10
    d,16 fs as fs d fs as fs cs fs as fs b, d gs d | % 11
    cs e b' e, cs e b' e, d fs cs' fs, e gs d' gs, | % 12
    a, cs e gs cs, e a cs d, fs b d b, d fs b | % 13
    d, fs a d cs, f a cs cs, e gs b a, cs e a'
  }
>>
}

\score {
  \music
  \layout {}
  \midi {
    \tempo 4 = 50
    \context {
      \Score
      midiChannelMapping = #'instrument
    }
  }
}