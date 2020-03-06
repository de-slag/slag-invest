package de.slag.invest.webservice;

import static org.junit.jupiter.api.Assertions.*;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.slag.invest.webcommon.DemoDto;

class IwsDemoControllerTest {

	private static final String BASE_URL = "http://localhost:18080/slag-invest-webservice/demo";
	
	private WebTarget baseEndpoint;
	private WebTarget responseEndpoint;
	private WebTarget dtoResponseEndpoint;

	@BeforeEach
	public void setUp() {
		baseEndpoint = TestWebTargetUtils.create(BASE_URL);
		responseEndpoint = TestWebTargetUtils.create(BASE_URL + "/response");
		dtoResponseEndpoint = TestWebTargetUtils.create(BASE_URL + "/dto");
	}
	
	@Test
	void dtoResponseEndpointUsingResponseTest() {
		final Response response = dtoResponseEndpoint.request().get();
		assertNotNull(response);
		assertEquals(200, response.getStatus());
		final DemoDto demoDto = response.readEntity(DemoDto.class);
		assertNotNull(demoDto);
		assertNotNull(demoDto.getId());
		assertNotNull(demoDto.getName());
	}

	@Test
	void dtoResponseEndpointTest() {
		final DemoDto demoDto = dtoResponseEndpoint.request().get(DemoDto.class);
		assertNotNull(demoDto);
		assertNotNull(demoDto.getId());
		assertNotNull(demoDto.getName());
	}

	@Test
	void responseEndpointTest() {
		final Response response = responseEndpoint.request().get();
		assertNotNull(response);
		assertTrue(response.getStatus() == 200);
	}

	@Test
	void baseEndpointTest() {
		assertEquals("demo", baseEndpoint.request().get(String.class));
	}
	

}
