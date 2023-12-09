package org.apache.coyote.http11.request;

import java.util.Map;
import java.util.TreeMap;
import org.apache.coyote.http11.Http11Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Body {
  Map<String, String> body = new TreeMap<>();
  private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

  public Body(String body) {
    if (body.isEmpty()) {
      return;
    }
    log.debug("request body: {}", body);
    String[] bodyParts = body.strip().split("&");
    for (String bodyPart : bodyParts) {
      String[] bodyKeyValue = bodyPart.split("=", 2);
      this.body.put(bodyKeyValue[0], bodyKeyValue[1]);
    }
  }

  public Map<String, String> getBody() {
    return body;
  }
}
