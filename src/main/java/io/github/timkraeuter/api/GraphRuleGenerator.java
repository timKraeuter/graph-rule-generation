package io.github.timkraeuter.api;

public interface GraphRuleGenerator {

  void startRule(String ruleName);

  /** Define that the current rule needs a node in the context with the given name. */
  GraphNode contextNode(String name);

  /** Define that the current rule adds a node with the given name. */
  GraphNode addNode(String nodeName);

  /** Define that the current rule deletes a node with the given name. */
  GraphNode deleteNode(String nodeName);

  /** Define that the current rule is not applicable if a node with the given name exists (NAC). */
  GraphNode nacNode(String nodeName);

  /** Define that the current rule adds an edge between the two given nodes. */
  void addEdge(String name, GraphNode source, GraphNode target);

  /**
   * Define that the current rule deletes an edge between two nodes (The nodes must be in context,
   * added or deleted).
   */
  void deleteEdge(String name, GraphNode source, GraphNode target);

  /**
   * Define that the current rule needs an edge between two nodes (The nodes must be in context,
   * added or deleted).
   */
  void contextEdge(String name, GraphNode source, GraphNode target);

  GraphRule buildRule();
}
