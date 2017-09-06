package org.rapidpm.vaadin.server;


import static org.rapidpm.frp.vaadin.addon.component.testing.Infrastructure.undertowWithDefaultShiro;

import java.util.Optional;

import io.undertow.Undertow;

/**
 *
 */
public class Main {


  public static void start() {
    main(new String[0]);
  }

  public static void shutdown() {
    undertowOptional.ifPresent(Undertow::stop);
  }

  private static Optional<Undertow> undertowOptional;

  public static void main(String[] args) {

    undertowOptional = undertowWithDefaultShiro(
        Main.class.getClassLoader() ,
        MainServlet.class
    );

  }
}
