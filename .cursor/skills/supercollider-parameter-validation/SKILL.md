---
name: supercollider-parameter-validation
description: Enforces where and how constructor parameters are validated in SuperCollider classes (.sc). Use when adding or changing parameters on *new or init, or when reviewing parameter validation in this repository.
---

# SuperCollider parameter validation

Validate constructor parameters in `*new`, not in `init`.

## When to apply

Apply whenever you:

- add a parameter to `*new` or `init`
- change validation rules for a constructor parameter
- review whether validation is in the right place

## Rules

### Validate in `*new`

- Perform all constructor-parameter validation in the class method `*new`, before calling `super.new.init(...)`.
- `init` should assume parameters are already valid and focus on initialisation.
- Setter methods (`name_`) and other instance methods validate their own arguments in those methods.

### Do not validate the same parameter twice

- If `*new` validates a parameter, do not repeat that validation in `init`.

### Use `Validator`

- Use `LINK::Classes/Validator::#-validateMethodParameterType::` for type checks.
- Pass `allowNil: true` only when `nil` is explicitly allowed.
- Omit `allowNil` (or pass `false`) when the parameter must not be `nil`; a `nil` value will then fail the type check.

```supercollider
*new {
	|midiOut|
	Validator.validateMethodParameterType(midiOut, MIDIOut, "midiOut", "MyClass", "new");
	^super.new.init(midiOut);
}
```

### Pass-through parameters

- When a class accepts a parameter only to pass it to a composed object, validate it in `*new` and forward it through `init`. Do not store it in an instance `var` unless the class itself uses it.
