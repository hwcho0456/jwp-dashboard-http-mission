package org.apache.coyote.http11;

import java.util.HashMap;
import org.apache.catalina.Servlet;

public class Http11RedirectHandler {
  private static final Http11RedirectHandler instance = new Http11RedirectHandler();

  static HashMap<RequestKey, String> redirectHashMap = new HashMap<>();
}
