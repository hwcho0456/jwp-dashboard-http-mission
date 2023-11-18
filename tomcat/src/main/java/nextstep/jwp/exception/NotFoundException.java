package nextstep.jwp.exception;

public class NotFoundException extends RuntimeException {
  public NotFoundException(Exception e) {
    super(e);
  }
}
