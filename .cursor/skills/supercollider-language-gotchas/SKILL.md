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

## View keyDown/keyUp return values

When a `keyDownAction` or `keyUpAction` is set, the handler's return value controls whether the widget's native C++ behaviour runs:

| Return | Effect |
|--------|--------|
| `nil` | Native widget behaviour runs (e.g. `TextField` inserts characters) |
| `true` | Event handled; native behaviour **skipped**; propagation stopped |
| `false` | Native behaviour **skipped**; event propagates to parent |

Returning `false` thinking it means "not handled" is a common bug — it still bypasses native behaviour. On a `TextField`, that blocks typing entirely.

```supercollider
// WRONG — false still bypasses TextField text entry
.keyDownAction_({ |view, char, modifiers, unicode, keycode, key|
    var keyHandled = false;
    if (keycode == ViewKeycode.downArrow, { this.prMoveSelection(1); keyHandled = true });
    keyHandled;
});

// RIGHT — nil for unhandled keys, true only to consume specific keys
.keyDownAction_({ |view, char, modifiers, unicode, keycode, key|
    var keyHandled = nil;
    if (keycode == ViewKeycode.downArrow, {
        this.prMoveSelection(1);
        keyHandled = true;
    });
    keyHandled;
});
```

See `View.schelp` ("Key and mouse event processing") for the full propagation rules.

## Adding views to a `LineLayout` (`HLayout` / `VLayout`) via `add` vs array syntax

`LineLayout.new` parses items wrapped in arrays (e.g. `[view, s: 1]`) to extract stretch and alignment. However, `LineLayout#-add` and `LineLayout#-insert` take stretch and alignment as **explicit method arguments**:

```supercollider
// WRONG — layout.add treats the Array itself as the item
layout.add([textField, s: 1]);

// RIGHT — pass arguments directly
layout.add(textField, stretch: 1);
// or simply
layout.add(textField);
```

Passing `[view, s: 1]` to `layout.add(...)` converts the array to a layout element via `asLayoutElement`, which fails to render the widget properly.

## Absolute `bounds` on a layout-managed parent are ignored

If a parent `View` has a `Layout` installed, constructing a child with `View(parent, Rect(...))` inserts that child into the layout. The layout then owns placement: setting `bounds.left` / `bounds.top` has no lasting effect (often the child appears as if `left` were `0`).

```supercollider
// WRONG — parent layout adopts the overlay; Rect left is ignored
root.layout = VLayout(inputRow, nil);
overlay = View(root, Rect(50, 30, 100, 60));

// RIGHT — root has no layout; absolute children keep their Rect
root = View(parent, bounds); // no layout_
inputRow = View(root, Rect(0, 0, w, h)).layout_(HLayout(...));
overlay = View(root, Rect(50, 30, 100, 60));
```

Use a layout only on inner containers that should be layout-managed. Keep the overlay's parent layout-free when you need absolute positioning (for example suggestion lists under a text field).

## `^` inside closures defined in `init` returns from `init`

A `^true` / `^false` / `^nil` inside a `Function` assigned in `init` (for example `keyDownAction_({ ... })`) returns from **`init`**, not from the closure.

```supercollider
// WRONG
.keyDownAction_({ |view, char, ...|
    if (handled, { ^true });
    ^false;
});

// RIGHT — last expression is the closure's return value; use nil, not false, when unhandled
.keyDownAction_({ |view, char, ...|
    var keyHandled = nil;
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

- **`&&` and `||` evaluate their arguments eagerly** unless wrapped in a `Function` `{ ... }`. Writing `x.notNil && x.isValid` evaluates `x.isValid` before calling `&&`, messaging `nil` if `x` is `nil`. Writing `x.isNil || x.isEmpty` evaluates `x.isEmpty` even when `x` is `nil`.

```supercollider
// WRONG — evaluates the right operand eagerly, messaging nil
if (activeCommand.notNil && activeCommand.isValid(args), { ... });
if (requiredModifiers.isNil || requiredModifiers.isEmpty, { ^true });

// RIGHT — wrap right-hand operand in a function { ... } for short-circuiting
if (activeCommand.notNil && { activeCommand.isValid(args) }, { ... });
if (requiredModifiers.isNil || { requiredModifiers.size == 0 }, { ^true });

// ALSO RIGHT — separate if statements
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
