package io.github.timkraeuter.groove;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.github.timkraeuter.groove.graph.GrooveNode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GrooveGTSBuilderTest {

  static final String EXPECTED_START_GRAPH =
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
      """;
  static final String EXPECTED_RULE =
      """
      <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
      <gxl xmlns="http://www.gupro.de/GXL/gxl-1.0.dtd">
          <graph id="Test" role="rule" edgeids="false" edgemode="directed">
              <node id="n0"/>
              <edge from="n0" to="n0">
                  <attr name="label">
                      <string>A</string>
                  </attr>
              </edge>
              <edge from="n0" to="n0">
                  <attr name="label">
                      <string>new:</string>
                  </attr>
              </edge>
              <node id="n1"/>
              <edge from="n1" to="n1">
                  <attr name="label">
                      <string>B</string>
                  </attr>
              </edge>
              <edge from="n1" to="n1">
                  <attr name="label">
                      <string>new:</string>
                  </attr>
              </edge>
              <edge from="n0" to="n1">
                  <attr name="label">
                      <string>new:A to B</string>
                  </attr>
              </edge>
          </graph>
      </gxl>
      """;
  static final String EXPECTED_START_GRAPH_LAYOUT =
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
      """;
  Path tempDir;
  GrooveGTSBuilder grooveGTSBuilder;

  @BeforeEach
  void setUp() throws IOException {
    tempDir = Files.createTempDirectory("");
    grooveGTSBuilder = new GrooveGTSBuilder();
  }

  @Test
  void writeRulesTest() throws IOException {
    buildSampleRule();

    grooveGTSBuilder.writeRules(tempDir);

    String rule = readFileFromDir(tempDir, "Test.gpr");
    assertThat(rule, is(EXPECTED_RULE));
  }

  private void buildSampleRule() {
    GrooveNode.setIDCounter(-1);
    grooveGTSBuilder.rules().startRule("Test");

    GrooveNode a = grooveGTSBuilder.rules().addNode("A");
    GrooveNode b = grooveGTSBuilder.rules().addNode("B");
    grooveGTSBuilder.rules().addEdge("A to B", a, b);

    grooveGTSBuilder.rules().buildRule();
  }

  @Test
  void writeStartGraphTest() throws IOException {
    buildSampleStartGraph();

    String startGraph = readFileFromDir(tempDir, "start.gst");
    assertThat(startGraph, is(EXPECTED_START_GRAPH));

    grooveGTSBuilder.layout(true);
    grooveGTSBuilder.writeStartGraph(tempDir);

    startGraph = Files.readString(Path.of(tempDir.toString(), "start.gst"));
    assertThat(startGraph, is(EXPECTED_START_GRAPH_LAYOUT));
  }

  private void buildSampleStartGraph() {
    GrooveNode.setIDCounter(-1);
    grooveGTSBuilder
        .startGraph()
        .name("start")
        .addNode(new GrooveNode("A"))
        .addNode(new GrooveNode("B"));

    grooveGTSBuilder.writeStartGraph(tempDir);
  }

  @Test
  void writePropertiesFileTest() throws IOException {

    grooveGTSBuilder.addProperty("A", "B");

    grooveGTSBuilder.writePropertiesFile(tempDir);

    String propertiesContent = readFileFromDir(tempDir, "system.properties");
    assertThat(propertiesContent, containsString("A=B"));
    assertThat(
        propertiesContent,
        containsString(
            "(graph rule generation, see https://github.com/timKraeuter/graph-rule-generation)"));
    assertThat(propertiesContent, containsString("grooveVersion=6.1.0"));
    assertThat(propertiesContent, containsString("grammarVersion=3.7"));
  }

  @Test
  void buildAndWriteEverythingTest() throws IOException {
    buildSampleStartGraph();
    buildSampleRule();

    Path gtsDir = grooveGTSBuilder.writeGTS(tempDir);

    String startGraph = readFileFromDir(tempDir, "start.gst");
    assertThat(startGraph, is(EXPECTED_START_GRAPH));

    String rule = readFileFromDir(gtsDir, "Test.gpr");
    assertThat(rule, is(EXPECTED_RULE));

    String propertiesContent = readFileFromDir(gtsDir, "system.properties");
    assertThat(
        propertiesContent,
        containsString(
            "(graph rule generation, see https://github.com/timKraeuter/graph-rule-generation)"));
    assertThat(propertiesContent, containsString("grooveVersion=6.1.0"));
    assertThat(propertiesContent, containsString("grammarVersion=3.7"));
  }

  private String readFileFromDir(Path dir, String filename) throws IOException {
    return Files.readString(Path.of(dir.toString(), filename));
  }
}
