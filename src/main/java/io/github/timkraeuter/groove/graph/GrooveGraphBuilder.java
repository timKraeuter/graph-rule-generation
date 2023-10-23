package io.github.timkraeuter.groove.graph;

import java.util.LinkedHashSet;
import java.util.Set;

/** Graph builder for groove. */
public class GrooveGraphBuilder {

  private String name;
  private final Set<GrooveNode> nodes;
  private final Set<GrooveEdge> edges;

  /** Create a new graph builder. */
  public GrooveGraphBuilder() {
    this.nodes = new LinkedHashSet<>();
    this.edges = new LinkedHashSet<>();
  }

  /**
   * Set the name.
   *
   * @param name name.
   * @return Graph builder
   */
  public GrooveGraphBuilder name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Add a node
   *
   * @param node node
   * @return Graph builder
   */
  public GrooveGraphBuilder addNode(GrooveNode node) {
    if (node == null) {
      throw new IllegalArgumentException("Node must not be null!");
    }
    this.nodes.add(node);
    return this;
  }

  /**
   * Add an edge.
   *
   * @param name edge name
   * @param source edge source
   * @param target edge target
   * @return Graph builder
   */
  public GrooveGraphBuilder addEdge(String name, GrooveNode source, GrooveNode target) {
    this.addNode(source).addNode(target);
    this.edges.add(new GrooveEdge(name, source, target));
    return this;
  }

  /**
   * Build the graph.
   *
   * @return built graph.
   */
  public GrooveGraph build() {
    return new GrooveGraph(this.name, this.nodes, this.edges);
  }
}
