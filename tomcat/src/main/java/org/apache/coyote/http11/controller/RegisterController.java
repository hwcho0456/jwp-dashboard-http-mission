package org.apache.coyote.http11.controller;

import static nextstep.jwp.db.InMemoryUserRepository.findByAccount;

import java.util.Optional;
import java.util.UUID;
import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;
import org.apache.coyote.http11.Http11Processor;
import org.apache.coyote.http11.constants.HttpStatus;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterController extends AbstractController {
  private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);
  @Override
  protected void doGet(HttpRequest request, HttpResponse response) {
    final AbstractController controller = new StaticController();
    request.setPath("/register");
    controller.service(request, response);
  }
  @Override
  protected void doPost(HttpRequest request, HttpResponse response) {
    final String account = request.getBody().get("account");
    final String email = request.getBody().get("email");
    final String pw = request.getBody().get("password");
    final User user = new User(account, pw, email);
    InMemoryUserRepository.save(user);
    response.setResponseLine(HttpStatus.REDIRECT);
    response.setHeader("Location", "/login.html");
  }
}
