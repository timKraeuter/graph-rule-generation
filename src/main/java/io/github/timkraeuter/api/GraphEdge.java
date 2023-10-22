package io.github.timkraeuter.api;

/** Represents edges in a graph. Edges should be immutable. */
public interface GraphEdge {

  /**
   * Returns the source node.
   *
   * @return Source node.
   */
  GraphNode getSourceNode();

  /**
   * Returns the target node.
   *
   * @return Target node.
   */
  GraphNode getTargetNode();
}
