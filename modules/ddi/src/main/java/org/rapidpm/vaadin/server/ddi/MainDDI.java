package org.rapidpm.vaadin.server.ddi;

import static org.rapidpm.vaadin.server.ddi.Infrastructure.undertowWithDefaultShiro;

import java.util.Optional;

import org.rapidpm.ddi.DI;
import org.rapidpm.vaadin.trainer.UIFunctions;
import io.undertow.Undertow;

/**
 *
 */
public class MainDDI {


  public static void start() {
    main(new String[0]);
  }

  public static void shutdown() {
    undertowOptional.ifPresent(Undertow::stop);
  }

  private static Optional<Undertow> undertowOptional;

  public static void main(String[] args) {

    DI.activatePackages(MainDDI.class);
    DI.activatePackages(UIFunctions.class);

    undertowOptional = undertowWithDefaultShiro(
        MainDDI.class.getClassLoader() ,
        MainServlet.class
    );


  }
}
