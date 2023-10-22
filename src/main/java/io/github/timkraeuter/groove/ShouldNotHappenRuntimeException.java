package io.github.timkraeuter.groove;

/** Runtime exception which should never happen. */
public class ShouldNotHappenRuntimeException extends RuntimeException {

  /**
   * Wraps an exception.
   *
   * @param e wrapped exception.
   */
  public ShouldNotHappenRuntimeException(Exception e) {
    super(e);
  }
}
