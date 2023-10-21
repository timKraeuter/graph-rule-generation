package io.github.timkraeuter.api;

import java.util.stream.Stream;

/**
 * Represents graphs. Graphs should be immutable. That is why we return streams of nodes and edges.
 */
public interface Graph<N extends GraphNode, E extends GraphEdge> {

  Stream<N> nodes();

  Stream<E> edges();
}
