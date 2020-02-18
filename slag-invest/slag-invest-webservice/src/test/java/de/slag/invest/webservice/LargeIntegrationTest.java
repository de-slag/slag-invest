package de.slag.invest.webservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import de.slag.invest.webservice.response.WebserviceResponse2;
import de.slag.invest.webservice.response.WsResponse;

public class LargeIntegrationTest extends AbstractWsIntegrationTest {

	private static final String INT_TEST_MANDANT = "I_MANDANT";

	private static final String BASE_URL = "http://localhost:18080/slag-invest-webservice";

	@Test
	public void testWithUrl() {

		// test calls

		assertEquals("test, param: null", getResponse(BASE_URL + "/test", String.class));
		assertEquals("test, param: 0815", getResponse(BASE_URL + "/test?param=0815", String.class));
		assertEquals(WsResponse.class, getResponse(BASE_URL + "/test?param=wsresponse", WsResponse.class).getClass());
		assertEquals("200", getResponse(BASE_URL + "/test?param=response", String.class));

		// USER + MANDANT tests
		final String token0 = getResponse(BASE_URL + "/login?username=test&password=test", String.class);
		assertTrue(StringUtils.isNotEmpty(token0));

		final String urlAddMandant = String.format(BASE_URL + "/addmandant?token=%s&mandant=%s", token0,
				INT_TEST_MANDANT);
		final WebserviceResponse2 response = getResponse(urlAddMandant, WebserviceResponse2.class);
		assertTrue(response.getSuccessful());
		final String expected = String.format("Mandant '%s' succesful added.", INT_TEST_MANDANT);
		assertEquals(expected, response.getMessage());

		final WebserviceResponse2 responseAgain = getResponse(urlAddMandant, WebserviceResponse2.class);
		assertFalse(responseAgain.getSuccessful());
		assertEquals("a mandant whith the given name already exists: " + INT_TEST_MANDANT, responseAgain.getMessage());
	}
}
