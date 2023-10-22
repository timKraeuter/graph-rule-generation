package io.github.timkraeuter.groove.graph;

import io.github.timkraeuter.api.GraphNode;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/** Node in groove. */
public class GrooveNode implements GraphNode {
  private static final AtomicLong idCounter = new AtomicLong(-1);
  private final String id;
  private final String name;
  private final Set<String> flags;
  private final Map<String, GrooveValue> attributes;

  /**
   * Create a new node.
   *
   * @param name name.
   */
  public GrooveNode(String name) {
    this.id = getNextNodeId();
    this.name = name;
    this.flags = new LinkedHashSet<>();
    this.attributes = new LinkedHashMap<>();
  }

  private static String getNextNodeId() {
    return "n" + idCounter.incrementAndGet();
  }

  /**
   * Set the id counter.
   *
   * @param counter new value.
   */
  public static void setIDCounter(int counter) {
    idCounter.set(counter);
  }

  @Override
  public String getId() {
    return this.id;
  }

  /**
   * Get the name.
   *
   * @return name.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Get all flags.
   *
   * @return flags.
   */
  public Set<String> getFlags() {
    return this.flags;
  }

  /**
   * Add a flag.
   *
   * @param flag flag.
   */
  public void addFlag(String flag) {
    this.flags.add(flag);
  }

  /**
   * Get attributes of the node.
   *
   * @return attributes.
   */
  public Map<String, GrooveValue> getAttributes() {
    return this.attributes;
  }

  /**
   * Add an attribute.
   *
   * @param name name
   * @param value value (String)
   */
  public void addAttribute(String name, String value) {
    this.attributes.put(name, new GrooveValue("string", String.format("\"%s\"", value)));
  }

  /**
   * Add an attribute.
   *
   * @param name name
   * @param value value (int)
   */
  public void addAttribute(String name, int value) {
    this.attributes.put(name, new GrooveValue("int", String.valueOf(value)));
  }

  /**
   * Add an attribute.
   *
   * @param name name
   * @param value value (boolean)
   */
  public void addAttribute(String name, boolean value) {
    this.attributes.put(name, new GrooveValue("bool", String.valueOf(value)));
  }

  @Override
  public String toString() {
    return this.name;
  }
}
