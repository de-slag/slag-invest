package de.slag.invest.webservice.crud;

import static org.junit.jupiter.api.Assertions.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import de.slag.invest.webcommon.demo.DemoDto;
import de.slag.invest.webcommon.model.ConfigDto;
import de.slag.invest.webservice.TestWebTargetUtils;

@TestMethodOrder(OrderAnnotation.class)
class IwsConfigCrudControllerTest {

	private final static String BASE_URL = "http://localhost:18080/slag-invest-webservice/config";

	private static Long id;

	@Test
	@Order(4)
	void testDelete() {
		assertEquals(200,
				TestWebTargetUtils.create(BASE_URL + "/delete").queryParam("id", id).request().delete().getStatus());
	}

	@Test
	@Order(3)
	void testSave() {
		final Response response0 = TestWebTargetUtils.create(BASE_URL + "/load").queryParam("id", id).request().get();
		assertNotNull(response0);
		assertEquals(200, response0.getStatus());
		final ConfigDto configDto = response0.readEntity(ConfigDto.class);
		
		configDto.setKey("test-key");
		configDto.setValue("test-value");

		final Response response1 = TestWebTargetUtils.create(BASE_URL + "/save").request(MediaType.APPLICATION_JSON)
				.accept(MediaType.TEXT_PLAIN_TYPE).post(Entity.json(configDto));

		assertNotNull(response1);
		assertEquals(200, response1.getStatus());
	}

	@Test
	@Order(2)
	void testLoad() {
		final Response response = TestWebTargetUtils.create(BASE_URL + "/load").queryParam("id", id).request().get();
		assertNotNull(response);
		assertEquals(200, response.getStatus());
		final ConfigDto configDto = response.readEntity(ConfigDto.class);
		assertNotNull(configDto);
	}

	@Test
	@Order(1)
	void testCreate() {
		final Response response = TestWebTargetUtils.create(BASE_URL + "/create").request().get();
		assertNotNull(response);
		assertEquals(200, response.getStatus());
		id = Long.valueOf(response.readEntity(String.class));
		assertNotNull(id);
	}

}
