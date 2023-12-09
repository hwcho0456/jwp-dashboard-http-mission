package org.apache.coyote.http11.request;

import java.util.Map;
import java.util.TreeMap;
import org.apache.coyote.http11.Http11Processor;
import org.apache.coyote.http11.constants.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestLine {

  private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);
  private final HttpMethod method;
  private String path;
  private String type;

  private final Map<String, String> query = new TreeMap<>();
  private final String protocol;

  public RequestLine(String requestHeader) {
    String[] requestHeaderParts = requestHeader.split(" ", 3);
    this.method = HttpMethod.from(requestHeaderParts[0]);
    String[] pathAndQuery = requestHeaderParts[1].split("\\?", 2);
    if (pathAndQuery[0].contains(".")) {
      String[] pathAndType = pathAndQuery[0].split("\\.", 2);
      this.path = pathAndType[0];
      this.type = pathAndType[1];
    } else {
      this.path = pathAndQuery[0];
      this.type = null;
    }
    if (pathAndQuery.length == 2) {
      for (String queryString : pathAndQuery[1].split("&")) {
        String[] queryStringParts = queryString.split("=", 2);
        this.query.put(queryStringParts[0], queryStringParts[1]);
      }
    }
    this.protocol = requestHeaderParts[2];
  }

  public HttpMethod getMethod() {
    return method;
  }

  public String getPath() {
    return path;
  }
  public String getType() {
    return type;
  }
  public Map<String, String> getQuery() {
    return query;
  }

  public String getProtocol() {
    return protocol;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
