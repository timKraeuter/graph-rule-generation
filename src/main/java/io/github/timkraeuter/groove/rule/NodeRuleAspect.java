package io.github.timkraeuter.groove.rule;

/** A node or edge in a graph rule can have one of three aspects: add, context, del, and not. */
public enum NodeRuleAspect {
  /** Added to the Graph. */
  ADD,
  /** Must be present in the graph. */
  CONTEXT,
  /** Is deleted from the graph. */
  DEL,
  /** NAC for the graph. */
  NOT
}
