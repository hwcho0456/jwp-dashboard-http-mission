package org.apache.coyote.http11.controller;

import static nextstep.jwp.db.InMemoryUserRepository.findByAccount;

import java.util.Optional;
import java.util.UUID;
import nextstep.jwp.model.User;
import org.apache.catalina.Session;
import org.apache.catalina.SessionManager;
import org.apache.coyote.http11.Http11Processor;
import org.apache.coyote.http11.constants.HttpStatus;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController extends AbstractController {
  private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);
  @Override
  protected void doGet(HttpRequest request, HttpResponse response) {
    log.debug("login request type: {}", request.getType());
    final AbstractController controller = new StaticController();
    controller.service(request, response);
  }
  @Override
  protected void doPost(HttpRequest request, HttpResponse response) {
    final String account = request.getBody().get("account");
    final String pw = request.getBody().get("password");
    final Optional<User> user = findByAccount(account);
    if (user.isPresent() && user.get().checkPassword(pw)) {
      final String jSessionId = UUID.randomUUID().toString();
      final Session session = new Session(jSessionId);
      session.putValue("account", user.get().getAccount());
      final SessionManager sessionManager = new SessionManager();
      sessionManager.add(session);
      response.setResponseLine(HttpStatus.REDIRECT);
      response.setHeader("Set-Cookie", "JSESSIONID=" + jSessionId);
      response.setHeader("Location", "/index");
    } else {
      response.setResponseLine(HttpStatus.UNAUTHORIZED);
      final AbstractController controller = new ErrorController();
      controller.service(request, response);
    }
  }
}
