# Undo

Each piano roll and each drum programmer keeps its own undo history. One command execution is one step. The editor owns the history, not the command prompt, because the Delete key and other shortcuts call commands directly. Both the prompt and those shortcuts go through one editor method that runs the command and, when there is something to undo, records the step.

The history entry holds the same note or drum-step objects the command received. They are the anchor. It also holds an `IdentityDictionary` from each of those objects to the values the command is about to overwrite. The command reads those values before it writes. Only objects it actually changes are entered. A `nil` event key is stored as `nil`, so undo can tell "this key was absent" from "this key was zero". If the dictionary is empty, the editor does not push a step.

Undo restores that dictionary. It does not run the forward command again, and it does not recompute anything from the original arguments. Those arguments are not enough: a MIDI-note target does not remember the previous note number, legato's next onset depends on the other notes at that moment, and a snap does not remember where an off-grid note used to be.

## The image is enough

`restore` is called later with that dictionary and nothing else. The command object stored beside it only interprets the image. It must not remember the arguments. `SetCommand` is reused for every key, so a known key stores the dispatched command, the one whose `restore` understands the snapshot. An unknown key stores `SetCommand` itself. Each entry in that snapshot names the event key and the previous value. A missing key is stored as `nil`, so undo can remove it again.

A value that the forward write mutates in place has to be copied into the snapshot. An amplitude change keeps the same drum hit and writes a new velocity onto it, so the snapshot keeps the old velocity as well as the hit.

The editor passes the before-image into `afterImage`. That is how the after snapshot covers the same objects once their old values are gone. A command that can find those objects from the arguments alone can ignore the extra dictionary.

When delete records a step, it uses the same membership restore as amplitude zero.

## Soft delete

Deleting a piano-roll note is a membership change. The history entry's note list is the set that left the roll. The note object, its `PlayableNote`, its event, and its callbacks stay intact. The note is taken out of the roll's list. Its view is removed and cleared, because the Qt view is the expensive part and undo creates a new one through the note's existing view function. Setting amplitude to zero is this same soft delete.

A drum step stays in the grid. Clearing a hit only drops the `PlayableNote` inside that step. The history key is the step, and the stored value is the hit that was cleared.

## Memory

SuperCollider has no deep size-of. The note data in a history entry is small. The views must not be retained. The stack is capped at a fixed number of entries, on the order of 50 to 100, which bounds the case that does grow: repeatedly deleting a large take, where each entry keeps those notes alive until it falls off the end.

Dropping an entry is enough for collection. The collector traces from roots and collects cycles. A note that is still on the roll stays. A soft-deleted note referenced only by that entry is collected, along with its playable note, event, and callbacks. Collection is incremental, so this happens on a later pass rather than inside the call that dropped the entry.

## Redo branch

Undo walks back through the stack. Those steps stay available for redo. If the user goes back three steps and then makes a new edit, those three steps are discarded. A later edit cannot be branched on top of an undone prefix. That discarded branch drops its dictionaries in the same way as an entry that falls off the cap.

## Turning it on

Ctrl+Z undoes. Ctrl+Shift+Z and Ctrl+Y redo. Each piano roll and the drum programmer already own an undo history, and the command prompt plus the part and delete keys run through the editor so a step can be recorded. The keys do nothing until a command's `beforeImage` returns a non-empty dictionary. Backward behaviour is added one command at a time. The prompt runs the command object it was given. `set key amp`, `set key legato`, `set key part`, and `set key midinote` record that dispatched command's step. `set key foo` records a `SetCommand` step for the event key. `left`, `right`, and `snap` record each moved note's start and stop time. `transpose` records each moved note's note number and slides the view back with `transpose`. `amp` is recorded in both the piano roll and the drum programmer. Amplitude `0` restores a removed piano-roll note, including a new view, and restores a cleared drum hit on its step. The Delete key records a `DeleteCommand` step and restores the note through that same membership restore.

`AddCommand` inserts a finished piano-roll note. Note-on creates that note and holds it aside. Note-off writes the stop time, then the command inserts the same object. Undo removes it. Redo inserts it again. The times stay on the note. A later mouse gesture can finish its own note and call the same command. A drum-square left drag records one amplitude step: the image from before the first write, and the image after mouse-up. A right-click that clears a hit records one step the same way. `AmpCommand.restore` applies either image. The slider writes go straight to the step, and `amp` from the prompt does not record them again.

Releasing a drag on an existing piano-roll note records one `DragCommand` step: the start time, stop time, and MIDI note number from before the move, and the same three after it. The view follows the pointer during the drag and is not recorded. A release that leaves the note in place records nothing. `DragCommand` is not on the prompt. Moving loop markers stays outside this history.
