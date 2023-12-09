package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.constants.HttpMethod;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;

public abstract class AbstractController {
  public void service(HttpRequest request, HttpResponse response) {
    if(request.getMethod().equals(HttpMethod.GET)) {
      doGet(request, response);
    } else if(request.getMethod().equals(HttpMethod.POST)) {
      doPost(request, response);
    }
  }
  protected abstract void doGet(HttpRequest request, HttpResponse response);
  protected abstract void doPost(HttpRequest request, HttpResponse response);
}
