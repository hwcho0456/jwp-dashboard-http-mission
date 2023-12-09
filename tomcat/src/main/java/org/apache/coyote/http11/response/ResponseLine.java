package org.apache.coyote.http11.response;

import org.apache.coyote.http11.constants.HttpStatus;

public class ResponseLine {

  String version = "HTTP/1.1";
  HttpStatus status;

  public String get() {
    return String.format("%s %s %s \r\n", version, status.getCode(), status.getReason());
  }

  public void setStatusCode(HttpStatus status) {
    this.status = status;
  }

  public HttpStatus getStatusCode() {
    return status;
  }
}
