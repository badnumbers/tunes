---
name: supercollider-language-gotchas
description: Documents non-obvious SuperCollider language semantics and common mistakes in this repo. Use when writing or debugging .sc code, GUI keyDown/keyUp handlers, closures assigned in init, early returns (^), Char values, Event dictionaries, nil-safe lookups, or unit tests that synthesize keyboard events.
---

# SuperCollider language gotchas

SuperCollider parses and evaluates several constructs differently from languages like JavaScript or Python. Apply these rules when editing `.sc` files in this repository.

## Early return (`^`) binds tightly

### `^this.method(...)` returns `this`, not the method result

The caret applies to `this` alone; the method call runs as a side effect.

```supercollider
// WRONG — caller receives the router instance
^this.prReturnValueFromResult(result);

// RIGHT
^(this.prReturnValueFromResult(result));

// ALSO RIGHT
var returnValue;
returnValue = this.prReturnValueFromResult(result);
^returnValue;
```

### Bare `methodName(arg)` inside an instance method is **not** `this.methodName(arg)`

If the first argument is named `char`, `handleKeyDown(char, ...)` is parsed as `char.handleKeyDown(...)`.

```supercollider
// WRONG — sends handleKeyDown to the Character
result = handleKeyDown(char, modifiers, unicode, keycode, key);

// RIGHT
result = this.handleKeyDown(char, modifiers, unicode, keycode, key);
```

Same rule for `prUnmatchedKeyDown`, `prReturnValueFromResult`, and other methods on `this`.

## `^` inside closures defined in `init` returns from `init`

A `^true` / `^false` / `^nil` inside a `Function` assigned in `init` (for example `keyDownAction_({ ... })`) returns from **`init`**, not from the closure.

```supercollider
// WRONG
.keyDownAction_({ |view, char, ...|
    if (handled, { ^true });
    ^false;
});

// RIGHT — last expression is the closure's return value
.keyDownAction_({ |view, char, ...|
    var keyHandled = false;
    if (handled, { keyHandled = true });
    keyHandled;
});
```

## `Char` cannot be constructed with `Char.new`

`Char *new` calls `shouldNotImplement`. Create characters with literals, `Integer.asAscii`, or class constants.

| Intent | Use |
|--------|-----|
| Printable character | `$s`, `$1`, `$g` |
| From ASCII code | `49.asAscii` (must be 0–255) |
| Carriage return | `Char.ret` or `$\r` |
| No typed character (arrows, Ctrl+chord, modifier keys) | `nil` |

```supercollider
// WRONG
Char.new(0)
Char.new(49)

// RIGHT
nil
$1
Char.ret
```

In help text and tests for `View` key actions, document and pass `nil` when the platform provides no character — not `Char.new(0)`.

Named constants for keyboard test/event fields live in `ViewKeyModifier`, `ViewQtKey`, `ViewKeycode`, and `ViewUnicode`.

## Event / Dictionary access and keys

- Prefer **getter** syntax on Events: `result.consume`, not `result[\consume]` (bracket form can send `at` to the wrong receiver if `result` is not an Event).
- Avoid Event keys that collide with reserved names. This repo uses `isHandled:` instead of `handled:`.
- Guard chained lookups when an intermediate value may be `nil`:

```supercollider
// WRONG — nil.at(\action) if no handlers registered for context
handler = prHandlers[prActiveContext][action];

// RIGHT
contextHandlers = prHandlers[prActiveContext];
if (contextHandlers.notNil, {
    handler = contextHandlers[action];
    ...
});
```

A binding match may occur with **no subscriber** registered; invoking a handler must be a no-op, not an error.

## Logic and collections

- **`||` does not short-circuit** in SuperCollider. Do not write `x.isNil || x.isEmpty` expecting `isEmpty` to be skipped when `x` is nil. Use separate branches or `if`.

```supercollider
// WRONG — may message nil
if (requiredModifiers.isNil || requiredModifiers.isEmpty, { ^true });

// RIGHT
if (requiredModifiers.isNil, { ^true });
if (requiredModifiers.size == 0, { ^true });
```

- Use **`every`**, not `all`, on collections (`Array`, etc.).

## Unit tests

- Do not run full `sclang` class-library compiles in CI-style shell scripts to verify small changes — it is slow and often hangs headless. Prefer asking the user to run `UnitTest.run(...)` in the IDE, or run a narrowly scoped check.
- When synthesizing `View` keyboard events in tests, match real field usage: `nil` char, `ViewKeyModifier.ctrl`, `ViewQtKey.m`, `ViewKeycode.leftArrow`, etc. See `SequencerKeyRouterUnitTests.sc`.

## Related skills

- `.schelp` markup: `supercollider-help-files`
- Class member order: `supercollider-class-ordering`
- Constructor validation: `supercollider-parameter-validation`
