package org.rapidpm.vaadin.server.cdi;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.rapidpm.vaadin.trainer.api.User;
import org.rapidpm.vaadin.trainer.modules.login.LoginComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

/**
 *
 */
@Theme("valo")
@PreserveOnRefresh
public class MainUI extends UI {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainUI.class);

  @Override
  protected void init(VaadinRequest request) {
    LOGGER.debug("init - request = " + request);
    Subject subject = SecurityUtils.getSubject();
    boolean remembered = subject.isRemembered();
    if (! (user().isPresent() && remembered)) setContent(login());
    setSizeFull();
  }

  @Override
  protected void refresh(VaadinRequest request) {
    super.refresh(request);
    LOGGER.debug("refresh - request = " + request);
  }

  private Optional<User> user() {
    return Optional
        .ofNullable(
            getCurrent()
                .getSession()
                .getAttribute(User.class));
  }


  @Inject private Instance<LoginComponent> loginComponentInstance;

  private LoginComponent login() {
    return loginComponentInstance.get();
  }


  @PostConstruct
  private void postConstruct(){
    System.out.println("CDi activated for MainUI ");
  }


}
