package io.egen.app;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

import io.swagger.jaxrs.config.BeanConfig;

@ApplicationPath("/api")
public class AppConfig extends ResourceConfig{
	
	public AppConfig(){
		packages("io.egen.rrs");
		register(io.swagger.jaxrs.listing.ApiListingResource.class);
		register(io.swagger.jaxrs.listing.SwaggerSerializers.class);
		register(io.egen.rest.provder.AccessControlResponseFilter.class);
		
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setVersion("1.0.0");
		beanConfig.setSchemes(new String[] {"http"});
		beanConfig.setBasePath("/RRS_App/api");
		beanConfig.setResourcePackage("io.egen");
		beanConfig.setDescription("Rest API for Restaurant Reservation");
		beanConfig.setScan(true);
		beanConfig.setTitle("io.egen");
	}

}
