package io.github.timkraeuter.api;

/** Represents nodes in a graph. Nodes should be immutable. */
public interface GraphNode {

  /**
   * Return the id.
   *
   * @return ID
   */
  String getId();
}
