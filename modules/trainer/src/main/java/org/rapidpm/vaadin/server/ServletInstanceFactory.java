package org.rapidpm.vaadin.server;

import javax.servlet.Servlet;

import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.api.InstanceHandle;

/**
 *
 */
public class ServletInstanceFactory implements InstanceFactory<Servlet> {


  private final Class<? extends Servlet> servletClass;

  public ServletInstanceFactory(Class<? extends Servlet> servletClass) {
    this.servletClass = servletClass;
  }

  @Override
  public InstanceHandle<Servlet> createInstance() throws InstantiationException {
    return new InstanceHandle<Servlet>() {
      @Override
      public Servlet getInstance() {
        try {
          return servletClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
          e.printStackTrace();
        }
        return null;
      }

      @Override
      public void release() {
        //release ???
      }
    };
  }
}