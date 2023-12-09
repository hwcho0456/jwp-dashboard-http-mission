package org.apache.coyote.http11.request;

import java.util.Map;
import java.util.TreeMap;

public class Headers {
  Map<String, String> headers = new TreeMap<>();
  public Headers(String header) {
    String[] headerParts = header.split("\r\n");
    for (String headerPart : headerParts) {
      String[] headerKeyValue = headerPart.split(": ", 2);
      headers.put(headerKeyValue[0], headerKeyValue[1]);
    }
  }

  public String getValue(String key) {
    return headers.get(key);
  }

  public Map<String, String> getCookie() {
    Map<String, String> cookie = new TreeMap<>();
    String cookieHeader = headers.get("Cookie");
    if (cookieHeader == null) {
      return cookie;
    }
    String[] cookieParts = cookieHeader.split("; ");
    for (String cookiePart : cookieParts) {
      String[] cookieKeyValue = cookiePart.split("=", 2);
      cookie.put(cookieKeyValue[0], cookieKeyValue[1]);
    }
    return cookie;
  }
}
