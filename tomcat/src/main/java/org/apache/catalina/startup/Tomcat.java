package org.apache.catalina.startup;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.sevlet.HelloWorldServlet;
import org.apache.coyote.http11.Http11DynamicHandler;
import org.apache.coyote.http11.HttpMethod;
import org.apache.coyote.http11.RequestKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Tomcat {

    private static final Logger log = LoggerFactory.getLogger(Tomcat.class);

    public void start() {
        var connector = new Connector();
        connector.start();
        Http11DynamicHandler.getInstance().addServlet(new RequestKey("/", HttpMethod.GET), new HelloWorldServlet());
        try {
            // make the application wait until we press any key.
            System.in.read();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            log.info("web server stop.");
            connector.stop();
        }
    }
}
