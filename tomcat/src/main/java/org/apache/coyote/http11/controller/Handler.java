package org.apache.coyote.http11.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import nextstep.jwp.exception.UncheckedServletException;
import org.apache.coyote.http11.Http11Processor;
import org.apache.coyote.http11.constants.HttpStatus;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Handler {
  private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

  static final HashMap<String, Class<? extends AbstractController>> mapping = new HashMap<>(
      Map.of(
          "/", HomeController.class,
          "/index", IndexController.class,
          "/login", LoginController.class,
          "/register", RegisterController.class
      )
  );
  public void handle(HttpRequest request, HttpResponse response)
      throws UncheckedServletException {
    AbstractController controller = null;
    try {
      Class<? extends AbstractController> controllerClass = mapping.get(request.getPath());
      if (controllerClass != null) {
        controller = controllerClass.getDeclaredConstructor().newInstance();
        log.debug("controller: {}", controller.getClass().getName());
      }
    } catch (Exception e) {
      throw new UncheckedServletException(e);
    }
    if (controller == null) {
      controller = new StaticController();
    }
    controller.service(request, response);
  }
}
