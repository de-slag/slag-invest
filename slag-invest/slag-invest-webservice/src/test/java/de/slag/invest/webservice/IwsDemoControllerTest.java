package de.slag.invest.webservice;

import static org.junit.jupiter.api.Assertions.*;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.slag.invest.webcommon.DemoDto;

class IwsDemoControllerTest {

	private static final String BASE_URL = "http://localhost:18080/slag-invest-webservice/demo";
	
	private WebTarget demoBaseEndpoint;
	private WebTarget demoResponseEndpoint;
	private WebTarget dtoResponseEndpoint;

	@BeforeEach
	public void setUp() {
		demoBaseEndpoint = TestWebTargetUtils.create(BASE_URL);
		demoResponseEndpoint = TestWebTargetUtils.create(BASE_URL + "/response");
		dtoResponseEndpoint = TestWebTargetUtils.create(BASE_URL + "/dto");
	}	

	@Test
	void demoDtoResponseEndpoint() {
		final DemoDto demoDto = dtoResponseEndpoint.request().get(DemoDto.class);
		assertNotNull(demoDto);
		assertNotNull(demoDto.getId());
		assertNotNull(demoDto.getName());
	}

	@Test
	void demoResponseEndpoint() {
		final Response response = demoResponseEndpoint.request().get();
		assertNotNull(response);
		assertTrue(response.getStatus() == 200);
	}

	@Test
	void demoBaseEndpoint() {
		assertEquals("demo", demoBaseEndpoint.request().get(String.class));
	}
	

}
