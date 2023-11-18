package org.apache.coyote.http11;

import java.util.HashMap;
import org.apache.catalina.Servlet;

public class Http11DynamicHandler {
  private static final Http11DynamicHandler instance = new Http11DynamicHandler();
  static HashMap<RequestKey, Servlet> servletHashMap = new HashMap<>();

  private Http11DynamicHandler() {}

  public static Http11DynamicHandler getInstance() {
    return instance;
  }
  public static Servlet getServlet(String s, HttpMethod method) {
    return servletHashMap.get(new RequestKey(s, method));
  }

  public void addServlet(RequestKey requestKey, Servlet servlet) {
    servletHashMap.put(requestKey, servlet);
  }

}
