package org.apache.coyote.http11;

import static nextstep.jwp.db.InMemoryUserRepository.findByAccount;
import static org.apache.coyote.http11.Constants.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import nextstep.jwp.exception.UncheckedServletException;
import nextstep.jwp.model.User;
import org.apache.catalina.Servlet;
import org.apache.coyote.Processor;
import org.apache.coyote.http11.Http11Response.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

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

            final BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
            final Http11Request request = Http11Utils.parse(input);

            if (!request.getOptions().containsKey(PROTOCOL)
                || !request.getOptions().containsKey(METHOD)
                || !request.getOptions().containsKey(PATH)) {
                final Http11Response response = new Builder().fromError(HttpStatus.BAD_REQUEST).build();
                outputStream.write(response.getResult().getBytes());
                outputStream.flush();
                return;
            }

          if (request.method.equals(HttpMethod.GET) && Objects.equals(
              request.getOptions().get(PATH), "/login") && request.getQueries()
              .containsKey("account") && request.getQueries().containsKey("password")) {
            final String account = request.getQueries().get("account");
            final String password = request.getQueries().get("password");
            final Optional<User> user = findByAccount(account);
            if (user.isPresent() && user.get().checkPassword(password)) {
              final Http11Response response = new Builder().fromRedirect("/index").build();
              outputStream.write(response.getResult().getBytes());
              outputStream.flush();
              return;
            } else {
              final Http11Response response = new Builder().fromError(HttpStatus.UNAUTHORIZED).build();
              outputStream.write(response.getResult().getBytes());
              outputStream.flush();
              return;
            }
          }

          if (request.method.equals(HttpMethod.GET)) {
                String path = STATIC + request.getOptions().get(PATH);
                String type = HTML;
                if (path.contains(DOT)) {
                    type = path.substring(path.lastIndexOf(DOT) + 1);
                } else {
                    path += DOT + type;
                }
                final URL resource = getClass().getClassLoader().getResource(path);
                if (resource != null) {
                    final Http11Response response = new Http11Response.Builder().fromStatic(resource, type).build();
                    outputStream.write(response.getResult().getBytes());
                    outputStream.flush();
                    return;
                }
            }

            Servlet servlet = Http11DynamicHandler.getServlet(request.getOptions().get(PATH), request.method);
            if (servlet != null) {
                final Http11Response response = new Http11Response.Builder().fromDynamic(servlet).build();
                outputStream.write(response.getResult().getBytes());
                outputStream.flush();
                return;
            }

            final Http11Response response = new Http11Response.Builder().fromError(HttpStatus.NOT_FOUND).build();
            outputStream.write(response.getResult().getBytes());
            outputStream.flush();

        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }
}
