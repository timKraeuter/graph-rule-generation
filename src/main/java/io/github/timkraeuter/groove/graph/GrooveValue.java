package io.github.timkraeuter.groove.graph;

/** Represent a value in groove. */
public class GrooveValue {

  private final String typeName;
  private final String value;

  /**
   * Create a Groove Value.
   *
   * @param typeName typeName.
   * @param value value.
   */
  protected GrooveValue(String typeName, String value) {
    this.typeName = typeName;
    this.value = value;
  }

  /**
   * Returns the TypeName.
   *
   * @return TypeName of the value.
   */
  public String getTypeName() {
    return typeName;
  }

  /**
   * Returns the value.
   *
   * @return Value as string.
   */
  public String getValue() {
    return value;
  }
}
