package org.apache.coyote.http11.controller;

import static nextstep.jwp.db.InMemoryUserRepository.findByAccount;

import java.io.IOException;
import java.util.Optional;
import nextstep.jwp.model.User;
import org.apache.catalina.Session;
import org.apache.catalina.SessionManager;
import org.apache.coyote.http11.Http11Processor;
import org.apache.coyote.http11.constants.HttpStatus;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexController extends AbstractController {
  private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);
  @Override
  protected void doGet(HttpRequest request, HttpResponse response) {
    final SessionManager sessionManager = new SessionManager();
    String jSessionId = request.getSession();
    if (jSessionId == null || !sessionManager.checkSession(jSessionId)) {
      final AbstractController controller = new ErrorController();
      response.setResponseLine(HttpStatus.UNAUTHORIZED);
      controller.service(request, response);
      return;
    }
    log.debug("session id: {}", jSessionId);
    try {
      final Session session = (Session) sessionManager.findSession(jSessionId);
      session.setLastAccessedTime();
      if (!session.isInvalidated()) {
        final AbstractController controller = new ErrorController();
        response.setResponseLine(HttpStatus.UNAUTHORIZED);
        controller.service(request, response);
        return;
      }
      final String account = (String) session.getAttribute("account");
      log.debug("account: {}", account);
      final Optional<User> user = findByAccount(account);
      if (user.isEmpty()) {
        final AbstractController controller = new ErrorController();
        response.setResponseLine(HttpStatus.UNAUTHORIZED);
        controller.service(request, response);
        return;
      }
    } catch (IOException e) {
      final AbstractController controller = new ErrorController();
      response.setResponseLine(HttpStatus.INTERNAL_SERVER_ERROR);
      controller.service(request, response);
      return;
    }
    final AbstractController controller = new StaticController();
    controller.service(request, response);
  }

  @Override
  protected void doPost(HttpRequest request, HttpResponse response) {
    final AbstractController controller = new ErrorController();
    response.setResponseLine(HttpStatus.NOT_FOUND);
    controller.service(request, response);
  }
}
