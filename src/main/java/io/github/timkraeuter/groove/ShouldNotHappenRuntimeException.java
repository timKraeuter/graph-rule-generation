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

  /**
   * Exception with a message.
   *
   * @param message message.
   * @param e exception
   */
  public ShouldNotHappenRuntimeException(String message, Exception e) {
    super(message, e);
  }
}
