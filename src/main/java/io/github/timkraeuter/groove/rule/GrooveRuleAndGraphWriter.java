package io.github.timkraeuter.groove.rule;

import io.github.timkraeuter.groove.graph.GrooveEdge;
import io.github.timkraeuter.groove.graph.GrooveGraph;
import io.github.timkraeuter.groove.graph.GrooveNode;
import io.github.timkraeuter.groove.graph.GrooveValue;
import io.github.timkraeuter.groove.gxl.Graph;
import io.github.timkraeuter.groove.gxl.Gxl;
import io.github.timkraeuter.groove.gxl.Node;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Helper class to write GT-Rules for Groove. */
public class GrooveRuleAndGraphWriter {

  private GrooveRuleAndGraphWriter() {
    // Helper class
  }

  private static final String ASPECT_LABEL_NEW = "new:";
  private static final String ASPECT_LABEL_DEL = "del:";
  private static final String ASPECT_LABEL_NOT = "not:";

  /**
   * Write graph transformation rules.
   *
   * @param dir target directory where to write the rules.
   * @param rules stream of GT rules.
   * @param layout set to true to layout the rules.
   */
  public static void writeRules(Path dir, Stream<GrooveGraphRule> rules, boolean layout) {
    rules.forEach(
        grooveGraphRule -> {
          // Create gxl with a graph for each rule
          Gxl gxl = new Gxl();
          Graph graph = GrooveGxlHelper.createStandardGxlGraph(grooveGraphRule.getRuleName(), gxl);

          Map<String, Node> allGxlNodes = new HashMap<>();
          // Add nodes which should be added to gxl
          grooveGraphRule
              .getNodesToBeAdded()
              .forEach(
                  toBeAddedNode ->
                      addNodeToGxlGraph(graph, toBeAddedNode, allGxlNodes, NodeRuleAspect.ADD));

          // Add nodes which should be deleted to gxl
          grooveGraphRule
              .getNodesToBeDeleted()
              .forEach(
                  toBeDeletedNode ->
                      addNodeToGxlGraph(graph, toBeDeletedNode, allGxlNodes, NodeRuleAspect.DEL));

          // Add nodes which should be in context
          grooveGraphRule
              .getContextNodes()
              .forEach(
                  contextNode ->
                      addNodeToGxlGraph(graph, contextNode, allGxlNodes, NodeRuleAspect.CONTEXT));

          // Add NAC nodes
          grooveGraphRule
              .getNACNodes()
              .forEach(
                  contextNode ->
                      addNodeToGxlGraph(graph, contextNode, allGxlNodes, NodeRuleAspect.NOT));

          // Add edges which should be added to gxl
          grooveGraphRule
              .getEdgesToBeAdded()
              .forEach(
                  toBeAddedEdge ->
                      addEdgeToGxlGraph(graph, toBeAddedEdge, allGxlNodes, NodeRuleAspect.ADD));

          // Add edges which should be deleted to gxl
          grooveGraphRule
              .getEdgesToBeDeleted()
              .forEach(
                  toBeDeletedEdge ->
                      addEdgeToGxlGraph(graph, toBeDeletedEdge, allGxlNodes, NodeRuleAspect.DEL));

          // Add context edges to gxl
          grooveGraphRule
              .getContextEdges()
              .forEach(
                  toBeDeletedEdge ->
                      addEdgeToGxlGraph(
                          graph, toBeDeletedEdge, allGxlNodes, NodeRuleAspect.CONTEXT));

          // Add nac edges to gxl
          grooveGraphRule
              .getNacEdges()
              .forEach(
                  toBeDeletedEdge ->
                      addEdgeToGxlGraph(graph, toBeDeletedEdge, allGxlNodes, NodeRuleAspect.NOT));

          layoutRuleIfConfigured(layout, grooveGraphRule, graph);
          // Write each rule to a file
          writeRuleToFile(dir, grooveGraphRule, gxl);
        });
  }

  /**
   * Write a graph to disk for groove to consume.
   *
   * @param dir directory
   * @param fileName file name of the graph
   * @param graph graph
   * @param layout true if the graph should be layouted.
   */
  public static void writeGraph(Path dir, String fileName, GrooveGraph graph, boolean layout) {
    Gxl gxl = createGxlFromGrooveGraph(graph, layout);
    GxlToXMLConverter.toXml(gxl, Paths.get(dir.toString(), fileName));
  }

