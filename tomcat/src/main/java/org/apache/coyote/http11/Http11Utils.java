package org.apache.coyote.http11;

import static org.apache.coyote.http11.Constants.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Http11Utils {

  private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

  public static Http11Request parse(BufferedReader request) throws IOException {
    HashMap<String, String> options = new HashMap<>();
    while (true) {
      String line = request.readLine();
      if (line == null || line.isEmpty()) {
        break;
      }
      if (line.contains(COLON)) {
        String[] option = line.split(COLON, 2);
        options.put(option[0].trim(), option[1].trim());
      } else if (line.contains(SPACE)) {
        String[] context = line.split(SPACE);
        if (context.length == 3) {
          options.put(METHOD, context[0]);
          if (context[1].contains(QUESTION)) {
            String[] pathAndQuery = context[1].split(ESCAPE_QUESTION, 2);
            options.put(PATH, pathAndQuery[0]);
            options.put(QUERY, pathAndQuery[1]);
          } else {
            options.put(PATH, context[1]);
          }
          options.put(PROTOCOL, context[2]);
        }
      }
    }
    return new Http11Request(options);
  }


}
