package io.github.timkraeuter.api;

import java.util.stream.Stream;

/**
 * Represents graphs. Graphs should be immutable. That is why we return streams of nodes and edges.
 *
 * @param <N> Node type.
 * @param <E> Edge type.
 */
public interface Graph<N extends GraphNode, E extends GraphEdge> {

  /**
   * Return all nodes of the graph.
   *
   * @return all nodes.
   */
  Stream<N> nodes();

  /**
   * All edges of the graph.
   *
   * @return all edges.
   */
  Stream<E> edges();
}
