package org.rapidpm.vaadin.server;

import static io.undertow.Handlers.redirect;
import static io.undertow.servlet.Servlets.servlet;
import static javax.servlet.DispatcherType.ERROR;
import static javax.servlet.DispatcherType.FORWARD;
import static javax.servlet.DispatcherType.INCLUDE;
import static javax.servlet.DispatcherType.REQUEST;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.rapidpm.frp.functions.TriFunction;
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
public interface InfrastructureFunctions {

  String DEFAULT_SHIRO_FILTER = "ShiroFilter";
  String DEFAULT_SHIRO_FILTER_MAPPING = "/*";
  String DEFAULT_DEPLOYMENT_NAME = "ROOT.war";


  static BiFunction<ClassLoader, String, DeploymentInfo> baseDeploymentInfo() {
    return (classLoader, contextpath) -> Servlets.deployment()
                                                 .setDefaultEncoding("UTF-8")
                                                 .setClassLoader(classLoader)
                                                 .setContextPath(contextpath);
  }


  static TriFunction<DeploymentInfo, String, String, DeploymentInfo> shiroFilter() {
    return (deploymentInfo, filterName, filterMapping) ->
        deploymentInfo
            .addListener(new ListenerInfo(EnvironmentLoaderListener.class))
            .addFilter(new FilterInfo(filterName, ShiroFilter.class))
            .addFilterUrlMapping(filterName, filterMapping, REQUEST)
            .addFilterUrlMapping(filterName, filterMapping, FORWARD)
            .addFilterUrlMapping(filterName, filterMapping, INCLUDE)
            .addFilterUrlMapping(filterName, filterMapping, ERROR);
  }


  static Supplier<Integer> httpPort(){
    return ()->8080;
  }

  static TriFunction<Class<? extends Servlet>, ClassLoader, String, Optional<Undertow>> undertowOneServlet() {

    return (servlet, classLoader, contextpath) -> {
      DeploymentInfo servletBuilder
          = baseDeploymentInfo().apply(classLoader, contextpath)
                                .setDeploymentName(DEFAULT_DEPLOYMENT_NAME)
                                .addServlets(
                                    servlet(servlet.getSimpleName(),
                                            servlet,
                                            new ServletInstanceFactory(servlet))
                                        .addMapping("/*")
                                );

      DeploymentManager manager = Servlets
          .defaultContainer()
          .addDeployment(servletBuilder);

      manager.deploy();

      try {
        HttpHandler httpHandler = manager.start();
        PathHandler path = Handlers.path(redirect(contextpath))
                                   .addPrefixPath(contextpath, httpHandler);

        Undertow undertowServer = Undertow.builder()
                                          .addHttpListener(httpPort().get(), "0.0.0.0")
                                          .setHandler(path)
                                          .build();
        undertowServer.start();

        undertowServer.getListenerInfo().forEach(System.out::println);
        return Optional.of(undertowServer);

      } catch (ServletException e) {
        e.printStackTrace();
        return Optional.empty();
      }
    };

  }


}
