package org.apache.catalina.sevlet;

import org.apache.catalina.Servlet;
import org.apache.catalina.ServletReturn;
import org.apache.coyote.http11.HttpStatus;

public class RegisterServlet implements Servlet {
  @Override
  public ServletReturn execute() {

    return new ServletReturn(HttpStatus.OK, "html", "Hello World!");
  }
}