package org.rapidpm.vaadin.server.ddi;

import javax.annotation.PostConstruct;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;

/**
 *
 */

@WebServlet(value = "/*", loadOnStartup = 1)
@VaadinServletConfiguration(productionMode = false, ui = MainUI.class )
public class MainServlet extends VaadinServlet {


  @Override
  protected VaadinServletService createServletService(final DeploymentConfiguration deploymentConfiguration)
      throws ServiceException {
    final DDIVaadinServletService service
        = new DDIVaadinServletService(this, deploymentConfiguration);
    service.init();
    return service;
  }

  @PostConstruct
  private void postConstruct(){
    System.out.println("DDi activated for MainServlet ");
  }
}


