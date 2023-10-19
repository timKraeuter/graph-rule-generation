package io.github.timKraeuter.groove.graph;

public class GrooveValue {

  private final String typeName;
  protected final String value;

  protected GrooveValue(String typeName, String value) {
    this.typeName = typeName;
    this.value = value;
  }

  public String getTypeName() {
    return typeName;
  }

  public String getValue() {
    return value;
  }
}
