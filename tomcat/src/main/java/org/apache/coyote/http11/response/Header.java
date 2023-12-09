package org.apache.coyote.http11.response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.TreeMap;
import org.apache.coyote.http11.constants.HttpStatus;

public class Header {
  Map<String, String> header = new TreeMap<>();
  String ContentType = "text/html;charset=utf-8";
  int ContentLength = 0;
  public String get(){
    StringBuilder header = new StringBuilder();
    this.header.forEach((key, value) -> {
      header.append(String.format("%s: %s \r\n", key, value));
    });
    header.append(String.format("Content-Type: %s \r\n", ContentType));
    header.append(String.format("Content-Length: %s \r\n", ContentLength));
    return header.toString();
  }

  public void setHeader(String key, String value) {
    header.put(key, value);
  }

  public void setContentType(String contentType) {
    this.ContentType = contentType;
  }

  public void setContentLength(int contentLength) {
    this.ContentLength = contentLength;
  }
}