  private static Gxl createGxlFromGrooveGraph(GrooveGraph graph, boolean layout) {
    Gxl gxl = new Gxl();
    io.github.timkraeuter.groove.gxl.Graph gxlGraph =
        GrooveGxlHelper.createStandardGxlGraph(graph.getName(), gxl);

    Map<String, String> idToNodeLabel = new HashMap<>();
    Map<String, Node> grooveNodeIdToGxlNode = new HashMap<>();

    graph
        .nodes()
        .forEach(
            node -> {
              Node gxlNode =
                  GrooveGxlHelper.createNodeWithName(node.getId(), node.getName(), gxlGraph);
              // Add flags
              node.getFlags()
                  .forEach(flag -> GrooveGxlHelper.addFlagToNode(gxlGraph, gxlNode, flag));
              // Add data nodes/attributes
              node.getAttributes()
                  .forEach(
                      (name, value) ->
                          addNodeAttribute(gxlGraph, idToNodeLabel, gxlNode, name, value));

              idToNodeLabel.put(node.getId(), node.getName());
              grooveNodeIdToGxlNode.put(node.getId(), gxlNode);
            });
    graph
        .edges()
        .forEach(
            edge ->
                GrooveGxlHelper.createEdgeWithName(
                    gxlGraph,
                    grooveNodeIdToGxlNode.get(edge.getSourceNode().getId()),
                    grooveNodeIdToGxlNode.get(edge.getTargetNode().getId()),
                    edge.getName()));

    if (layout) {
      GrooveGxlHelper.layoutGraph(gxlGraph, idToNodeLabel);
    }
    return gxl;
  }

  private static void addNodeAttribute(
      io.github.timkraeuter.groove.gxl.Graph graph,
      Map<String, String> idToNodeLabel,
      Node attributeHolder,
      String attributeName,
      GrooveValue attributeValue) {
    String attributeNodeName =
        String.format("%s:%s", attributeValue.getTypeName(), attributeValue.getValue());
    Node dataNode =
        GrooveGxlHelper.createNodeWithName(GrooveNode.getNextNodeId(), attributeNodeName, graph);
    GrooveGxlHelper.createEdgeWithName(graph, attributeHolder, dataNode, attributeName);

    idToNodeLabel.put(dataNode.getId(), attributeNodeName);
  }

  private static void layoutRuleIfConfigured(
      boolean layout, GrooveGraphRule grooveGraphRule, Graph graph) {
    if (layout) {
      GrooveGxlHelper.layoutGraph(
          graph,
          grooveGraphRule.getAllNodes().entrySet().stream()
              .collect(
                  Collectors.toMap(
                      Map.Entry::getKey, idNodePair -> idNodePair.getValue().getName())));
    }
  }

  private static void addEdgeToGxlGraph(
      Graph graph,
      GrooveEdge grooveEdge,
      Map<String, Node> createdGxlNodes,
      NodeRuleAspect nodeAspect) {
    Node sourceNode = createdGxlNodes.get(grooveEdge.getSourceNode().getId());
    Node targetNode = createdGxlNodes.get(grooveEdge.getTargetNode().getId());
    assert sourceNode != null;
    assert targetNode != null;

    GrooveGxlHelper.createEdgeWithName(
        graph, sourceNode, targetNode, getAspectLabel(nodeAspect) + grooveEdge.getName());
  }

  private static void addNodeToGxlGraph(
      Graph graph,
      GrooveNode grooveNode,
      Map<String, Node> nodeRepository,
      NodeRuleAspect nodeAspect) {
    Node gxlNode =
        GrooveGxlHelper.createNodeWithName(grooveNode.getId(), grooveNode.getName(), graph);
    // Each flag itself could be deleted, added or just context!
    grooveNode.getFlags().forEach(flag -> GrooveGxlHelper.addFlagToNode(graph, gxlNode, flag));
    nodeRepository.put(gxlNode.getId(), gxlNode);

    // Nodes need get a "new:", "del:" or no label depending on their aspect.
    switch (nodeAspect) {
      case CONTEXT:
        // No label
        break;
      case ADD:
        GrooveGxlHelper.createEdgeWithName(graph, gxlNode, gxlNode, ASPECT_LABEL_NEW);
        break;
      case DEL:
        GrooveGxlHelper.createEdgeWithName(graph, gxlNode, gxlNode, ASPECT_LABEL_DEL);
        break;
      case NOT:
        GrooveGxlHelper.createEdgeWithName(graph, gxlNode, gxlNode, ASPECT_LABEL_NOT);
        break;
    }
  }

  private static String getAspectLabel(NodeRuleAspect addDelOrContext) {
    return switch (addDelOrContext) {
      case ADD -> ASPECT_LABEL_NEW;
      case DEL -> ASPECT_LABEL_DEL;
      case CONTEXT -> "";
      case NOT -> ASPECT_LABEL_NOT;
    };
  }

  private static void writeRuleToFile(Path dir, GrooveGraphRule grooveGraphRule, Gxl gxl) {
    Path file = Paths.get(dir.toString(), grooveGraphRule.getRuleName() + ".gpr");
    GxlToXMLConverter.toXml(gxl, file);
  }
}
