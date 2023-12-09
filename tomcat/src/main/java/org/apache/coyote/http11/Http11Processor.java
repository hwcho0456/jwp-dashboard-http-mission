package org.apache.coyote.http11;

import java.io.IOException;
import java.net.Socket;
import nextstep.jwp.exception.UncheckedServletException;
import org.apache.coyote.Processor;
import org.apache.coyote.http11.controller.Handler;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Http11Processor implements Runnable, Processor {

  private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

  private final Socket connection;

  public Http11Processor(final Socket connection) {
    this.connection = connection;
  }

  @Override
  public void run() {
    process(connection);
  }

  @Override
  public void process(final Socket connection) {
    try (final var inputStream = connection.getInputStream();
        final var outputStream = connection.getOutputStream()) {

      final HttpRequest request = new HttpRequest(inputStream);
      final HttpResponse response = new HttpResponse(outputStream);
      log.debug("method: {}", request.getMethod());
      log.debug("path: {}", request.getPath());
      log.debug("type: {}", request.getType());
      final Handler handler = new Handler();
      handler.handle(request, response);
      log.debug("changed method: {}", request.getMethod());
      log.debug("changed path: {}", request.getPath());
      log.debug("changed type: {}", request.getType());
      response.write();
    } catch (IOException | UncheckedServletException e) {
      log.error(e.getMessage(), e);
    }
  }
}
