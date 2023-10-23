package io.github.timkraeuter.groove;

import static org.hamcrest.CoreMatchers.containsString;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;

class GrooveGTSBuilderTest {

  @Test
  void writePropertiesFileTest() throws IOException {
    Path tempDir = Files.createTempDirectory("");
    GrooveGTSBuilder grooveGTSBuilder = new GrooveGTSBuilder();

    grooveGTSBuilder.addProperty("A", "B");

    grooveGTSBuilder.writePropertiesFile(tempDir);

    String propertiesContent = Files.readString(Path.of(tempDir.toString(), "system.properties"));

    assertThat(propertiesContent, containsString("A=B"));
    assertThat(propertiesContent, containsString("(graph rule generation, see https://mvnrepository.com/artifact/io.github.timKraeuter/graph-rule-generation)"));
    assertThat(propertiesContent, containsString("grooveVersion=6.1.0"));
    assertThat(propertiesContent, containsString("grammarVersion=3.7"));
  }
}
