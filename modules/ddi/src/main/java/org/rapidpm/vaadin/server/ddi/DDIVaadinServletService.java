package org.rapidpm.vaadin.server.ddi;

import org.rapidpm.ddi.DI;
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
public class DDIVaadinServletService extends VaadinServletService {


  public DDIVaadinServletService(VaadinServlet servlet ,
                                 DeploymentConfiguration deploymentConfiguration)
      throws ServiceException {

    super(servlet , deploymentConfiguration);


    addSessionInitListener(event -> event.getSession().addUIProvider(new DefaultUIProvider() {
      @Override
      public UI createInstance(final UICreateEvent event) {
        return DI.activateDI(event.getUIClass());
      }
    }));

    addSessionDestroyListener(event -> {
    });
  }

}