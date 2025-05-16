\version "2.20.0"
\language "english"

\header {
  title = "20250119"
  subtitle = "G♭"
}

\markup "REV2 F1 P22 Prrg Nexus"

\new GrandStaff <<
  \new Staff \with { instrumentName = "REV2" } \relative c''' {
    \key gf \major
    \time 7/4
    << { r4 gf4 r4 af4 r4 gf4 } \\ { r4. bf,8 r4. df8 r4. bf8 } >> r4 | % 1
    << { r4 gf'4 r4 af4 r4 gf4 } \\ { r4. bf,8 r4. df8 r4. bf8 } >> r4 | % 2
    \time 4/4
    << { r4 f'4 r4 af4  } \\ { r4. af,8 r4. cf8 } >> | % 3
    << { r4 bf'4 r4 bf4  } \\ { r4. ef,8 r4. df8 } >> | % 4
  }
  \new Staff \with { instrumentName = "REV2" } \relative c' {
    \key gf \major
    \clef bass
    << { r8 bf4. r8 bf4. r8 bf4. } \\ { bf,2 bf2 bf2 } >> r4 | % 1
    << { r8 bf'4. r8 bf4. r8 bf4. } \\ { bf,2 bf2 bf2 } >> r4 | % 2
    << { r8 af'4. r8 af4. } \\ { af,2 af2 } >> | % 3
    << { r8 gf'4. r8 ef4. } \\ { gf,2 ef2 } >> | % 4
  }
>>