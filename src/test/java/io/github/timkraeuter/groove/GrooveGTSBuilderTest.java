package io.github.timkraeuter.groove;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.github.timkraeuter.groove.graph.GrooveNode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class GrooveGTSBuilderTest {

  @Test
  void writeStartGraphTest() throws IOException {
    Path tempDir = Files.createTempDirectory("");
    GrooveGTSBuilder grooveGTSBuilder = new GrooveGTSBuilder();

    grooveGTSBuilder
        .startGraph()
        .name("start")
        .addNode(new GrooveNode("A"))
        .addNode(new GrooveNode("B"));

    grooveGTSBuilder.writeStartGraph(tempDir);

    String startGraph = Files.readString(Path.of(tempDir.toString(), "start.gst"));

    assertThat(
        startGraph,
        is(
            """
          <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
          <gxl xmlns="http://www.gupro.de/GXL/gxl-1.0.dtd">
              <graph id="start" role="rule" edgeids="false" edgemode="directed">
                  <node id="n0"/>
                  <edge from="n0" to="n0">
                      <attr name="label">
                          <string>A</string>
                      </attr>
                  </edge>
                  <node id="n1"/>
                  <edge from="n1" to="n1">
                      <attr name="label">
                          <string>B</string>
                      </attr>
                  </edge>
              </graph>
          </gxl>
          """));

    grooveGTSBuilder.layout(true);

    grooveGTSBuilder.writeStartGraph(tempDir);

    startGraph = Files.readString(Path.of(tempDir.toString(), "start.gst"));

    assertThat(
        startGraph,
        is(
            """
          <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
          <gxl xmlns="http://www.gupro.de/GXL/gxl-1.0.dtd">
              <graph id="start" role="rule" edgeids="false" edgemode="directed">
                  <node id="n0">
                      <attr name="layout">
                          <string>62 72 0 0</string>
                      </attr>
                  </node>
                  <edge from="n0" to="n0">
                      <attr name="label">
                          <string>A</string>
                      </attr>
                  </edge>
                  <node id="n1">
                      <attr name="layout">
                          <string>97 72 0 0</string>
                      </attr>
                  </node>
                  <edge from="n1" to="n1">
                      <attr name="label">
                          <string>B</string>
                      </attr>
                  </edge>
              </graph>
          </gxl>
          """));
  }

  @Test
  void writePropertiesFileTest() throws IOException {
    Path tempDir = Files.createTempDirectory("");
    GrooveGTSBuilder grooveGTSBuilder = new GrooveGTSBuilder();

    grooveGTSBuilder.addProperty("A", "B");

    grooveGTSBuilder.writePropertiesFile(tempDir);

    String propertiesContent = Files.readString(Path.of(tempDir.toString(), "system.properties"));

    assertThat(propertiesContent, containsString("A=B"));
    assertThat(
        propertiesContent,
        containsString(
            "(graph rule generation, see https://github.com/timKraeuter/graph-rule-generation)"));
    assertThat(propertiesContent, containsString("grooveVersion=6.1.0"));
    assertThat(propertiesContent, containsString("grammarVersion=3.7"));
  }
}
