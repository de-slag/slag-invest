package de.slag.invest.webservice.it;

import java.util.Optional;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.BeforeEach;

import de.slag.invest.webservice.WebTargetBuilder;


public abstract class AbstractWsIntegrationTest {
	
	private static final Log LOG = LogFactory.getLog(AbstractWsIntegrationTest.class);

	private Client client;
	private WebTarget webTarget;

	protected String getUri() {
		return "http://localhost:18080/slag-invest-webservice";
	}

	@BeforeEach
	void setUp() {
		client = ClientBuilder.newClient();
		webTarget = client.target(getUri());
	}

	@Deprecated
	Builder getRequest() {
		return webTarget.request();
	}

	@Deprecated
	protected <T> T getResponse(Class<T> expectedResponseType) {
		return getResponse(Optional.of(expectedResponseType), Optional.empty());
	}

	@Deprecated
	<T> T getResponse(Optional<Class<T>> expectedResponseType, Optional<String> endpoint) {
		final WebTarget webTarget = client.target(getUri() + (endpoint.isPresent() ? endpoint.get() : ""));
		return webTarget.request().get(expectedResponseType.get());
	}

	@Deprecated
	Builder getRequest(String endpoint) {
		return getWebTarget(endpoint).request();
	}

	protected WebTarget getWebTarget(String endpoint) {
		final String uri = getUri() + "/" + endpoint;
		LOG.info("URI: " + uri);
		return ClientBuilder.newClient().target(uri);
	}

	protected <T> T getResponse(String url, Class<T> responseType) {
		final WebTarget build = new WebTargetBuilder().withUrl(url).build();
		LOG.info(build);
		return build.request().get(responseType);
	}	
}
