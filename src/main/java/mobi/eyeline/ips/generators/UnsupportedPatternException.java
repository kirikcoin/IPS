package mobi.eyeline.ips.generators;

public class UnsupportedPatternException extends Exception {
  public UnsupportedPatternException() {
  }

  public UnsupportedPatternException(String message) {
    super(message);
  }

  public UnsupportedPatternException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnsupportedPatternException(Throwable cause) {
    super(cause);
  }
}
