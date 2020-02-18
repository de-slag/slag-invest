package de.slag.invest.webservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import de.slag.invest.webservice.response.WebserviceResponse;
import de.slag.invest.webservice.response.WsResponse;

public class UltimateIntegrationTest extends AbstractWsIntegrationTest {

	private static final Log LOG = LogFactory.getLog(UltimateIntegrationTest.class);

	private static final String BASE_URL = "http://localhost:18080/slag-invest-webservice";

	private static final String PASSWORD = "itest_password";
	private static final String USER_NAME = "itest_user";
	private static final String MANDANT_NAME = "ITEST";

	@Test
	public void testWithUrl() {

		// test calls

		assertEquals("test, param: null", getResponse(BASE_URL + "/test", String.class));
		assertEquals("test, param: 0815", getResponse(BASE_URL + "/test?param=0815", String.class));
		assertEquals(WsResponse.class, getResponse(BASE_URL + "/test?param=wsresponse", WsResponse.class).getClass());
		assertEquals("200", getResponse(BASE_URL + "/test?param=response", String.class));

		// USER tests

		final String token0 = getResponse(BASE_URL + "/login?username=test&password=test", String.class);
		assertTrue(StringUtils.isNotEmpty(token0));
	}

	@Disabled
	@Test
	public void test() {

		// TEST calls
		getWebTarget("test").request().get();
		getWebTarget("test").queryParam("param", "response").request().get();
		getWebTarget("test").queryParam("param", "wsresponse").request().get(WsResponse.class);
		final WebserviceResponse wsBeanResponse = getWebTarget("test").queryParam("param", "wsbeanresponse").request()
				.get(WebserviceResponse.class);
		final Collection beans = wsBeanResponse.getBeans();

		// USER tests

		final String token = getWebTarget("login").queryParam("username", "test").queryParam("password", "test")
				.request().get(String.class);
		LOG.info("OK: test user login");

		final String ok = getWebTarget("addmandant").queryParam("token", token).queryParam("mandant", MANDANT_NAME)
				.request().get(String.class);
		assertEquals("OK", ok);

		LOG.info("OK: mandant creation");

		//
		final String ok2 = getWebTarget("adduser").queryParam("token", token).queryParam("mandant", MANDANT_NAME)
				.queryParam("username", USER_NAME).queryParam("password", PASSWORD).request().get(String.class);

		assertEquals("OK", ok2);

		final String userToken = getWebTarget("login").queryParam("username", USER_NAME)
				.queryParam("password", PASSWORD).request().get(String.class);

		// CRUD tests

		// final Object collection = getWebTarget("crud/read").queryParam("type",
		// "STOCK_VALUE").request().get(String.class);
		// Assertions.assertTrue(collection.isEmpty());
		// collection.getClass();
	}
}
