package io.github.timkraeuter.groove.graph;

import io.github.timkraeuter.api.GraphEdge;
import java.util.concurrent.atomic.AtomicLong;

/** Edge in a groove graph. */
public class GrooveEdge implements GraphEdge {
  private static final AtomicLong idCounter = new AtomicLong(-1);
  private final String id = Long.toString(idCounter.incrementAndGet());
  private final String name;
  private final GrooveNode sourceNode;
  private final GrooveNode targetNode;

  /**
   * Create an edge.
   *
   * @param name name.
   * @param sourceNode source node.
   * @param targetNode target node.
   */
  public GrooveEdge(String name, GrooveNode sourceNode, GrooveNode targetNode) {
    this.name = name;
    this.sourceNode = sourceNode;
    this.targetNode = targetNode;
  }

  /**
   * Return the id.
   *
   * @return id.
   */
  public String getId() {
    return this.id;
  }

  /**
   * Return the name.
   *
   * @return name.
   */
  public String getName() {
    return this.name;
  }

  public GrooveNode getSourceNode() {
    return this.sourceNode;
  }

  public GrooveNode getTargetNode() {
    return this.targetNode;
  }
}
