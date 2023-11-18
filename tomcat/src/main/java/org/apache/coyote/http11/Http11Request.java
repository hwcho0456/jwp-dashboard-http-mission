package org.apache.coyote.http11;

import static org.apache.coyote.http11.Constants.*;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Http11Request {
  private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);
  HttpMethod method;
  HashMap<String, String> queries = new HashMap<>();
  HashMap<String, String> options;

  public Http11Request(HashMap<String, String> options) {
    this.options = options;
    this.method = HttpMethod.fromString(options.get(Constants.METHOD));
    if (options.containsKey(QUERY)) {
      String[] query = options.get(QUERY).split(AND);
      for (String q : query) {
        String[] keyValue = q.split(EQUAL);
        queries.put(keyValue[0], keyValue[1]);
      }
      log.debug("queries: {}", queries);
    }
  }

  public Map<String, String> getOptions() {
    return options;
  }

  public Map<String, String> getQueries() {
    return queries;
  }
}
