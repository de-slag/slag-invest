package de.slag.invest.webservice;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BasicWsIntegrationTest extends AbstractWsIntegrationTest {
	
	@Test
	void testBase() {
		final String string = webTarget.request().get(String.class);
		Assertions.assertNotNull(string);
	}

	@Test
	void testTest() {
		final Client client = ClientBuilder.newClient();
		final String endpointUrl = getUri();
		final WebTarget target = client.target(endpointUrl + "/test").queryParam("param", "TEST");
		final String string = target.request().get(String.class);
		Assertions.assertEquals("test, param: TEST", string);
	}

}
