package org.rapidpm.vaadin.server.ddi;

import javax.servlet.Servlet;

import org.rapidpm.ddi.DI;
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
        return DI.activateDI(servletClass);
      }

      @Override
      public void release() {
        //release ???
      }
    };
  }
}