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
<<
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
    piece = "Intermediate
 section"
  }
<<
  \new Staff \with { instrumentName = "Hydrasynth" } \relative c' {
    \key ef \minor
    r8 gf16 af bf8. af16 gf af df4. | % 1
    r8 gf,16 af bf4 f'16 gf bf4. | % 2
    r8 gf,16 af bf8. af16 gf af ef'4. | % 3
    r8 gf,16 af bf4 g'16 af bf8 ef,4 | % 4
  }
  \new Staff \with { instrumentName = "REV2" } \relative c''' {
    \key ef \minor
    \ottava 1
    \repeat volta 2 {
      <bf~ af'>1 | % 1
      <bf~ ef>1 | % 2
      <bf~ af'>1 | % 3
      <bf ef>1 | % 4
    }
    <cf~ af'>1 | % 9
    <cf~ ff>1 | % 10
    <cf~ af'>1 | % 11
    <cf~ df>1 | % 12
    <cf~ af'~>1 | % 13
    <cf af'>1 | % 14
    <af~ ff'~>1 | % 15
    <af ff'>1 | % 16
    <cf~ af'~>1 | % 17
    <cf af'>1 | % 18
    <af~ f'~>1 | % 19
    <af f'>1 | % 20
  }
  \new Staff \with { instrumentName = "REV2"
  } \relative c' {
    \key ef \minor
    \repeat volta 2 {
      <gf gf'>1 | % 1
      <af af'>1 | % 2
      <bf bf'>1 | % 3
      <df df'>1 | % 4
    }
    <ff ff'>1 | % 9
    <df df'>1 | % 10
    <ff ff'>1 | % 11
    <gf gf'>1 | % 12
    <ff ff'>1 | % 13
    <ef ef'>1 | % 14
    <df~ df'~>1 | % 15
    <df df'>1 | % 16
    <ff ff'>1 | % 17
    <ef ef'>1 | % 18
    <df df'>1 | % 19
    <f f'>1 | % 20
  }
>>
}

\score {
  \header {
    piece = "Gentle section"
  }
<<
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
  \new Staff \with { instrumentName = "REV2"
  } \relative c' {
    \key ef \minor
    <gf' ef'>1 | % 1
    << { f' } \\ { af,2 bf } >> | % 2
    <cf gf'>1 | % 3
    << { af' } \\ { ef2 df } >> | % 4
    <gf, ef'>1 | % 5
    << { ef'2 df } \\ { f,1 } >> | % 6
    <ef cf'>2 <f df'> | % 7 
    <gf ef'> <af f'>| % 8
    
  }
>>
}

\score {
  \header {
    piece = "Interesting connecting section"
  }
<<
  \new Staff \with { instrumentName = "REV2" } \relative c''' {
    \key ef \minor
    \ottava 1
    <af df gf>1 | % 1
    << { ef'1 } \\ { <gf, bf>2 <ef af> }  >> | % 2
    <af df gf>1 | % 3
    << { ef'1 } \\ { <gf, bf>2 <ef af> }  >> | % 4
    <ef af ef'>1 | % 5
    << { ef'2 df } \\ { <f, bf>1 } >> | % 6
    <ef af ef'>1 | % 7
    <f bf ef>2 <bf d f> | % 8
    <bf~ df~ gf>1 | % 9
  }
  \new Staff \with { instrumentName = "REV2"
  } \relative c' {
    \key ef \minor
    <af af'>2. <bf bf'>4 | % 1
    <cf cf'>1 | % 2
    <af af'>2. <bf bf'>4 | % 3
    <cf cf'>1 | % 4
    <ff, ff'>2. <f f'>4 | % 5
    <gf gf'>2 <bf bf'>2 | % 6
    <ff ff'>2. <f f'>4 | % 7
    <gf gf'>4 <bf bf'> <c c'> <d d'> | % 8
    <ef ef'>1 | % 9
  }
>>
}

\score {
  \header {
    piece = "Recovery"
  }
  \new Staff \with { instrumentName = "REV2"
  } \relative c' {
    \key ef \minor
    <df bf'>2 <ef cf'>2 | % 1
    <f df'> <gf ef'> | % 2
    <bf gf'> <af f'> | % 3
    <f df'> <g ef'>\fermata | % 4
    <df bf'> <ff df'> | % 5
    <gf ef'> <af f'> | % 6
    <bf g'>1 | % 7
  }
}

\score {
  \header {
    piece = "Alien and mysterious"
  }
<<
  \new Staff \with { instrumentName = "REV2" } \relative c''' {
    \key ef \minor
    \ottava 1
    \repeat volta 2
    {
      << { <df~ g~>1 } \\ { g,2 a } >> | % 1
      << { <df g~>1 } \\ { g,2 a } >> | % 2
      << { <ef'~ g~>1 } \\ { g,2 a } >> | % 3
      << { <ef' g>1 } \\ { g,2 a } >> | % 4
    }
    \repeat volta 2
    {
      << { <df~ g~>1 } \\ { g,2 a } >> | % 9
      << { <df g~>1 } \\ { g,2 a } >> | % 10
      << { <ff'~ g~>1 } \\ { g,2 a } >> | % 11
      << { <ff' g>1 } \\ { g,2 a } >>\break | % 12
    }
    << { <df~ g~>1 } \\ { g,2 a } >> | % 13
    << { <df g~>1 } \\ { g,2 a } >> | % 14
    << { <ff'~ g~>1 } \\ { g,2 a } >> | % 15
    << { <ff' g>1 } \\ { g,2 a } >> | % 16
    << { <df~ ff~>1 } \\ { g,2 a } >> | % 17
    << { <df ff>1 } \\ { g,2 a } >> | % 18
    << { <ef'~ g~>1 } \\ { a,2 cf } >> | % 19
    << { <ef g>1 } \\ { a,2 cf } >>\break | % 20
    <df~ f~ a>1^"Big and dramatic"\fermata | % 21
    <df f af>1^"Back to a major place" | % 22
  }
  \new Staff \with { instrumentName = "REV2"
  } \relative c' {
    \key ef \minor
    \repeat volta 2
    {
      <ef~ ef'~>1 | % 1
      <ef~ ef'~>1 | % 2
      <ef~ ef'~>1 | % 3
      <ef ef'>1 | % 4
    }
    \repeat volta 2
    {
      <ff~ ff'~>1 | % 9
      <ff~ ff'~>1 | % 10
      <ff~ ff'~>1 | % 11
      <ff ff'>1 | % 12
    }
    <df~ df'~>1 | % 13
    <df df'>1 | % 14
    <cf~ cf'~>1 | % 15
    <cf cf'>1 | % 16
    <g~ g'~>1 | % 17
    <g g'>1 | % 18
    <gf~ gf'~>1 | % 19
    <gf gf'>1 | % 20
    <ef ef'>1 | % 21
    <df df'>1 | % 22
  }
>>
}