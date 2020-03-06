package de.slag.invest.webservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import de.slag.invest.webcommon.demo.DemoDto;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
class IwsDemoCrudControllerTest {

	private static final String BASE_URL = "http://localhost:18080/slag-invest-webservice/democrud";

	@Test
	@Order(4)
	void testDelete() {
		final Response response = TestWebTargetUtils.create(BASE_URL + "/delete").queryParam("id", 1l).request()
				.delete();
		assertNotNull(response);
		assertEquals(200, response.getStatus());
	}

	@Test
	@Order(3)
	void testSave() {
		final DemoDto demoDto = new DemoDto();
		demoDto.setId(1l);
		demoDto.setName("demo crud test");

		final Response response = TestWebTargetUtils.create(BASE_URL + "/save").request(MediaType.APPLICATION_JSON)
				.accept(MediaType.TEXT_PLAIN_TYPE).post(Entity.json(demoDto));

		assertNotNull(response);
		assertEquals(200, response.getStatus());

	}

	@Test
	@Order(2)
	void testLoad() {
		final Response response = TestWebTargetUtils.create(BASE_URL + "/load").queryParam("id", 1l).request().get();
		assertNotNull(response);
		assertEquals(200, response.getStatus());
		final DemoDto dto = response.readEntity(DemoDto.class);
		assertNotNull(dto);
	}

	@Test
	@Order(1)
	void testCreate() {
		final Response response = TestWebTargetUtils.create(BASE_URL + "/create").request().get();
		assertNotNull(response);
		assertEquals(200, response.getStatus());
	}
}
