package org.apache.coyote.http11.response;

import java.io.OutputStream;
import java.util.HashMap;

public class Body {
  String body = "";

  public String get() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }
}
