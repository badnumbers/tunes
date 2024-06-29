\version "2.20.0"
\language "english"

\header {
  title = "Another world is possible"
}

\markup "REV2 U2 P31: Gorgeous Brass BN."
\markup "Hydrasynth A013 SpiralBallad PS."
\markup "E♭ minor"
\markup ""

\score {
  \header {
    piece = "Main section"
  }
\repeat volta 2
\new GrandStaff <<
  \new Staff \with { instrumentName = "Hydrasynth" } \relative c'' {
    \key ef \minor
    gf16 af bf8 ef,4~ ef2~  | % 1
    ef2~ ef8 gf ef'16 df bf8  | % 2
    cf4. af8\staccato af2 | % 3
    bf4. f8\staccato f2 |  % 4
    gf4. ef8\staccato ef2 | % 5
    r8 af,16 bf df ef~ ef8~ ef2 | % 6
    \alternative {
      \volta 1 {
        r2 r4 r16 bf df ef | % 7
        gf2 f | % 8
      }
      \volta 2 {
        r2 r4 r16 bf, df ef | % 7
        gf2 f | % 8
      }
    }
  }
  \new Staff \with { instrumentName = "REV2" } \relative c''' {
    \key ef \minor
    \ottava 1
    <bf~ df~ gf>1 | % 1
    <bf df af'>1 | % 2
    <ef af cf > | % 3
    <df f bf> | % 4
    <bf~ df~ gf> | % 5
    <bf df f> | % 6
    \alternative {
      {
      \volta 1 <gf cf ef> | % 7
      << {gf'2 f} \\ { <af, cf>1 } >> | % 8
      }
      {
      \volta 2 <cf ef af> | % 7
      <af cf gf'>2 << { f'2 } \\ { af,2 } \\ { ef'4 df } >> | % 8
      }
    }
  }
  \new Staff \with { instrumentName = "REV2" } \relative c' {
    \key ef \minor
    <ef ef'>1 | % 1
    <f f'> | % 2
    <gf gf'>1 | % 3
    <af af'> | % 4
    <ef ef'>1 | % 5
    <df df'> | % 6
    \alternative {
      \volta 1
      {
        <cf cf'> | % 7
        <af af'>2 <df df'> | % 8
      }
      \volta 2
      {
        <cf cf'>2. <bf bf'>4 | % 7
        <af af'>2 <df df'> | % 8
      }
    }
  }
>>
}

\score {
  \header {
    piece = "Gentle section"
  }
\new GrandStaff <<
  \new Staff \with { instrumentName = "REV2" } \relative c''' {
    \key ef \minor
    \ottava 1
    <bf~ df~ gf>1 | % 1
    <bf df af'>1 | % 2
    <ef af cf > | % 3
    <df f bf> | % 4
    <bf~ df~ gf> | % 5
    <bf df f> | % 6
    <gf cf ef> | % 7
    << {gf'2 f} \\ { <af, cf>1 } >> | % 8
  }
  \new Staff \with { instrumentName = "REV2" } \relative c' {
    \key ef \minor
    <gf' ef'>1 | % 1
    << { f' } \\ { af,2 bf } >> | % 2
    <cf gf'>1 | % 3
    << { af' } \\ { ef2 df } >> | % 4
    <gf, ef'>1 | % 5
    << { ef'2 df } \\ { f, } >> | % 6
    <ef cf'>2 <f df'> | % 7 
    <gf ef'> <af f'>| % 8
  }
>>
}