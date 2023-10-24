package io.github.timkraeuter.groove.rule;

import static io.github.timkraeuter.util.FileTestHelper.getResource;

import io.github.timkraeuter.groove.graph.GrooveNode;
import io.github.timkraeuter.util.FileTestHelper;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GrooveRuleGenerationTest {

  @BeforeEach
  void setUp() {
    GrooveNode.setIDCounter(-1);
  }

  @Test
  void generateAddNodeRuleTest() throws Exception {
    Path tempDir = Files.createTempDirectory("");

    GrooveRuleBuilder ruleBuilder = new GrooveRuleBuilder();
    ruleBuilder.startRule("addSingleNode");
    ruleBuilder.addNode("node");
    ruleBuilder.buildRule();
    GrooveRuleAndGraphWriter.writeRules(tempDir, ruleBuilder.getRules(), true);

    testRuleEquals("addSingleNode", tempDir);
  }

  @Test
  void generateDeleteNodeRuleTest() throws Exception {
    Path tempDir = Files.createTempDirectory("");

    GrooveRuleBuilder ruleBuilder = new GrooveRuleBuilder();
    ruleBuilder.startRule("deleteSingleNode");
    ruleBuilder.deleteNode("node");
    ruleBuilder.buildRule();
    GrooveRuleAndGraphWriter.writeRules(tempDir, ruleBuilder.getRules(), true);

    testRuleEquals("deleteSingleNode", tempDir);
  }

  @Test
  void generateNACNodeRuleTest() throws Exception {
    Path tempDir = Files.createTempDirectory("");

    GrooveRuleBuilder ruleBuilder = new GrooveRuleBuilder();
    ruleBuilder.startRule("nacSingleNode");
    ruleBuilder.nacNode("node");
    ruleBuilder.buildRule();
    GrooveRuleAndGraphWriter.writeRules(tempDir, ruleBuilder.getRules(), true);

    testRuleEquals("nacSingleNode", tempDir);
  }

  @Test
  void generateAddEdgeRuleTest() throws Exception {
    Path tempDir = Files.createTempDirectory("");

    GrooveRuleBuilder ruleBuilder = new GrooveRuleBuilder();
    ruleBuilder.startRule("addNodesWithEdge");
    GrooveNode source = ruleBuilder.addNode("source");
    GrooveNode target = ruleBuilder.addNode("target");
    ruleBuilder.addEdge("edge", source, target);
    ruleBuilder.buildRule();
    GrooveRuleAndGraphWriter.writeRules(tempDir, ruleBuilder.getRules(), true);

    testRuleEquals("addNodesWithEdge", tempDir);
  }

  @Test
  void generateContextEdgeRuleTest() throws Exception {
    Path tempDir = Files.createTempDirectory("");

    GrooveRuleBuilder ruleBuilder = new GrooveRuleBuilder();
    ruleBuilder.startRule("contextEdge");
    GrooveNode source = ruleBuilder.contextNode("source");
    GrooveNode target = ruleBuilder.contextNode("target");
    ruleBuilder.contextEdge("edge", source, target);
    ruleBuilder.buildRule();
    GrooveRuleAndGraphWriter.writeRules(tempDir, ruleBuilder.getRules(), true);

    testRuleEquals("contextEdge", tempDir);
  }

  @Test
  void generateNodeWithFlagTest() throws Exception {
    Path tempDir = Files.createTempDirectory("");

    GrooveRuleBuilder ruleBuilder = new GrooveRuleBuilder();
    ruleBuilder.startRule("nodeWithFlag");
    GrooveNode node = ruleBuilder.addNode("node");
    node.addFlag("root");
    ruleBuilder.buildRule();
    GrooveRuleAndGraphWriter.writeRules(tempDir, ruleBuilder.getRules(), true);

    testRuleEquals("nodeWithFlag", tempDir);
  }

  @Test
  void generateTwoRuleSyncTest() throws Exception {
    Path tempDir = Files.createTempDirectory("");

    GrooveRuleBuilder generator1 = new GrooveRuleBuilder();
    generator1.startRule("addEdge1");
    GrooveNode s1 = generator1.addNode("s1");
    GrooveNode t1 = generator1.addNode("t1");
    generator1.addEdge("edge1", s1, t1);
    GrooveGraphRule r1 = generator1.buildRule();

    generator1.startRule("addEdge2");
    GrooveNode s2 = generator1.addNode("s2");
    GrooveNode t2 = generator1.addNode("t2");
    generator1.addEdge("edge2", s2, t2);
    GrooveGraphRule r2 = generator1.buildRule();

    Map<String, Set<GrooveGraphRule>> nameToToBeSynchedRules = new LinkedHashMap<>();

    Set<GrooveGraphRule> toBeSynched =
        new LinkedHashSet<>(); // Fixed iteration order needed for the testcase.
    toBeSynched.add(r1);
    toBeSynched.add(r2);

    nameToToBeSynchedRules.put("twoRuleSynch", toBeSynched);
    Stream<GrooveGraphRule> synchedRules =
        GrooveRuleBuilder.createSyncedRules(nameToToBeSynchedRules);

    GrooveRuleAndGraphWriter.writeRules(tempDir, synchedRules, true);

    testRuleEquals("twoRuleSynch", tempDir);
  }

  @Test
  void generateThreeRuleSyncTest() throws Exception {
    Path tempDir = Files.createTempDirectory("");

    GrooveRuleBuilder generator1 = new GrooveRuleBuilder();
    generator1.startRule("addEdge1");
    GrooveNode s1 = generator1.addNode("s1");
    GrooveNode t1 = generator1.addNode("t1");
    generator1.addEdge("edge1", s1, t1);
    GrooveGraphRule r1 = generator1.buildRule();

    generator1.startRule("addEdge2");
    GrooveNode s2 = generator1.addNode("s2");
    GrooveNode t2 = generator1.addNode("t2");
    generator1.deleteNode("delete");
    generator1.addEdge("edge2", s2, t2);
    GrooveGraphRule r2 = generator1.buildRule();

    generator1.startRule("addEdge3");
    GrooveNode s3 = generator1.contextNode("s3");
    GrooveNode t3 = generator1.contextNode("t3");
    generator1.deleteEdge("edge3", s3, t3);
    GrooveGraphRule r3 = generator1.buildRule();

    Map<String, Set<GrooveGraphRule>> nameToToBeSynchedRules = new LinkedHashMap<>();

    Set<GrooveGraphRule> toBeSynched =
        new LinkedHashSet<>(); // Fixed iteration order needed for the testcase.
    toBeSynched.add(r1);
    toBeSynched.add(r2);
    toBeSynched.add(r3);

    nameToToBeSynchedRules.put("threeRuleSynch", toBeSynched);
    Stream<GrooveGraphRule> synchedRules =
        GrooveRuleBuilder.createSyncedRules(nameToToBeSynchedRules);

    GrooveRuleAndGraphWriter.writeRules(tempDir, synchedRules, true);

    testRuleEquals("threeRuleSynch", tempDir);
  }

  private static void testRuleEquals(String resource, Path tempDir) {
    String resourceRuleName = resource + ".gpr";
    Path expected_rule = getResource(resourceRuleName);
    Path generated_rule = Path.of(tempDir.toString(), resourceRuleName);
    FileTestHelper.testFileEquals(expected_rule, generated_rule);
  }
}
