package io.github.timkraeuter.api;

import io.github.timkraeuter.groove.graph.GrooveNode;

/** Represent a rule builder for graphs. */
public interface GraphRuleBuilder {

  /**
   * Start a new rule.
   *
   * @param ruleName name for the new rule.
   * @return builder.
   */
  GraphRuleBuilder startRule(String ruleName);

  /**
   * Define that the current rule needs a node in the context with the given name.
   *
   * @param nodeName nodeName.
   * @return created graph node.
   */
  GraphNode contextNode(String nodeName);

  /**
   * Define that the current rule adds a node with the given name.
   *
   * @param nodeName node name
   * @return created graph node.
   */
  GraphNode addNode(String nodeName);

  /**
   * Define that the current rule deletes a node with the given name.
   *
   * @param nodeName node name
   * @return created graph node.
   */
  GraphNode deleteNode(String nodeName);

  /**
   * Define that the current rule is not applicable if a node with the given name exists (NAC).
   *
   * @param nodeName node name
   * @return created graph node.
   */
  GraphNode nacNode(String nodeName);

  /**
   * Define that the current rule adds an edge between the two given nodes.
   *
   * @param name edge name
   * @param source source node
   * @param target target node
   * @return builder.
   */
  GraphRuleBuilder addEdge(String name, GraphNode source, GraphNode target);

  /**
   * Define that the current rule deletes an edge between two nodes (The nodes must be in context,
   * added or deleted).
   *
   * @param name edge name
   * @param source source node
   * @param target target node
   * @return builder.
   */
  GraphRuleBuilder deleteEdge(String name, GraphNode source, GraphNode target);

  /**
   * Define that the current rule needs an edge between two nodes (The nodes must be in context,
   * added or deleted).
   *
   * @param name edge name
   * @param source source node
   * @param target target node
   * @return builder.
   */
  GraphRuleBuilder contextEdge(String name, GraphNode source, GraphNode target);

  /**
   * Define that the current rule is not applicable if an edge between the two nodes exists.
   *
   * @param name edge name
   * @param source source node
   * @param target target node
   * @return builder.
   */
  GraphRuleBuilder nacEdge(String name, GrooveNode source, GrooveNode target);

  /**
   * Build the current rule.
   *
   * @return the build rule.
   */
  GraphRule buildRule();
}
