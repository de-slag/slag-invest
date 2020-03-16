package de.slag.invest.webservice;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.slag.invest.webservice.response.WsResponse;

public class BasicWsIntegrationTest extends AbstractWsIntegrationTest {

	@Test
	void testBase() {
		final String string = getWebTarget("").request().get(String.class);
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

	@Test
	void testWsResponse() {
		final Client client = ClientBuilder.newClient();
		final WebTarget target = client.target(getUri() + "/test").queryParam("param", "wsresponse");
		final WsResponse wsResponse = target.request().get(WsResponse.class);
		Assertions.assertEquals("param: wsresponse", wsResponse.getMessage());

	}

}
