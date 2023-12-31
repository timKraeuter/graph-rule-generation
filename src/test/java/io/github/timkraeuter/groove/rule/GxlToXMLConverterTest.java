package io.github.timkraeuter.groove.rule;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.github.timkraeuter.groove.gxl.Graph;
import io.github.timkraeuter.groove.gxl.Gxl;
import org.junit.jupiter.api.Test;

class GxlToXMLConverterTest {
  @Test
  void test() {
    Gxl gxl = new Gxl();
    Graph graph = new Graph();
    gxl.getGraph().add(graph);
    graph.setRole("rule");
    graph.setEdgeids("false");
    graph.setEdgemode("directed");
    graph.setId("addNodesWithEdge");
    String gxlString = GxlToXMLConverter.toXml(gxl);
    assertThat(
        gxlString,
        is(
            """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <gxl xmlns="http://www.gupro.de/GXL/gxl-1.0.dtd">
                    <graph id="addNodesWithEdge" role="rule" edgeids="false" edgemode="directed"/>
                </gxl>
                """));
  }
}
