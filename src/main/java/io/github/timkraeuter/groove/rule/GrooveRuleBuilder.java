package io.github.timkraeuter.groove.rule;

import io.github.timkraeuter.api.GraphNode;
import io.github.timkraeuter.api.GraphRuleGenerator;
import io.github.timkraeuter.groove.graph.GrooveEdge;
import io.github.timkraeuter.groove.graph.GrooveNode;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/** Builder for Groove rules. */
public class GrooveRuleBuilder implements GraphRuleGenerator {
  private final Map<String, GrooveGraphRule> ruleNameToRule;
  private GrooveGraphRule currentRule;

  /** Create a new rule builder. */
  public GrooveRuleBuilder() {
    ruleNameToRule = new LinkedHashMap<>();
    currentRule = null;
  }

  /**
   * Created synced rules.
   *
   * @param nameToToBeSyncedRules name and rules to be synched.
   * @return Synced GT rules.
   */
  public static Stream<GrooveGraphRule> createSyncedRules(
      Map<String, Set<GrooveGraphRule>> nameToToBeSyncedRules) {
    GrooveRuleBuilder ruleGenerator = new GrooveRuleBuilder();
    nameToToBeSyncedRules.forEach(
        (synchedRuleName, synchedRules) -> {
          ruleGenerator.startRule(synchedRuleName);

          synchedRules.stream()
              .sorted(Comparator.comparing(GrooveGraphRule::getRuleName))
              .forEach(
                  grooveGraphRule -> {
                    Map<String, GrooveNode> oldIdToNewNode = new HashMap<>();
                    // Nodes
                    grooveGraphRule
                        .getNodesToBeAdded()
                        .forEach(
                            addNode -> {
                              GrooveNode createdAddNode = ruleGenerator.addNode(addNode.getName());
                              oldIdToNewNode.put(addNode.getId(), createdAddNode);
                            });
                    grooveGraphRule
                        .getNodesToBeDeleted()
                        .forEach(
                            delNode -> {
                              GrooveNode createdDelNode =
                                  ruleGenerator.deleteNode(delNode.getName());
                              oldIdToNewNode.put(delNode.getId(), createdDelNode);
                            });
                    grooveGraphRule
                        .getContextNodes()
                        .forEach(
                            contextNode -> {
                              GrooveNode createdContextNode =
                                  ruleGenerator.contextNode(contextNode.getName());
                              oldIdToNewNode.put(contextNode.getId(), createdContextNode);
                            });

                    // Edges
                    grooveGraphRule
                        .getEdgesToBeAdded()
                        .forEach(
                            addEdge ->
                                ruleGenerator.addEdge(
                                    addEdge.getName(),
                                    oldIdToNewNode.get(addEdge.getSourceNode().getId()),
                                    oldIdToNewNode.get(addEdge.getTargetNode().getId())));
                    grooveGraphRule
                        .getEdgesToBeDeleted()
                        .forEach(
                            delEdge ->
                                ruleGenerator.deleteEdge(
                                    delEdge.getName(),
                                    oldIdToNewNode.get(delEdge.getSourceNode().getId()),
                                    oldIdToNewNode.get(delEdge.getTargetNode().getId())));
                  });

          ruleGenerator.buildRule();
        });
    return ruleGenerator.getRules();
  }

  @Override
  public void startRule(String ruleName) {
    if (ruleNameToRule.get(ruleName) != null) {
      throw new IllegalArgumentException(
          String.format("A rule with the name \"%s\" already exists!", ruleName));
    }
    this.currentRule = new GrooveGraphRule(ruleName);
  }

  @Override
  public GrooveNode contextNode(String nodeName) {
    assert this.currentRule != null;
    GrooveNode contextNode = new GrooveNode(nodeName);
    this.currentRule.addContextNode(contextNode);
    return contextNode;
  }

  @Override
  public GrooveNode addNode(String nodeName) {
    assert this.currentRule != null;

    GrooveNode newNode = new GrooveNode(nodeName);
    this.currentRule.addNewNode(newNode);
    return newNode;
  }

  @Override
  public void addEdge(String edgeName, GraphNode source, GraphNode target) {

    assert this.currentRule != null;
    Map<String, GrooveNode> contextAndAddedNodes = this.currentRule.getContextAndAddedNodes();
    GrooveNode sourceNode = contextAndAddedNodes.get(source.getId());
    GrooveNode targetNode = contextAndAddedNodes.get(target.getId());

    this.checkNodeContainment(source, target, sourceNode, targetNode);

    this.currentRule.addNewEdge(new GrooveEdge(edgeName, sourceNode, targetNode));
  }

  @Override
  public GrooveNode deleteNode(String nodeName) {
    assert this.currentRule != null;

    GrooveNode deleteNode = new GrooveNode(nodeName);
    this.currentRule.addDelNode(deleteNode);
    return deleteNode;
  }

  @Override
  public GrooveNode nacNode(String nodeName) {
    assert this.currentRule != null;

    GrooveNode notNode = new GrooveNode(nodeName);
    this.currentRule.addNacNode(notNode);
    return notNode;
  }

  @Override
  public void deleteEdge(String edgeName, GraphNode source, GraphNode target) {
    assert this.currentRule != null;
    Map<String, GrooveNode> contextAndAddedNodes = this.currentRule.getAllNodes();

    GrooveNode sourceNode = contextAndAddedNodes.get(source.getId());
    GrooveNode targetNode = contextAndAddedNodes.get(target.getId());

    this.checkNodeContainment(source, target, sourceNode, targetNode);

    this.currentRule.addDelEdge(new GrooveEdge(edgeName, sourceNode, targetNode));
  }

  @Override
  public void contextEdge(String name, GraphNode source, GraphNode target) {
    assert this.currentRule != null;
    Map<String, GrooveNode> nodes = this.currentRule.getAllNodes();

    GrooveNode sourceNode = nodes.get(source.getId());
    GrooveNode targetNode = nodes.get(target.getId());

    this.checkNodeContainment(source, target, sourceNode, targetNode);

    this.currentRule.addContextEdge(new GrooveEdge(name, sourceNode, targetNode));
  }

  private void checkNodeContainment(
      GraphNode source, GraphNode target, GrooveNode sourceNode, GrooveNode targetNode) {
    if (sourceNode == null) {
      throw new IllegalArgumentException(
          String.format("Source node %s not contained in the rule!", source));
    }
    if (targetNode == null) {
      throw new IllegalArgumentException(
          String.format("Target node %s not contained in the rule!", target));
    }
  }

  @Override
  public GrooveGraphRule buildRule() {
    GrooveGraphRule newRule = this.currentRule;
    this.ruleNameToRule.put(newRule.getRuleName(), newRule);
    this.currentRule = null;
    return newRule;
  }

  /**
   * Get all rules.
   *
   * @return all build rules.
   */
  public Stream<GrooveGraphRule> getRules() {
    return this.ruleNameToRule.values().stream();
  }
}
