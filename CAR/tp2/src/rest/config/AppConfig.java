package rest.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import rest.rs.FTPRestService;
import rest.rs.JaxRsApiApplication;
import rest.rs.PeopleRestService;
import rest.services.FTPService;
import rest.services.PeopleService;
import car.HelloWorldResource;

@Configuration
public class AppConfig {
	@Bean(destroyMethod = "shutdown")
	public SpringBus cxf() {
		return new SpringBus();
	}
	
	private String applicationPath;

	@Bean
	@DependsOn("cxf")
	public Server jaxRsServer() throws IOException {
		JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance()
				.createEndpoint(jaxRsApiApplication(),
						JAXRSServerFactoryBean.class);

		List<Object> serviceBeans = new ArrayList<Object>();
		//serviceBeans.add(peopleRestService());
		serviceBeans.add(new HelloWorldResource());
		//serviceBeans.add(new FTPClientResource());
		serviceBeans.add(ftpRestService());

		factory.setServiceBeans(serviceBeans);
		factory.setAddress("/" + factory.getAddress());
		factory.setProviders(Arrays.<Object> asList(jsonProvider()));
		return factory.create();
	}

	@Bean
	public JaxRsApiApplication jaxRsApiApplication() {
		final JaxRsApiApplication application = new JaxRsApiApplication();
		applicationPath = application.getClass().getAnnotation(ApplicationPath.class).value();
		return application;
	}

	@Bean
	public PeopleRestService peopleRestService() {
		return new PeopleRestService();
	}
	
	@Bean
	public FTPService ftpService() {
		return new FTPService();
	}
	
	@Bean
	public FTPRestService ftpRestService() {
		return new FTPRestService(ftpService(), applicationPath);
	}

	@Bean
	public PeopleService peopleService() {
		return new PeopleService();
	}

	@Bean
	public JacksonJsonProvider jsonProvider() {
		return new JacksonJsonProvider();
	}
}
