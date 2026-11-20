---
name: subsystem-architecture
description: >-
  Judges whether a named subsystem in this repository would benefit from
  rearranging its object model. Use when the user asks to analyse the
  architecture of a subsystem, review how objects are composed or how data
  passes between them, or decide whether classes such as Sequencer and
  SequencerGui should be rearranged. Analysis only; do not change code as
  part of the analysis.
---

# Subsystem architecture

Decide whether a named subsystem would benefit from rearranging. The default verdict is to leave it.

Don't make a change unless there is a strong case for it. We don't refactor for its own sake.

This skill analyses. It does not edit code, reorder class members, or rename types. A rearrangement the user accepts is a separate request. Member order inside a class is covered by `supercollider-class-ordering` and is not an architectural finding.

## What to analyse

Name the subsystem first, including the classes inside it and the neighbours it talks to. Do not follow neighbours into the rest of the repository.

Then:

- analyse the object model / composition hierarchy
- look at how data is passed between the objects
- determine if there is excessive movement of data between layers of the object model that could be reduced by rearranging the objects

Composition means an object constructs or holds another. An argument that is used and not stored is a collaboration, not a layer. A callback the parent installs is part of that composition.

In this repository, a `pr` method is a naming convention. Another class can call it. That call is real coupling.

## How to judge movement

Trace facts, not call graphs. For each value that crosses an object boundary, record where it is born and who uses it. An object that only forwards it does not use it.

Judge a crossing with these, in order. Stop once the verdict is clear.

1. **Connascence** (Page-Jones). Two objects are connascent when a change to one requires a change to the other. Strength runs from name, to type, to meaning, to position, to algorithm, then to values and identity. The same connascence inside one class is fine. Strong connascence spread across distant objects is the thing a move can fix, by weakening it or bringing it closer.
2. **Tramp data.** A value passed through an object that does not use it. One new argument on an action that object already performs is not excessive.
3. **Feature envy** (Fowler). A method that uses another object's state more than its own. The candidate move is to put the method with that state.
4. **Shotgun surgery** (Fowler). The same knowledge must be edited in several classes. A feature that touches several files is not shotgun surgery when each file owns a different decision, such as the gesture, the lookup, the view, and playback.

Coupling between objects (Chidamber and Kemerer) and efferent coupling (Martin) are comparisons inside the subsystem: which class knows the most others, and whether that knowledge is pass-through. Do not score every class. Do not use inheritance depth, lack of cohesion, cyclomatic complexity, or distance from the main sequence as a reason to rearrange this code. Inheritance here is shallow, and splitting a class is a different question from data movement.

## Strong case

A rearrangement has a strong case only when all of these are true:

- The fact or decision is named, with the path it travels.
- An intermediate object does not use it, or the same derivation is repeated in more than one place.
- A specific move shortens that path: relocate a method, store the fact where it is used, or stop forwarding it.
- The move adds no type whose only job is to carry the old arguments.
- Leaving it has a named cost. The next change of this same fact edits the pass-through again, or a layer must know something that is not its job.

If any item fails, the verdict is to leave it.

## Report

Lead with the verdict. Then:

- The boundary: classes inside, neighbours left outside.
- The composition tree: who holds whom.
- The crossings that matter, each with where the fact is born and who only forwards it.
- The one rule above that decides the verdict.
- When the verdict is to leave it, say what was considered and why it fails the bar. Do not propose a design.
- When the verdict is a strong case, the smallest move and the cost of leaving it. No alternative designs.

## Examples

**Leave it.** Opening a sequence passes the synth MIDI channel from `Arranger` through `SequencerGui` and `PianoRollContainer` into `PianoRoll`, once, on the open action. The channel is born on the synth. The roll is what plays it. The objects in between already perform the open. One argument. Not a reason to rearrange them.

**Strong case, and still a small move.** Suppose the arranger, the drum page, and the synth desk each took a sequence id, called `Sequencer.prSequenceAt`, and then read `Sequence.synthId` to choose a hardware port. A change to that choice would be edited in each of them. Look the port up once, on `Sequencer`, and keep the result on the object that opens the port. That removes the repeated derivation. It does not add a type, and it does not rearrange those screens around actions they already perform.
