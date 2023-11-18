package org.apache.catalina;

import org.apache.coyote.http11.HttpStatus;

public class ServletReturn {
  final HttpStatus status;
  final String type;
  final String body;

  public ServletReturn(HttpStatus status, String type, String body) {
    this.status = status;
    this.type = type;
    this.body = body;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public String getType() {
    return type;
  }

  public String getBody() {
    return body;
  }
}
