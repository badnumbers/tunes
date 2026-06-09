---
name: supercollider-class-ordering
description: Enforces member ordering inside SuperCollider class definitions (.sc). Use when creating a new SuperCollider class, or when the user explicitly asks to reorder an existing class. Do not reorder existing classes while making functional changes.
---

# SuperCollider class member ordering

Every class body in this repository must follow this layout:

1. **classvars** — all `classvar` declarations first
2. **vars** — all instance `var` declarations second
3. **methods** — all method declarations last

Within each of those three groups, members are ordered **alphabetically**.

## When to apply

**New classes:** apply this ordering when creating a class from scratch.

**Existing classes:** apply this ordering only when the user explicitly asks you to reorder the class. Do **not** reorder an existing class at the same time as functional changes (bug fixes, new behaviour, signature changes, and so on). Reordering obscures the functional diff.

### After functional changes to an existing class

When you finish functional edits to an existing `.sc` class and the file does not yet follow this convention, tell the user:

- you have **not** reordered the class
- they can ask you to reorder it as a separate step if they want

Do not reorder unless asked.

### Adding members during functional changes

When adding a `classvar`, `var`, or method to an existing class as part of functional work, place the new member without reshuffling existing members. A later reordering pass can sort the full class when requested.

## Ordering rules

### classvars

List every `classvar` declaration together at the top of the class, sorted alphabetically by variable name.

```supercollider
MyClass {
	classvar prAlpha;
	classvar prBeta = 1;
	classvar <>gamma;
```

### vars

List every instance `var` declaration together after the classvars, sorted alphabetically by variable name.

```supercollider
	var prApple;
	var prBanana = 0;
	var <>cherry;
```

### methods

List every method after the vars, sorted alphabetically by method name.

- Include instance methods, class methods (`*name`), getters, setters (`name_`), and private methods (`prName`).
- For class methods, sort by the name after `*` (e.g. `*new` is sorted as `new`).
- Getter/setter pairs are separate entries: `loopEnd` comes before `loopEnd_`.

```supercollider
	*new {
		^super.new.init;
	}

	delta {
		^prDelta;
	}

	init {
		|arg|
	}

	loopEnd {
		^prLoopEnd;
	}

	loopEnd_ {
		|newTime|
	}
```

## Full class skeleton

```supercollider
ExampleClass {
	classvar prSharedA;
	classvar prSharedB;

	var prFieldA;
	var prFieldB;

	*new {
		^super.new.init;
	}

	init {
		|arg|
	}

	prHelper {
	}

	publicMethod {
	}
}
```

## Files with multiple classes

Each class block in a file follows these rules independently. Blank lines between the three groups within a class are optional but encouraged for readability.

## Checklist

Use this checklist when creating a new class, or when the user has asked you to reorder an existing one:

- [ ] All `classvar` declarations are at the top, alphabetically sorted
- [ ] All `var` declarations come next, alphabetically sorted
- [ ] All methods come last, alphabetically sorted
- [ ] No `var` or `classvar` declarations appear after the first method

When finishing functional changes to an existing class without reordering:

- [ ] New members were added without reshuffling existing members
- [ ] You told the user reordering was not done and they can ask for it separately
