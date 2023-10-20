package io.github.timkraeuter.groove;

public class ShouldNotHappenRuntimeException extends RuntimeException {

  public ShouldNotHappenRuntimeException(Exception e) {
    super(e);
  }
}
