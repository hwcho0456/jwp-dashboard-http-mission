package org.apache.catalina;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionContext;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.apache.coyote.http11.Http11Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Session implements HttpSession {
  private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);
  final private long creationTime;
  private int maxInactiveInterval = 60;
  private String jSessionId;
  private long lastAccessedTime;
  private boolean invalidated = true;
  private Map<String, Object> attributes;
  public Session(String jSessionId) {
    this.creationTime = System.currentTimeMillis();
    this.lastAccessedTime = creationTime;
    this.jSessionId = jSessionId;
    this.attributes = new HashMap<>();
  }
  @Override
  public long getCreationTime() {
    return this.creationTime;
  }

  @Override
  public String getId() {
    return this.jSessionId;
  }

  @Override
  public long getLastAccessedTime() {
    return this.lastAccessedTime;
  }

  @Override
  public ServletContext getServletContext() {
    return null;
  }

  @Override
  public void setMaxInactiveInterval(int interval) {
    this.maxInactiveInterval = interval;
  }

  @Override
  public int getMaxInactiveInterval() {
    return this.maxInactiveInterval;
  }

  @Override
  public HttpSessionContext getSessionContext() {
    return null;
  }

  @Override
  public Object getAttribute(String name) {
    return this.attributes.get(name);
  }

  @Override
  public Object getValue(String name) {
    return getAttribute(name);
  }

  @Override
  public Enumeration<String> getAttributeNames() {
    return Collections.enumeration(this.attributes.keySet());
  }

  @Override
  public String[] getValueNames() {
    return this.attributes.keySet().toArray(new String[0]);
  }

  @Override
  public void setAttribute(String name, Object value) {
    this.attributes.put(name, value);
  }

  @Override
  public void putValue(String name, Object value) {
    setAttribute(name, value);
  }

  @Override
  public void removeAttribute(String name) {
    this.attributes.remove(name);
  }

  @Override
  public void removeValue(String name) {
    removeAttribute(name);
  }

  @Override
  public void invalidate() {
    this.attributes.clear();
    this.invalidated = false;
  }

  public boolean isInvalidated() {
    log.debug("creationTime: {}", this.creationTime);
    log.debug("maxInactiveInterval: {}", this.maxInactiveInterval);
    log.debug("lastAccessedTime: {}", this.lastAccessedTime);
    log.debug("System.currentTimeMillis(): {}", System.currentTimeMillis());
    log.debug("this.creationTime + this.maxInactiveInterval * 1000L: {}", this.creationTime + this.maxInactiveInterval * 1000L);
    if (this.creationTime + this.maxInactiveInterval * 1000L < System.currentTimeMillis()) {
      this.invalidated = false;
    }
    log.debug("this.invalidated: {}", this.invalidated);
    return this.invalidated;
  }
  @Override
  public boolean isNew() {
    return this.creationTime == this.lastAccessedTime;
  }

  public void setLastAccessedTime() {
    this.lastAccessedTime = System.currentTimeMillis();
  }
}
