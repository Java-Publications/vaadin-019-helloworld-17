package org.rapidpm.vaadin.server.cdi;

import com.vaadin.server.DefaultUIProvider;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.ui.UI;

/**
 *
 */
public class CDIVaadinServletService extends VaadinServletService {


  public CDIVaadinServletService(VaadinServlet servlet ,
                                 DeploymentConfiguration deploymentConfiguration)
      throws ServiceException {

    super(servlet , deploymentConfiguration);


    addSessionInitListener(event -> event.getSession().addUIProvider(new DefaultUIProvider() {
      @Override
      public UI createInstance(final UICreateEvent event) {
        return MainCDI.weldContainer.select(event.getUIClass()).get();
      }
    }));

    addSessionDestroyListener(event -> {
    });
  }

}