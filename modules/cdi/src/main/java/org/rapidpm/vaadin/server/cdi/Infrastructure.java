package org.rapidpm.vaadin.server.cdi;

import static io.undertow.Handlers.redirect;
import static io.undertow.servlet.Servlets.servlet;
import static javax.servlet.DispatcherType.ERROR;
import static javax.servlet.DispatcherType.FORWARD;
import static javax.servlet.DispatcherType.INCLUDE;
import static javax.servlet.DispatcherType.REQUEST;

import java.util.Optional;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.apache.shiro.web.servlet.ShiroFilter;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.FilterInfo;
import io.undertow.servlet.api.ListenerInfo;

/**
 *
 */
public interface Infrastructure {

  String DEFAULT_CONTEXT_PATH = "/";
  String DEFAULT_FILTER_MAPPING = "/*";
  String DEFAULT_SHIRO_FILTER_NAME = "ShiroFilter";
  Integer DEFAULT_HTTP_PORT = 8080;

  String DEFAULT_DEPLOYMENT_NAME = "ROOT.war";

//  Undertow factories

  static Optional<Undertow> undertow(ClassLoader classLoader ,
                                     Class<? extends Servlet> servletClass) {
    return undertow(classLoader ,
                    servletClass ,
                    DEFAULT_CONTEXT_PATH ,
                    null ,
                    DEFAULT_FILTER_MAPPING ,
                    DEFAULT_HTTP_PORT ,
                    DEFAULT_DEPLOYMENT_NAME);
  }

  static Optional<Undertow> undertowWithDefaultShiro(ClassLoader classLoader ,
                                                     Class<? extends Servlet> servletClass) {
    return undertow(classLoader ,
                    servletClass ,
                    DEFAULT_CONTEXT_PATH ,
                    DEFAULT_SHIRO_FILTER_NAME ,
                    DEFAULT_FILTER_MAPPING ,
                    DEFAULT_HTTP_PORT ,
                    DEFAULT_DEPLOYMENT_NAME);
  }

  static Optional<Undertow> undertow(ClassLoader classLoader ,
                                     Class<? extends Servlet> servletClass ,
                                     String shiroFilter , String shiroFilterMapping) {
    return undertow(classLoader ,
                    servletClass ,
                    DEFAULT_CONTEXT_PATH ,
                    shiroFilter ,
                    shiroFilterMapping ,
                    DEFAULT_HTTP_PORT ,
                    DEFAULT_DEPLOYMENT_NAME);
  }

  static Optional<Undertow> undertow(ClassLoader classLoader ,
                                     Class<? extends Servlet> servletClass ,
                                     String shiroFilter , String shiroFilterMapping ,
                                     int port) {
    return undertow(classLoader ,
                    servletClass ,
                    DEFAULT_CONTEXT_PATH ,
                    shiroFilter ,
                    shiroFilterMapping ,
                    port ,
                    DEFAULT_DEPLOYMENT_NAME);
  }


  static Optional<Undertow> undertow(ClassLoader classLoader ,
                                     Class<? extends Servlet> servletClass ,
                                     String defaultContextPath ,
                                     String shiroFilter , String filterMapping ,
                                     int port ,
                                     String deploymentName) {

    DeploymentManager manager = Servlets
        .defaultContainer()
        .addDeployment(
            addServlet(
                (shiroFilter == null)
                ? deploymentInfo(classLoader , defaultContextPath , deploymentName)
                : deploymentInfo(classLoader , defaultContextPath , deploymentName , shiroFilter , filterMapping) ,
                servletClass ,
                filterMapping)
        );

    manager.deploy();

    try {
      HttpHandler httpHandler = manager.start();
      PathHandler path = Handlers.path(redirect(defaultContextPath))
                                 .addPrefixPath(defaultContextPath , httpHandler);

      Undertow undertowServer = Undertow.builder()
                                        .addHttpListener(port , "0.0.0.0")
                                        .setHandler(path)
                                        .build();
      undertowServer.start();

      return Optional.of(undertowServer);
    } catch (ServletException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  static DeploymentInfo addServlet(DeploymentInfo deploymentInfo ,
                                   Class<? extends Servlet> servletClass ,
                                   String filterMapping) {
    return deploymentInfo
        .addServlets(
            servlet(
                servletClass.getSimpleName() ,
                servletClass,
                new ServletInstanceFactory(servletClass) //activate CDI on Servlet
            )
                .addMapping(filterMapping)
        );
  }

  static DeploymentInfo deploymentInfo(ClassLoader classLoader ,
                                       String defaultContextPath ,
                                       String deploymentName) {
    return getRawDeploymentInfo(classLoader , defaultContextPath , deploymentName);
  }

  static DeploymentInfo deploymentInfo(ClassLoader classLoader ,
                                       String defaultContextPath ,
                                       String deploymentName ,
                                       String shiroFilter ,
                                       String filterMapping) {
    return addShiroFilter(getRawDeploymentInfo(classLoader , defaultContextPath , deploymentName) , shiroFilter , filterMapping);
  }


  static DeploymentInfo getRawDeploymentInfo(ClassLoader classLoader ,
                                             String defaultContextPath ,
                                             String deploymentName) {
    return Servlets.deployment()
                   .setClassLoader(classLoader)
                   .setContextPath(defaultContextPath)
                   .setDeploymentName(deploymentName)
                   .setDefaultEncoding("UTF-8");
  }

  static DeploymentInfo addShiroFilter(DeploymentInfo deploymentInfo ,
                                       String shiroFilterName ,
                                       String shiroShiroFilterMappin) {
    return deploymentInfo.addListener(new ListenerInfo(EnvironmentLoaderListener.class))
                         .addFilter(new FilterInfo(shiroFilterName , ShiroFilter.class))
                         .addFilterUrlMapping(shiroFilterName , shiroShiroFilterMappin , REQUEST)
                         .addFilterUrlMapping(shiroFilterName , shiroShiroFilterMappin , FORWARD)
                         .addFilterUrlMapping(shiroFilterName , shiroShiroFilterMappin , INCLUDE)
                         .addFilterUrlMapping(shiroFilterName , shiroShiroFilterMappin , ERROR);

  }

}
