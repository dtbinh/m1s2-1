package rest;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import rest.config.AppConfig;
import rest.model.FileServlet;

public class Starter {
	public static void main(final String[] args) throws Exception {
		Server server = new Server(8081);

		// Register and map the dispatcher servlet
		final ServletHolder servletHolder = new ServletHolder(new CXFServlet());
		final ServletHolder staticFileServletHolder = new ServletHolder(new FileServlet());
		final ServletContextHandler context = new ServletContextHandler();
		context.setContextPath("/");
		context.addServlet(servletHolder, "/ftp/*");
		context.addServlet(staticFileServletHolder, "/static/*");
		context.addEventListener(new ContextLoaderListener());

		context.setInitParameter("contextClass",
				AnnotationConfigWebApplicationContext.class.getName());
		context.setInitParameter("contextConfigLocation",
				AppConfig.class.getName());

		server.setHandler(context);
		server.start();
		server.join();
	}
}
