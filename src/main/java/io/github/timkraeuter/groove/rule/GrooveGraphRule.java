package io.github.timkraeuter.groove.rule;

import io.github.timkraeuter.api.GraphRule;
import io.github.timkraeuter.groove.graph.GrooveEdge;
import io.github.timkraeuter.groove.graph.GrooveNode;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/** Represent a groove graph rule. */
public class GrooveGraphRule implements GraphRule {
  private final String ruleName;

  private final Map<String, GrooveNode> nodesToBeAdded;
  private final Map<String, GrooveNode> contextNodes;
  private final Map<String, GrooveNode> nodesToBeDeleted;
  private final Map<String, GrooveNode> nacNodes;
  private final Map<String, GrooveEdge> edgesToBeAdded;
  private final Map<String, GrooveEdge> contextEdges;
  private final Map<String, GrooveEdge> edgesToBeDeleted;

  /**
   * Create a new rule.
   *
   * @param ruleName rule name.
   */
  public GrooveGraphRule(String ruleName) {
    this.ruleName = ruleName;
    this.nodesToBeAdded = new LinkedHashMap<>();
    this.contextNodes = new LinkedHashMap<>();
    this.nodesToBeDeleted = new LinkedHashMap<>();
    this.edgesToBeAdded = new LinkedHashMap<>();
    this.contextEdges = new LinkedHashMap<>();
    this.edgesToBeDeleted = new LinkedHashMap<>();
    this.nacNodes = new LinkedHashMap<>();
  }

  /**
   * Returns the rule name.
   *
   * @return name.
   */
  public String getRuleName() {
    return this.ruleName;
  }

  void addNewNode(GrooveNode newNode) {
    this.checkIfNotAlreadyInContext(newNode);
    this.checkIfNotAlreadyDeleted(newNode);
    this.checkIfNotAlreadyInNot(newNode);
    this.nodesToBeAdded.put(newNode.getId(), newNode);
  }

  private void checkIfNotAlreadyDeleted(GrooveNode node) {
    if (this.nodesToBeDeleted.get(node.getId()) != null) {
      throw new IllegalArgumentException(
          String.format("Node %s already contained as a to-be-deleted node!", node));
    }
  }

  private void checkIfNotAlreadyInContext(GrooveNode node) {
    if (this.contextNodes.get(node.getId()) != null) {
      throw new IllegalArgumentException(
          String.format("Node %s already contained as a context node!", node));
    }
  }

  private void checkIfNotAlreadyInNot(GrooveNode node) {
    if (this.nacNodes.get(node.getId()) != null) {
      throw new IllegalArgumentException(
          String.format("Node %s already contained as a not node (NAC)!", node));
    }
  }

  private void checkIfNotAlreadyAdded(GrooveNode contextNode) {
    if (this.nodesToBeAdded.get(contextNode.getId()) != null) {
      throw new IllegalArgumentException(
          String.format("Node %s already contained as a to-be-added node!", contextNode));
    }
  }

  void addContextNode(GrooveNode contextNode) {
    this.checkIfNotAlreadyAdded(contextNode);
    this.checkIfNotAlreadyDeleted(contextNode);
    this.checkIfNotAlreadyInNot(contextNode);
    this.contextNodes.put(contextNode.getId(), contextNode);
  }

  void addDelNode(GrooveNode deleteNode) {
    this.checkIfNotAlreadyAdded(deleteNode);
    this.checkIfNotAlreadyInContext(deleteNode);
    this.checkIfNotAlreadyInNot(deleteNode);
    this.nodesToBeDeleted.put(deleteNode.getId(), deleteNode);
  }

  void addNacNode(GrooveNode nacNode) {
    this.checkIfNotAlreadyAdded(nacNode);
    this.checkIfNotAlreadyDeleted(nacNode);
    this.checkIfNotAlreadyInContext(nacNode);
    this.nacNodes.put(nacNode.getId(), nacNode);
  }

  void addNewEdge(GrooveEdge edge) {
    // It should be checked elsewhere that the source and target node are in the context of the rule
    // or added by the rule!
    this.edgesToBeAdded.put(edge.getId(), edge);
  }

  void addDelEdge(GrooveEdge edge) {
    // It should be checked elsewhere that the source and target nodes are contained in the rule!
    this.edgesToBeDeleted.put(edge.getId(), edge);
  }

  void addContextEdge(GrooveEdge edge) {
    // It should be checked elsewhere that the source and target nodes are contained in the rule!
    this.contextEdges.put(edge.getId(), edge);
  }

  /**
   * Return the added nodes.
   *
   * @return nodes to be added.
   */
  public Set<GrooveNode> getNodesToBeAdded() {
    return new LinkedHashSet<>(this.nodesToBeAdded.values());
  }

  /**
   * Return the context nodes.
   *
   * @return context nodes.
   */
  public Set<GrooveNode> getContextNodes() {
    return new LinkedHashSet<>(this.contextNodes.values());
  }

  /**
   * Return the NAC nodes.
   *
   * @return NAC nodes.
   */
  public Set<GrooveNode> getNACNodes() {
    return new LinkedHashSet<>(this.nacNodes.values());
  }

  /**
   * Return the edges to be added.
   *
   * @return edges to be added.
   */
  public Set<GrooveEdge> getEdgesToBeAdded() {
    return new LinkedHashSet<>(this.edgesToBeAdded.values());
  }

  /**
   * Return the edges to be deleted.
   *
   * @return edges to be deleted.
   */
  public Set<GrooveEdge> getEdgesToBeDeleted() {
    return new LinkedHashSet<>(this.edgesToBeDeleted.values());
  }

  /**
   * Return context edges.
   *
   * @return edges.
   */
  public Set<GrooveEdge> getContextEdges() {
    return new LinkedHashSet<>(this.contextEdges.values());
  }

  /**
   * All context and added nodes.
   *
   * @return nodes.
   */
  public Map<String, GrooveNode> getContextAndAddedNodes() {
    Map<String, GrooveNode> addedAndContextNodes = new HashMap<>(this.nodesToBeAdded);
    addedAndContextNodes.putAll(this.contextNodes); // the maps are distinct, see add methods.
    return addedAndContextNodes;
  }

  /**
   * Get all nodes to be deleted.
   *
   * @return nodes to be deleted.
   */
  public Set<GrooveNode> getNodesToBeDeleted() {
    return new LinkedHashSet<>(this.nodesToBeDeleted.values());
  }

  /**
   * Get all nodes.
   *
   * @return all nodes.
   */
  public Map<String, GrooveNode> getAllNodes() {
    Map<String, GrooveNode> allNodes = new LinkedHashMap<>(this.nodesToBeDeleted);
    allNodes.putAll(this.nodesToBeAdded);
    allNodes.putAll(this.contextNodes);
    allNodes.putAll(this.nacNodes);
    return allNodes;
  }
}
