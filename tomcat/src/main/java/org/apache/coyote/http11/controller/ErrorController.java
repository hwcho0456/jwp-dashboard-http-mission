package org.apache.coyote.http11.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import org.apache.coyote.http11.Http11Processor;
import org.apache.coyote.http11.constants.HttpStatus;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorController extends AbstractController {

  private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

  @Override
  public void service(HttpRequest request, HttpResponse response) {
    HttpStatus error = response.getStatus();
    request.setType("html");
    final String path =
        "static/"
            + error.getCode()
            + "."
            + request.getType();
    final String type = "text/" + request.getType();
    final URL resource = getClass().getClassLoader().getResource(path);
    try {
      assert resource != null;
      final String body = new String(Files.readAllBytes(new File(resource.getFile()).toPath()));
      response.setResponseLine(error);
      response.setBody(type, body);
    } catch (IOException e) {
      response.setResponseLine(HttpStatus.INTERNAL_SERVER_ERROR);
      final AbstractController controller = new ErrorController();
      controller.service(request, response);
    }
  }

  @Override
  protected void doGet(HttpRequest request, HttpResponse response) {
    return;
  }

  @Override
  protected void doPost(HttpRequest request, HttpResponse response) {
    return;
  }
}
