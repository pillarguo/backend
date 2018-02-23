package cmdc.dao;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@ApplicationPath("/")
@Singleton
public class ApplicationAPI extends ResourceConfig {
	public ApplicationAPI() {
		packages("cmdc");
		register(MultiPartFeature.class);
		register(JacksonFeature.class);
		// register(JsonContextProvider.class);
		register(JacksonJsonProvider.class);
		register(RolesAllowedDynamicFeature.class);

	}

}
