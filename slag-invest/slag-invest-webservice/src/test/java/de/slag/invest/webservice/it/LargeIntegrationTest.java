package de.slag.invest.webservice.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.slag.invest.webservice.IntegrationTestProperties;
import de.slag.invest.webservice.response.StringWebserviceResponse2;
import de.slag.invest.webservice.response.WebserviceResponse2;
import de.slag.invest.webservice.response.WsResponse;

@TestMethodOrder(OrderAnnotation.class)
public class LargeIntegrationTest extends AbstractWsIntegrationTest {

	private static final Log LOG = LogFactory.getLog(LargeIntegrationTest.class);

	private static final String MANDANT_SUPER_USER_NAME = "super";

	private static final String ADM_USER_NAME = "sysadm";

	private static final String INT_TEST_MANDANT = "I_MANDANT";

	private static final String BASE_URL = "http://localhost:18080/slag-invest-webservice";

	private final IntegrationTestProperties integrationTestProperties = new IntegrationTestProperties();

	private static List<String> testProtocol;

	private static Long start;

	@Test
	@Order(0)
	public void basicTest() {
		// base entdpoint URL-Test
		assertEquals("slag-invest-webservice", getResponse(BASE_URL, String.class));
		logResult("Base Endpoint URL-Test");
	}

	@Test
	@Order(1)
	public void testTest() {
		testTests();
	}

	@Test
	@Order(2)
	public void adminUserTest() {
		adminUserTests();
	}

	@Test
	@Order(3)
	public void mandantTest() {
		mandantTests();
	}

	@Test
	@Order(4)
	public void userTest() {
		userTests();
	}

	@BeforeAll
	public static void setUp2() {
		testProtocol = new ArrayList<>();
		start = System.currentTimeMillis();
	}

	@AfterAll
	public static void shutDown() {
		int size = testProtocol.size();
		testProtocol.add(String.format("test count: %s", size));
		testProtocol.add(String.format("runtime: %s seconds", (System.currentTimeMillis() - start) / 1000.0));
		LOG.info("Results:\n" + String.join("\n", testProtocol));

		LOG.info("Test results:\n" + String.join("\n", testProtocol));
	}

	@Test
	@Order(999)
	public void testWithUrl() {

		// status test
		final String url = String.format(BASE_URL + "/status?token=%s",
				integrationTestProperties.getToken(MANDANT_SUPER_USER_NAME));
		StringWebserviceResponse2 response = getResponse(url, StringWebserviceResponse2.class);
		assertNotNull(response);
		assertTrue(response.getSuccessful());
		assertTrue(response.getValue().size() == 1);
		logResult("Get Status with Mandant User Token");
	}

	private void logResult(String testName) {
		logResult(testName, TestResult.OK);
	}

	private void logResult(String testName, TestResult testResult) {
		testProtocol.add(String.format("[%s] %s", testResult, testName));
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
		logResult("Admin User Login");
	}

	private void mandantTests() {
		final String superUserToken = integrationTestProperties.getToken(ADM_USER_NAME);

		final String urlAddMandant = String.format(BASE_URL + "/addmandant?token=%s&mandant=%s", superUserToken,
				INT_TEST_MANDANT);

		final WebserviceResponse2 response = getResponse(urlAddMandant, WebserviceResponse2.class);
		assertTrue(response.getSuccessful());

		final String expected = String.format("Mandant '%s' succesful added.", INT_TEST_MANDANT);
		assertEquals(expected, response.getMessage());
		logResult("Add mandant");

		final WebserviceResponse2 responseAgain = getResponse(urlAddMandant, WebserviceResponse2.class);
		assertFalse(responseAgain.getSuccessful());
		assertEquals("a mandant whith the given name already exists: " + INT_TEST_MANDANT, responseAgain.getMessage());
		logResult("Add mandant, avoid second mandant with same name");

	}

	private void userTests() {
		final String superUserToken = integrationTestProperties.getToken(ADM_USER_NAME);

		String urlAdduser2 = String.format(BASE_URL + "/adduser?username=%s&mandant=%s&password=%s&token=%s",
				MANDANT_SUPER_USER_NAME, INT_TEST_MANDANT, MANDANT_SUPER_USER_NAME, superUserToken);
		final WebserviceResponse2 responseAdduser = getResponse(urlAdduser2, WebserviceResponse2.class);

		assertTrue(responseAdduser.getSuccessful());
		assertEquals("adduser succsessful: super, mandant: I_MANDANT", responseAdduser.getMessage());
		logResult("Mandant User Registration");

		final String urlLoginUser = String.format(BASE_URL + "/login?username=%s&password=%s&mandant=%s",
				MANDANT_SUPER_USER_NAME, MANDANT_SUPER_USER_NAME, INT_TEST_MANDANT);

		final StringWebserviceResponse2 loginMandantSuperUser = getResponse(urlLoginUser,
				StringWebserviceResponse2.class);
		assertTrue(loginMandantSuperUser.getSuccessful());
		List<String> value = loginMandantSuperUser.getValue();
		assertTrue(!value.isEmpty());
		final String mandantSuperUserToken = value.get(0);
		integrationTestProperties.putToken(MANDANT_SUPER_USER_NAME, mandantSuperUserToken);
		logResult("Mandant User Login");
	}

	private void testTests() {
		assertEquals("test, param: null", getResponse(BASE_URL + "/test", String.class));
		logResult("Test Call, no parameter");

		assertEquals("test, param: 0815", getResponse(BASE_URL + "/test?param=0815", String.class));
		logResult("Test Call, random parameter");

		assertEquals(WsResponse.class, getResponse(BASE_URL + "/test?param=wsresponse", WsResponse.class).getClass());
		logResult("Test Call, expect WsResponse");

		assertEquals("200", getResponse(BASE_URL + "/test?param=response", String.class));
		logResult("Test Call, expect '200' (http: ok)");
	}

	private enum TestResult {
		OK,

		FAIL
	}
}
