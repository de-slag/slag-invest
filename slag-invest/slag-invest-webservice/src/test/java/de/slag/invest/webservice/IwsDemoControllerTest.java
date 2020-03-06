package de.slag.invest.webservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.slag.invest.webcommon.DemoDto;

@TestMethodOrder(OrderAnnotation.class)
class IwsDemoControllerTest {

	private static final String BASE_URL = "http://localhost:18080/slag-invest-webservice/demo";

	private WebTarget baseEndpoint;
	private WebTarget responseEndpoint;
	private WebTarget dtoEndpoint;
	private WebTarget createEndpoint;
	private WebTarget loadEndpoint;
	private WebTarget saveEndpoint;
	private WebTarget deleteEndpoint;

	@BeforeEach
	public void setUp() {
		baseEndpoint = TestWebTargetUtils.create(BASE_URL);
		responseEndpoint = TestWebTargetUtils.create(BASE_URL + "/response");
		dtoEndpoint = TestWebTargetUtils.create(BASE_URL + "/dto");
		createEndpoint = TestWebTargetUtils.create(BASE_URL + "/create");
		loadEndpoint = TestWebTargetUtils.create(BASE_URL + "/load");
		saveEndpoint = TestWebTargetUtils.create(BASE_URL + "/save");
		deleteEndpoint = TestWebTargetUtils.create(BASE_URL + "/delete");		
	}
	
	@Test
	@Order(8)
	void deleteEndpointTest() {
		final Response response = deleteEndpoint.queryParam("id", 1L).request().delete();
		assertEquals(200, response.getStatus());
	}

	@Test
	@Order(7)
	void saveEndpointTest() {
		final DemoDto demoDto = new DemoDto();
		demoDto.setId(1L);
		demoDto.setName("Demo Dto");

		final Response response = saveEndpoint.request(MediaType.APPLICATION_JSON).accept(MediaType.TEXT_PLAIN_TYPE).post(Entity.json(demoDto));
		assertEquals(200, response.getStatus());
	}

	@Test
	@Order(6)
	void simplePostTest() {
		final Response response = TestWebTargetUtils.create(BASE_URL + "/test").request(MediaType.TEXT_PLAIN).accept(MediaType.TEXT_PLAIN)
				.post(Entity.entity("demo test", MediaType.TEXT_PLAIN));
		assertEquals(200, response.getStatus());
		final String readEntity = response.readEntity(String.class);
		assertNotNull(readEntity);
		assertEquals("OK: demo test", readEntity);
	}

	@Test
	@Order(5)
	void loadEndpointTest() {
		final Response response = loadEndpoint.queryParam("id", 1L).request().get();
		assertNotNull(response);
		assertEquals(200, response.getStatus());
		final DemoDto demoDto = response.readEntity(DemoDto.class);
		assertNotNull(demoDto);
	}

	@Test
	@Order(4)
	void createEndpointTest() {
		final Response response = createEndpoint.request().get();
		assertNotNull(response);
		assertEquals(200, response.getStatus());
		final String readEntity = response.readEntity(String.class);
		assertNotNull(readEntity);
		assertEquals(1L, Long.valueOf(readEntity));
	}

	@Test
	@Order(3)
	void dtoEndpointUsingResponseTest() {
		final Response response = dtoEndpoint.request().get();
		assertNotNull(response);
		assertEquals(200, response.getStatus());
		final DemoDto demoDto = response.readEntity(DemoDto.class);
		assertNotNull(demoDto);
		assertNotNull(demoDto.getId());
		assertNotNull(demoDto.getName());
	}

	@Test
	@Order(2)
	void dtoEndpointTest() {
		final DemoDto demoDto = dtoEndpoint.request().get(DemoDto.class);
		assertNotNull(demoDto);
		assertNotNull(demoDto.getId());
		assertNotNull(demoDto.getName());
	}

	@Test
	@Order(1)
	void responseEndpointTest() {
		final Response response = responseEndpoint.request().get();
		assertNotNull(response);
		assertTrue(response.getStatus() == 200);
	}

	@Test
	@Order(0)
	void baseEndpointTest() {
		assertEquals("demo", baseEndpoint.request().get(String.class));
	}
}
