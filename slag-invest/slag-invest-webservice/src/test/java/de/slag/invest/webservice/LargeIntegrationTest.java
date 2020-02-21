package de.slag.invest.webservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import de.slag.invest.webservice.response.StringWebserviceResponse2;
import de.slag.invest.webservice.response.WebserviceResponse2;
import de.slag.invest.webservice.response.WsResponse;

public class LargeIntegrationTest extends AbstractWsIntegrationTest {

	private static final String ADM_USER_NAME = "sysadm";

	private static final String INT_TEST_MANDANT = "I_MANDANT";

	private static final String BASE_URL = "http://localhost:18080/slag-invest-webservice";

	private final IntegrationTestProperties integrationTestProperties = new IntegrationTestProperties();

	@Test
	public void testWithUrl() {

		testTests();

		userAndMandantTests();
	}

	private void userAndMandantTests() {
		// final String url = BASE_URL + "/login?username=sysadm&password=adm_User";
		final String url = String.format(BASE_URL + "/login?username=%s&password=%s", ADM_USER_NAME, "adm_User");
		final StringWebserviceResponse2 superLoginResponse = getResponse(url, StringWebserviceResponse2.class);
		final String superUserToken = superLoginResponse.getValue();
		assertTrue(StringUtils.isNotEmpty(superUserToken));
		integrationTestProperties.putToken(ADM_USER_NAME, superUserToken);

		final String urlAddMandant = String.format(BASE_URL + "/addmandant?token=%s&mandant=%s", superUserToken,
				INT_TEST_MANDANT);
		final WebserviceResponse2 response = getResponse(urlAddMandant, WebserviceResponse2.class);
		assertTrue(response.getSuccessful());

		final String expected = String.format("Mandant '%s' succesful added.", INT_TEST_MANDANT);
		assertEquals(expected, response.getMessage());

		final WebserviceResponse2 responseAgain = getResponse(urlAddMandant, WebserviceResponse2.class);
		assertFalse(responseAgain.getSuccessful());
		assertEquals("a mandant whith the given name already exists: " + INT_TEST_MANDANT, responseAgain.getMessage());

		final String urlAdduser = BASE_URL + "/adduser?username=super&mandant=" + INT_TEST_MANDANT
				+ "&password=super&token=" + superUserToken;
		final WebserviceResponse2 responseAdduser = getResponse(urlAdduser, WebserviceResponse2.class);
		assertTrue(responseAdduser.getSuccessful());
		assertEquals("adduser succsessful: super, mandant: I_MANDANT", responseAdduser.getMessage());

		final String urlLoginUser = String.format(BASE_URL + "/login?username=%s&password=%s&mandant=%s", "super",
				"super", INT_TEST_MANDANT);
		final WebserviceResponse2 loginMandantSuperUser = getResponse(urlLoginUser, WebserviceResponse2.class);
		assertTrue(loginMandantSuperUser.getSuccessful());
	}

	private void testTests() {
		assertEquals("test, param: null", getResponse(BASE_URL + "/test", String.class));
		assertEquals("test, param: 0815", getResponse(BASE_URL + "/test?param=0815", String.class));
		assertEquals(WsResponse.class, getResponse(BASE_URL + "/test?param=wsresponse", WsResponse.class).getClass());
		assertEquals("200", getResponse(BASE_URL + "/test?param=response", String.class));
	}
}
