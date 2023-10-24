[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.timKraeuter/graph-rule-generation/badge.svg)](https://mvnrepository.com/artifact/io.github.timKraeuter/graph-rule-generation)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=timKraeuter_graph-rule-generation&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=timKraeuter_graph-rule-generation)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=timKraeuter_graph-rule-generation&metric=coverage)](https://sonarcloud.io/summary/new_code?id=timKraeuter_graph-rule-generation)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=timKraeuter_graph-rule-generation&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=timKraeuter_graph-rule-generation)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=timKraeuter_graph-rule-generation&metric=bugs)](https://sonarcloud.io/summary/new_code?id=timKraeuter_graph-rule-generation)

# graph-rule-generation

Available on [Maven Central](https://mvnrepository.com/artifact/io.github.timKraeuter/graph-rule-generation) and faster indexing on [SonaType](https://central.sonatype.com/artifact/io.github.timKraeuter/graph-rule-generation).

# Usage

This library allows users to programmatically generate graphs, graph transformation rules, and even _entire_ graph transformation systems. Currently, it targets the graph transformation tool [Groove](https://groove.ewi.utwente.nl/).

## Generating graphs:
Graphs are created using the _GrooveGraphBuilder_ and can be written for Groove via the _GrooveRuleAndGraphWriter_. 
Here is an example of how to use the builder:
```java
GrooveGraphBuilder builder = new GrooveGraphBuilder();
GrooveNode a = new GrooveNode("A");
GrooveNode b = new GrooveNode("B");
GrooveGraph graph = builder.name("Graph")
    .addNode(a)
    .addNode(b)
    .addEdge("A to B", a, b)
    .build();
```

## Generating graph transformation rules:
Graphs are created using the _GrooveRuleBuilder_ and can be written for Groove via the _GrooveRuleAndGraphWriter_. 
Here is an example of how to use the builder:
```java
GrooveRuleBuilder ruleBuilder = new GrooveRuleBuilder();
ruleBuilder.startRule("sampleRule");
// Adding nodes and edges
GrooveNode a = ruleBuilder.addNode("A");
GrooveNode b = ruleBuilder.addNode("B");
ruleBuilder.addEdge("A to B", a, b);
// Deleting nodes and edges
GrooveNode c = ruleBuilder.deleteNode("C");
GrooveNode d = ruleBuilder.deleteNode("D");
ruleBuilder.deleteEdge("C to D", c, d);
// Context nodes and edges
GrooveNode e = ruleBuilder.deleteNode("E");
GrooveNode f = ruleBuilder.deleteNode("F");
ruleBuilder.deleteEdge("E to F", e, f);
// NAC nodes
GrooveNode g = ruleBuilder.nacNode("G");
GrooveNode h = ruleBuilder.nacNode("H");
ruleBuilder.deleteEdge("G to H", g, h);

GrooveGraphRule gtRule = ruleBuilder.buildRule();
```

## Generating graph transformation systems:

```java

```

# Motivation

The tool is used in my PhD research see [code](https://github.com/timKraeuter/Rewrite_Rule_Generation) or my [website](https://timkraeuter.com/about/).
