package io.github.timkraeuter.groove;

import io.github.timkraeuter.groove.graph.GrooveGraphBuilder;
import io.github.timkraeuter.groove.rule.GrooveRuleAndGraphWriter;
import io.github.timkraeuter.groove.rule.GrooveRuleBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.io.file.PathUtils;

/**
 * A builder for graph transformation systems (properties file, start graph, and transformation
 * rules).
 */
public class GrooveGTSBuilder {
  static final String START_GST = "start.gst";
  static final String START = "start";

  private final GrooveGraphBuilder startGraphBuilder;
  private final GrooveRuleBuilder ruleBuilder;
  private boolean layout;
  private final Map<String, String> additionalProperties;

  private String name;

  /** Create a new GTS builder. */
  public GrooveGTSBuilder() {
    this.startGraphBuilder = new GrooveGraphBuilder();
    this.ruleBuilder = new GrooveRuleBuilder();
    this.additionalProperties = new LinkedHashMap<>();
    this.layout = false;
    name = "";
  }

  /**
   * Set the name.
   *
   * @param name name.
   * @return builder.
   */
  public GrooveGTSBuilder name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Add a property for the groove properties file of this GTS.
   *
   * @param key key
   * @param value value
   * @return builder.
   */
  public GrooveGTSBuilder addProperty(String key, String value) {
    this.additionalProperties.put(key, value);
    return this;
  }

  /**
   * Returns the builder for the start graph.
   *
   * @return start graph builder.
   */
  public GrooveGraphBuilder startGraph() {
    return startGraphBuilder;
  }

  /**
   * Returns the builder for the rules.
   *
   * @return rule builder.
   */
  public GrooveRuleBuilder rules() {
    return this.ruleBuilder;
  }

  /**
   * Set the layout parameter.
   *
   * @param layout layout parameter.
   * @return the builder.
   */
  public GrooveGTSBuilder layout(boolean layout) {
    this.layout = layout;
    return this;
  }

  /**
   * Writes the GTS to the specified dir. This includes start graph, rules and the properties file.
   *
   * @param targetDir target directory.
   * @return dir to the generated GTS
   */
  public Path writeGTS(Path targetDir) {
    Path dir = makeSubFolder(this.name, targetDir);

    writeStartGraph(dir);

    writePropertiesFile(dir);

    writeRules(dir);

    return dir;
  }

  private Path makeSubFolder(String folderName, Path targetDir) {
    Path graphGrammarSubFolder = Paths.get(targetDir.toString(), folderName + ".gps");
    createEmptyDir(graphGrammarSubFolder);
    return graphGrammarSubFolder;
  }

  private static void createEmptyDir(Path graphGrammarSubFolder) {
    try {
      Files.createDirectories(graphGrammarSubFolder);
      if (!PathUtils.isEmpty(graphGrammarSubFolder)) {
        PathUtils.cleanDirectory(graphGrammarSubFolder);
      }
    } catch (IOException e) {
      throw new ShouldNotHappenRuntimeException(
          String.format(
              "The empty subfolder %s for graph rule generation could not be created.",
              graphGrammarSubFolder),
          e);
    }
  }

  /**
   * Write rules to the target directory.
   *
   * @param targetDir target directory
   */
  public void writeRules(Path targetDir) {
    GrooveRuleAndGraphWriter.writeRules(targetDir, ruleBuilder.getRules(), layout);
  }

  /**
   * Write the start graph to the target directory.
   *
   * @param targetDir target directory
   */
  public void writeStartGraph(Path targetDir) {
    startGraphBuilder.build().write(targetDir, START_GST, layout);
  }

  /**
   * Write the configures properties file to the target directory.
   *
   * @param targetDir target directory
   */
  public void writePropertiesFile(Path targetDir) {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());

    String propertiesContent =
        String.format(
            "# %s (graph rule generation, see https://github.com/timKraeuter/graph-rule-generation)%nlocation=%s%nstartGraph=%s%n%sgrooveVersion=6.1.0%ngrammarVersion=3.7",
            dtf.format(now), targetDir, START, this.getAdditionalProperties());
    Path propertiesFile = Paths.get(targetDir.toString(), "system.properties");
    try {
      Files.writeString(propertiesFile, propertiesContent);
    } catch (IOException e) {
      throw new ShouldNotHappenRuntimeException(e);
    }
  }

  private String getAdditionalProperties() {
    return this.additionalProperties.entrySet().stream()
        .reduce("", (prop1, prop2) -> prop1 + prop2 + "\n", (key, value) -> key + "=" + value);
  }
}
