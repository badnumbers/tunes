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
- `DESCRIPTION`, `CLASSMETHODS`, `INSTANCEMETHODS`, `EXAMPLES`
- `SECTION`, `SUBSECTION`, `SUBSUBSECTION`
- `METHOD`, `ARGUMENT`, `RETURNS`, `DISCUSSION`, `PRIVATE`
- `NOTE`, `WARNING`
- `CODE`, `TELETYPE`, `LINK`, `STRONG`, `EMPHASIS`, `TABLE`

Never write `title::`, `Title::`, `link::`, etc.

## Section structure

Top-level class-reference sections (level 1) are only:

- `DESCRIPTION::`
- `CLASSMETHODS::`
- `INSTANCEMETHODS::`
- `EXAMPLES::`

Under `DESCRIPTION::`, use extra paragraphs, `NOTE::`, `WARNING::`, `TABLE::`, or a named `SUBSECTION::` for longer material. Do **not** put `DISCUSSION::` directly under `DESCRIPTION::`.

### `DISCUSSION::` is method-only

Per SuperCollider `SCDocSyntax.schelp` and the `SCDoc.y` grammar, `DISCUSSION::` is parsed only inside a `METHOD::` block (after the method summary, optional `ARGUMENT::` / `RETURNS::` sections). It is **not** valid in `DESCRIPTION::`, `CLASSMETHODS::`, or `INSTANCEMETHODS::` on its own.

**Wrong** (parse error — `DISCUSSION::` is not part of the `DESCRIPTION` body grammar):

```
DESCRIPTION::
Short intro.

DISCUSSION::
More detail and a TABLE:: ...
```

**Right** — keep class-level detail in `DESCRIPTION::` or a subsection:

```
DESCRIPTION::
Short intro.

SUBSECTION:: More detail
Extra paragraphs, TABLE::, NOTE::, etc.
```

**Right** — method-specific discussion after arguments and returns:

```
METHOD:: handleKeyDown
Short summary.

ARGUMENT:: freq
...

RETURNS::
An UGen.

DISCUSSION::
Extended notes and CODE:: examples for this method only.
```

Valid `DISCUSSION::` examples in this repo: `Sequencer.schelp` (under `METHOD::addGlobalPreKeys`, etc.), `Synthesizer.schelp` (under `METHOD::addUpdateAction`).

## Inline tags (SCDoc)

SCDoc only supports these **inline** tags (see SuperCollider `HelpSource/Reference/SCDocSyntax.schelp`):

- `LINK::` — hyperlink to another help topic
- `CODE::` — inline code (symbols, methods, literals)
- `TELETYPE::` — monospaced name of the class this help file documents
- `STRONG::`, `EMPHASIS::`, `SOFT::`, `MATH::`, `ANCHOR::`

**There is no `EVENT::` tag.** Do not invent tags such as `EVENT::Dictionary::` or `EVENT::Event::`; SCDoc will error.

### `CODE::` vs `LINK::` vs `TELETYPE::`

| Intent | Use | Example |
|--------|-----|---------|
| Symbol, method, literal, or key name | `CODE::` | `CODE::\isHandled::`, `CODE::#-handleKeyDown::`, `CODE::true::` |
| Another SuperCollider class | `LINK::Classes/ClassName::` | `LINK::Classes/Dictionary::`, `LINK::Classes/Function::` |
| The class this `.schelp` file documents | `TELETYPE::` | `TELETYPE::SequencerKeyRouter::` |

When a method returns a key-value structure (for example `(isHandled: true, consume: false)`), refer to the **class** with `LINK::Classes/Dictionary::` or `LINK::Classes/Event::` as appropriate — not `EVENT::Dictionary::`. Document individual keys with `CODE::\keyName::`.

**Wrong:**

```
Returns an EVENT:: Dictionary:: with CODE::\isHandled:: keys.
```

**Right:**

```
Returns a LINK::Classes/Dictionary:: with CODE::\isHandled:: and CODE::\consume:: keys.
```


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

Never use a bare class name (`Dictionary`, `Function`) or a made-up tag (`EVENT::`) where `LINK::Classes/...::` is required.

For `Char` and keyboard-event test values in help examples, see `supercollider-language-gotchas`.

## Method block tag order

Inside a `METHOD::` block, SCDoc enforces this order (see `SCDocSyntax.schelp` and `SCDoc.y` `methodbody`):

1. Method summary (prose)
2. `ARGUMENT::` — one per parameter, in declaration order
3. `RETURNS::` — return value description
4. `DISCUSSION::` — optional extended notes or examples

`RETURNS::` and `DISCUSSION::` must **not** appear before `ARGUMENT::` sections. Putting `RETURNS::` first causes a parse error.

**Wrong:**

```
METHOD:: handleKeyDown
Evaluates a key-down event.

RETURNS::
An Event with \isHandled and \consume.

ARGUMENT:: char
...
```

**Right:**

```
METHOD:: handleKeyDown
Evaluates a key-down event.

ARGUMENT:: char
...

RETURNS::
An Event with \isHandled and \consume.
```

If a method has no arguments, `RETURNS::` may follow the summary directly.

## Method signatures must match

When updating a method on a class, update the matching `METHOD::` entry so that:

- the method name in the heading matches the method in the `.sc` file
- every `ARGUMENT::` matches an argument of the actual method, in order
- arguments removed from the method are also removed from the help file
- new arguments are added with at least a one-line description

If a method becomes private (no longer part of the public API), move it into the `PRIVATE::` line instead of leaving a stale `METHOD::` block.

## Member order

Help files use separate `CLASSMETHODS::` and `INSTANCEMETHODS::` sections. The `.sc` class uses one merged method list. See the supercollider-class-ordering skill for how to order `METHOD::` entries within each help section so they match the relative order of that method type in the class file.

## Stale or placeholder content

Do not leave `(describe argument here)` / `(describe method here)` placeholder text in a help file you have just touched. Replace it with a real one-line description, or remove the entry if the thing it documents no longer exists.
