package org.apache.coyote.http11.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import org.apache.coyote.http11.Http11Processor;
import org.apache.coyote.http11.constants.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequest {
  private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

  final RequestLine requestLine;
  final Headers headers;
  final Body body;

  public HttpRequest(InputStream input) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
    this.requestLine = new RequestLine(bufferedReader.readLine());
    final StringBuilder headerBuilder = new StringBuilder();
    while (true) {
      final String line = bufferedReader.readLine();
      if (line == null || line.isEmpty()) {
        break;
      }
      headerBuilder.append(line).append("\r\n");
    }
    this.headers = new Headers(headerBuilder.toString());
    String contentLength = headers.getValue("Content-Length");
    if (contentLength != null) {
      final int length = Integer.parseInt(contentLength);
      final char[] bodyChars = new char[length];
      bufferedReader.read(bodyChars, 0, length);
      this.body = new Body(new String(bodyChars));
    } else {
      this.body = new Body("");
    }
  }

  public HttpMethod getMethod() {
    return requestLine.getMethod();
  }

  public String getPath() {
    return requestLine.getPath();
  }
  public String getType() {
    return requestLine.getType();
  }

  public Map<String, String> getQuery() {
    return requestLine.getQuery();
  }

  public void setType(String type) {
    requestLine.setType(type);
  }

  public void setPath(String path) {
    requestLine.setPath(path);
  }

  public String getSession() {
    Map<String, String> cookie = headers.getCookie();
    return cookie.get("JSESSIONID");
  }

  public Map<String, String> getBody() {
    return body.getBody();
  }
}
