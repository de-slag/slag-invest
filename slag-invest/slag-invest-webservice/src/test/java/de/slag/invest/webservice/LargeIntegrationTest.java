package de.slag.invest.webservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import de.slag.invest.webservice.response.StringWebserviceResponse2;
import de.slag.invest.webservice.response.WebserviceResponse2;
import de.slag.invest.webservice.response.WsResponse;

public class LargeIntegrationTest extends AbstractWsIntegrationTest {

	private static final String MANDANT_SUPER_USER_NAME = "super";

	private static final String ADM_USER_NAME = "sysadm";

	private static final String INT_TEST_MANDANT = "I_MANDANT";

	private static final String BASE_URL = "http://localhost:18080/slag-invest-webservice";

	private final IntegrationTestProperties integrationTestProperties = new IntegrationTestProperties();

	@Test
	public void testWithUrl() {

		// base entdpoint URL-Test
		assertEquals("slag-invest-webservice", getResponse(BASE_URL, String.class));

		testTests();

		adminUserTests();

		mandantTests();

		userTest();

		// status test
		final String url = String.format(BASE_URL + "/status?token=%s",
				integrationTestProperties.getToken(MANDANT_SUPER_USER_NAME));
		StringWebserviceResponse2 response = getResponse(url, StringWebserviceResponse2.class);
		assertNotNull(response);
		assertTrue(response.getSuccessful());
		assertTrue(response.getValue().size() == 5);

	}

	private void adminUserTests() {
		final String adminLoginUrl = String.format(BASE_URL + "/login?username=%s&password=%s", ADM_USER_NAME,
				"adm_User");
		final StringWebserviceResponse2 superLoginResponse = getResponse(adminLoginUrl,
				StringWebserviceResponse2.class);

		List<String> value = superLoginResponse.getValue();
		assertTrue(!value.isEmpty());

		final String superUserToken = value.get(0);
		assertTrue(StringUtils.isNotEmpty(superUserToken));

		integrationTestProperties.putToken(ADM_USER_NAME, superUserToken);
	}

	private void mandantTests() {
		final String superUserToken = integrationTestProperties.getToken(ADM_USER_NAME);

		final String urlAddMandant = String.format(BASE_URL + "/addmandant?token=%s&mandant=%s", superUserToken,
				INT_TEST_MANDANT);

		final WebserviceResponse2 response = getResponse(urlAddMandant, WebserviceResponse2.class);
		assertTrue(response.getSuccessful());

		final String expected = String.format("Mandant '%s' succesful added.", INT_TEST_MANDANT);
		assertEquals(expected, response.getMessage());

		final WebserviceResponse2 responseAgain = getResponse(urlAddMandant, WebserviceResponse2.class);
		assertFalse(responseAgain.getSuccessful());
		assertEquals("a mandant whith the given name already exists: " + INT_TEST_MANDANT, responseAgain.getMessage());
	}

	private void userTest() {
		final String superUserToken = integrationTestProperties.getToken(ADM_USER_NAME);

		String urlAdduser2 = String.format(BASE_URL + "/adduser?username=%s&mandant=%s&password=%s&token=%s",
				MANDANT_SUPER_USER_NAME, INT_TEST_MANDANT, MANDANT_SUPER_USER_NAME, superUserToken);
		final WebserviceResponse2 responseAdduser = getResponse(urlAdduser2, WebserviceResponse2.class);

		assertTrue(responseAdduser.getSuccessful());
		assertEquals("adduser succsessful: super, mandant: I_MANDANT", responseAdduser.getMessage());

		final String urlLoginUser = String.format(BASE_URL + "/login?username=%s&password=%s&mandant=%s",
				MANDANT_SUPER_USER_NAME, MANDANT_SUPER_USER_NAME, INT_TEST_MANDANT);

		final StringWebserviceResponse2 loginMandantSuperUser = getResponse(urlLoginUser,
				StringWebserviceResponse2.class);
		assertTrue(loginMandantSuperUser.getSuccessful());
		List<String> value = loginMandantSuperUser.getValue();
		assertTrue(!value.isEmpty());
		final String mandantSuperUserToken = value.get(0);
		integrationTestProperties.putToken(MANDANT_SUPER_USER_NAME, mandantSuperUserToken);
	}

	private void testTests() {
		assertEquals("test, param: null", getResponse(BASE_URL + "/test", String.class));
		assertEquals("test, param: 0815", getResponse(BASE_URL + "/test?param=0815", String.class));
		assertEquals(WsResponse.class, getResponse(BASE_URL + "/test?param=wsresponse", WsResponse.class).getClass());
		assertEquals("200", getResponse(BASE_URL + "/test?param=response", String.class));
	}
}
