---
name: supercollider-help-files
description: Keeps SuperCollider class files (.sc) and their help files (.schelp) in sync, and enforces project conventions for schelp tag casing and class references. Use when editing, creating, renaming, or deleting any SuperCollider class in this repository, or when authoring or updating any .schelp file.
---

# SuperCollider help-file conventions

This repo treats every public-facing SuperCollider class as having a paired help file. Help files must reflect the current state of the class.

## When to apply

Apply whenever you:

- create, rename, or delete an `.sc` class
- add, rename, remove, or change the signature of a public method on an `.sc` class
- change the meaning of a class method argument
- otherwise change behaviour that the help file describes

If the change does not affect anything the help file says, no help-file update is needed.

## File layout

Help files live alongside the class they document, under `HelpSource/Classes/<ClassName>.schelp`. Example:

- `Classes/Sequencers/PianoRoll.sc`
- `Classes/Sequencers/HelpSource/Classes/PianoRoll.schelp`

When renaming a class, rename the help file to match. When deleting a class, delete the help file.

## Tag casing

All schelp tags must be uppercase. Use the existing tag names exactly:

- `TITLE`, `SUMMARY`, `CATEGORIES`, `RELATED`
- `DESCRIPTION`, `DISCUSSION`, `NOTE`
- `CLASSMETHODS`, `INSTANCEMETHODS`, `METHOD`, `ARGUMENT`, `PRIVATE`
- `EXAMPLES`, `CODE`, `TELETYPE`, `LINK`, `STRONG`, `EMPHASIS`, `TABLE`

Never write `title::`, `Title::`, `link::`, etc.

## References to classes

- **Other classes**: always use `LINK::Classes/OtherClass::`. Never refer to another class as a bare word or with `TELETYPE`.
- **The current class** (the one this help file documents): always use `TELETYPE::CurrentClass::`. Never `LINK` a class to itself.

### Example (in `PianoRoll.schelp`)

```
TITLE:: PianoRoll
SUMMARY:: Piano-roll view for MIDI recording and note editing.
RELATED:: Classes/PianoRollNote, Classes/SequencerGui

DESCRIPTION::
TELETYPE::PianoRoll:: shows recorded notes as a piano roll. It uses
LINK::Classes/PianoRollNote:: to represent each note, and is embedded in
LINK::Classes/SequencerGui::.
```

Note: `RELATED::` values are class paths only (`Classes/Foo`), not wrapped in `LINK::...::`.

## Method signatures must match

When updating a method on a class, update the matching `METHOD::` entry so that:

- the method name in the heading matches the method in the `.sc` file
- every `ARGUMENT::` matches an argument of the actual method, in order
- arguments removed from the method are also removed from the help file
- new arguments are added with at least a one-line description

If a method becomes private (no longer part of the public API), move it into the `PRIVATE::` line instead of leaving a stale `METHOD::` block.

## Stale or placeholder content

Do not leave `(describe argument here)` / `(describe method here)` placeholder text in a help file you have just touched. Replace it with a real one-line description, or remove the entry if the thing it documents no longer exists.
