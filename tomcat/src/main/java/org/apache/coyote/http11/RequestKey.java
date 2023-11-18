package org.apache.coyote.http11;

import java.util.Objects;

public class RequestKey {
  private String path;
  private HttpMethod method;

  public RequestKey(String path, HttpMethod method) {
    this.path = path;
    this.method = method;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RequestKey that = (RequestKey) o;
    return Objects.equals(path, that.path) &&
        Objects.equals(method, that.method);
  }

  @Override
  public int hashCode() {
    return Objects.hash(path, method);
  }
}
