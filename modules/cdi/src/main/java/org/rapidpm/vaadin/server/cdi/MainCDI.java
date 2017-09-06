package org.rapidpm.vaadin.server.cdi;

import static org.rapidpm.vaadin.server.cdi.Infrastructure.undertowWithDefaultShiro;

import java.util.Optional;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.rapidpm.vaadin.trainer.UIFunctions;
import io.undertow.Undertow;

/**
 *
 */
public class MainCDI {


  public static WeldContainer weldContainer;

  public static void start() {
    main(new String[0]);
  }

  public static void shutdown() {
    undertowOptional.ifPresent(Undertow::stop);
  }

  private static Optional<Undertow> undertowOptional;

  public static void main(String[] args) {

    weldContainer = new Weld()
        .disableDiscovery()
        .addPackage(true , MainCDI.class)
        .addPackage(true , UIFunctions.class)
        .initialize();

    undertowOptional = undertowWithDefaultShiro(
        MainCDI.class.getClassLoader() ,
        MainServlet.class
    );


  }
}
