package io.egen.rest.provder;

import java.io.IOException;
import java.util.Arrays;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;


@Provider
public class AccessControlResponseFilter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext reqContext, ContainerResponseContext respContext) throws IOException {
		MultivaluedMap<String, Object> headers = respContext.getHeaders();

		String[] originsAllowed = { "http://localhost:63342", "http://petstore.swagger.io" };

		String origin = reqContext.getHeaders().getFirst("Origin");
		if (Arrays.asList(originsAllowed).contains(origin)) {
			headers.add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,HEAD,OPTIONS");
			headers.add("Access-Control-Allow-Origin", origin);
			headers.add("Access-Control-Allow-Headers", "Authorization, Origin, Content-Type, www-Authenticate");
		}

		headers.add("Powered By", "io.egen.api");

	}
}