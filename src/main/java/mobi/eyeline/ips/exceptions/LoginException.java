package mobi.eyeline.ips.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: maxim.romanovsky
 * Date: 22.04.11
 * Time: 12:36
 */
public class LoginException extends Exception {
  public LoginErrorKind kind;

  public enum LoginErrorKind {
    NotFoundUser,
    WrongPassword

  }

  public LoginException(LoginErrorKind kind) {
    super();
    this.kind = kind;
  }
}
