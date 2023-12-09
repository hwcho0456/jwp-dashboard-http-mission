package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.constants.HttpStatus;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;

public class HomeController extends AbstractController {

  @Override
  protected void doGet(HttpRequest request, HttpResponse response) {
    response.setResponseLine(HttpStatus.OK);
    response.setBody("text/html;charset=utf-8", "Hello world!");
  }

  @Override
  protected void doPost(HttpRequest request, HttpResponse response) {
    response.setResponseLine(HttpStatus.OK);
    response.setBody("text/html;charset=utf-8", "Hello world!");
  }
}
