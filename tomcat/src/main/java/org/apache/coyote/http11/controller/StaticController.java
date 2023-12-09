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

public class StaticController extends AbstractController {

  private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

  @Override
  public void service(HttpRequest request, HttpResponse response) {
    try {
      if (request.getType() == null) {
        request.setType("html");
      }
      log.debug("static request path: {}", request.getPath());
      log.debug("static request type: {}", request.getType());
      final String path =
          "static"
              + request.getPath()
              + "."
              + request.getType();
      final String type = "text/" + request.getType();
      final URL resource = getClass().getClassLoader().getResource(path);
      String body;
      if (resource == null) {
        response.setResponseLine(HttpStatus.NOT_FOUND);
        final AbstractController controller = new ErrorController();
        controller.service(request, response);
        return;
      }
      log.debug("request resource: {}", resource);
      body = new String(Files.readAllBytes(new File(resource.getFile()).toPath()));
      response.setResponseLine(HttpStatus.OK);
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
