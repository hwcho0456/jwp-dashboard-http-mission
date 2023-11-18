package org.apache.coyote.http11;

import static org.apache.coyote.http11.Constants.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;
import nextstep.jwp.exception.UncheckedServletException;
import org.apache.catalina.Servlet;
import org.apache.catalina.ServletReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Http11Response {

  private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);
  private String result;

  private Http11Response() {
  }

  public static class Builder {
    private int code;
    private String status;
    private String contentType;
    private String responseBody;

    public Builder fromError(HttpStatus status) {
      this.code = status.getCode();
      this.status = status.getReason();
      this.contentType = HTML;
      final String path = STATIC + SLASH + code + DOT + HTML;
      URL resource = getClass().getClassLoader().getResource(path);
      if (resource != null) {
        try (InputStream in = resource.openStream()) {
          this.responseBody = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
          this.responseBody = "Error";
        }
      } else {
        this.responseBody = "Error";
      }

      return this;
    }

    public Builder fromRedirect(String path) {
      this.code = HttpStatus.REDIRECT.getCode();
      this.status = HttpStatus.REDIRECT.getReason();
      this.contentType = HTML;
      this.responseBody = String.format("<html><head><meta http-equiv=\"refresh\" content=\"0; url=%s\" /></head></html>", path);
      return this;
    }
    public Builder fromStatic(URL resource, String type) throws IOException {
      this.contentType = type;
      try (InputStream in = resource.openStream()) {
        this.responseBody = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        this.code = 200;
        this.status = HttpStatus.OK.getReason();
      }
      return this;
    }

    public Builder fromDynamic(Servlet servlet) throws IOException, UncheckedServletException {

      final ServletReturn result = servlet.execute();
      final HttpStatus status = result.getStatus();
      this.contentType = result.getType();
      this.responseBody = result.getBody();
      this.code = status.getCode();
      this.status = status.getReason();

      return this;
    }
    public Http11Response build() {
      Http11Response response = new Http11Response();
      response.result =
          String.join(CRLF,
              String.format("%s %s %s", VERSION, code, status),
              String.format("Content-Type: text/%s;charset=utf-8 ", contentType),
              "Content-Length: " + responseBody.getBytes().length + " ",
              "",
              responseBody);
      return response;
    }
  }

  public String getResult() {
    return result;
  }
}
