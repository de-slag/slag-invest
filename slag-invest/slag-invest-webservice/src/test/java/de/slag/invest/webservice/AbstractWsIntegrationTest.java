package de.slag.invest.webservice;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;

import org.junit.jupiter.api.BeforeEach;

abstract class AbstractWsIntegrationTest {

	Client client;
	WebTarget webTarget;

	String getUri() {
		return "http://localhost:18080/slag-invest-webservice";
	}

	@BeforeEach
	void setUp() {
		client = ClientBuilder.newClient();
		webTarget = client.target(getUri());
	}

	Builder getRequest() {
		return webTarget.request();
	}

	<T> T getResponse(Class<T> expectedResponseType) {
		return getRequest().get(expectedResponseType);
	}
}
