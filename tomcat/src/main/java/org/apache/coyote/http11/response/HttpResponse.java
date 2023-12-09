package org.apache.coyote.http11.response;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.coyote.http11.Http11Processor;
import org.apache.coyote.http11.constants.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpResponse {

  private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

  final OutputStream outputStream;
  final ResponseLine responseLine = new ResponseLine();
  final Header header = new Header();
  final Body body = new Body();

  public HttpResponse(OutputStream outputStream) {
    this.outputStream = outputStream;
  }

  public void write() throws IOException {
    final var response =
        responseLine.get()
            + header.get()
            + "\r\n"
            + body.get();
    outputStream.write(response.getBytes());
    outputStream.flush();
  }

  public void setResponseLine(HttpStatus code) {
    this.responseLine.setStatusCode(code);
  }

  public void setHeader(String key, String value) {
    this.header.setHeader(key, value);
  }

  public void setBody(String contentType, String body) {
    this.header.setContentType(contentType);
    this.header.setContentLength(body.length());
    this.body.setBody(body);
  }

  public HttpStatus getStatus() {
    return responseLine.getStatusCode();
  }
}
