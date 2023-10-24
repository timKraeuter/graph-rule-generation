package io.github.timkraeuter.groove.graph;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GrooveGraphBuilderTest {

  @Test
  void testBuilder() {
    GrooveGraphBuilder builder = new GrooveGraphBuilder();
    GrooveNode a = new GrooveNode("A");
    GrooveNode b = new GrooveNode("B");
    GrooveGraph graph = builder.name("Graph").addNode(a).addNode(b).addEdge("A to B", a, b).build();

    assertEquals("Graph", graph.getName());
    assertEquals(2, graph.nodes().count());
    assertEquals(1, graph.edges().count());
  }
}
