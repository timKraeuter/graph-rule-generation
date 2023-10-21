package io.github.timkraeuter.groove.rule;

/** A node or edge in a graph rule can have one of three aspects: add, context, del, and not. */
public enum NodeRuleAspect {
  ADD,
  CONTEXT,
  DEL,
  NOT
}
