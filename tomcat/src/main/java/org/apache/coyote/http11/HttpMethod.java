package org.apache.coyote.http11;

public enum HttpMethod {
  GET, POST;

  public static HttpMethod fromString(String method) {
    switch (method) {
      case "GET":
        return GET;
      case "POST":
        return POST;
      default:
        throw new IllegalArgumentException("Unknown HTTP method: " + method);
    }
  }
}
