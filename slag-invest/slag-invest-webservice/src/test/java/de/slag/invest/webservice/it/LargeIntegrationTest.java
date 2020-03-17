package de.slag.invest.webservice.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.slag.invest.webcommon.model.CommonDto;
import de.slag.invest.webservice.WebTargetBuilder;
import de.slag.invest.webservice.response.StringWebserviceResponse2;
import de.slag.invest.webservice.response.WebserviceResponse2;
import de.slag.invest.webservice.response.WsResponse;

@TestMethodOrder(OrderAnnotation.class)
public class LargeIntegrationTest {

	private static final String FIRST_TRANSACTION_ID = "first_transaction_id";

	private static final Log LOG = LogFactory.getLog(LargeIntegrationTest.class);

	private static final String BASE_URL = "http://localhost:18080/slag-invest-webservice";

	private static List<String> testProtocol;

	private static Long start;

	private static LargeIntegrationTestProperties properties;

	private static LitReporter reporter = new LitReporter();

	private static WebTarget baseWebTarget;
	private static WebTarget testWebTarget;
	private static WebTarget loginWebTarget;
	private static WebTarget addmandantWebTarget;
	private static WebTarget adduserWebTaget;
	
	private static WebTarget configCreateWebTarget;
	
	
	private static WebTarget transactionCreateWebTarget;
	private static WebTarget transactionLoadWebTarget;

	@BeforeAll
	public static void setUpLit() {
		reporter.start("setup");
		start = System.currentTimeMillis();

		baseWebTarget = new WebTargetBuilder().withUrl(BASE_URL).build();
		testWebTarget = new WebTargetBuilder().withUrl(BASE_URL + "/test").build();
		loginWebTarget = new WebTargetBuilder().withUrl(BASE_URL + "/login").build();
		addmandantWebTarget = new WebTargetBuilder().withUrl(BASE_URL + "/addmandant").build();
		adduserWebTaget = new WebTargetBuilder().withUrl(BASE_URL + "/adduser").build();
		
		configCreateWebTarget =  new WebTargetBuilder().withUrl(BASE_URL + "/config/create").build();
		
		transactionCreateWebTarget = new WebTargetBuilder().withUrl(BASE_URL + "/portfoliotransaction/create").build();
		transactionLoadWebTarget = new WebTargetBuilder().withUrl(BASE_URL + "/portfoliotransaction/load").build();

		properties = new LargeIntegrationTestProperties(start);
		reporter.end("setup");
	}

	@BeforeAll
	public static void setUp2() {
		testProtocol = new ArrayList<>();
	}

	@Test
	@Order(0)
	public void basicTest() {
		reporter.start("basic test");
		assertEquals("slag-invest-webservice", request(baseWebTarget).get(String.class));
		logResult("Base Endpoint URL-Test");
		reporter.end("basic test");
	}

	@Test
	@Order(1)
	public void testTest() {
		reporter.start("test test");
		final String response = request(testWebTarget).get(String.class);
		assertEquals("test, param: null", response);
		logResult("Test Call, no parameter");

		assertEquals("test, param: 0815", request(testWebTarget.queryParam("param", "0815")).get(String.class));
		logResult("Test Call, random parameter");

		assertEquals(WsResponse.class,
				request(testWebTarget.queryParam("param", "wsresponse")).get(WsResponse.class).getClass());
		logResult("Test Call, expect WsResponse");

		assertEquals("200", request(testWebTarget.queryParam("param", "response")).get(String.class));
		logResult("Test Call, expect '200' (http: ok)");
		reporter.end("test test");
	}

	@Test
	@Order(2)
	public void adminUserTest() {
		reporter.start("admin user test");
		final String admUserName = properties.getAdmUserName();
		final WebTarget queryParam = loginWebTarget.queryParam("username", admUserName).queryParam("password",
				"adm_User");
		final StringWebserviceResponse2 superLoginResponse = request(queryParam).get(StringWebserviceResponse2.class);

		List<String> value = superLoginResponse.getValue();
		assertTrue(!value.isEmpty());

		final String superUserToken = value.get(0);
		assertTrue(StringUtils.isNotEmpty(superUserToken));

		properties.putToken(admUserName, superUserToken);
		logResult("Admin User Login");
		reporter.end("admin user test");
	}

	@Test
	@Order(3)
	public void mandantTest() {
		reporter.start("mandant test");
		final String superUserToken = properties.getToken(properties.getAdmUserName());
		final String mandantName = properties.getMandantName();

		final WebserviceResponse2 responseOnce = request(
				addmandantWebTarget.queryParam("token", superUserToken).queryParam("mandant", mandantName))
						.get(WebserviceResponse2.class);

		assertTrue(responseOnce.getSuccessful());
		assertNotNull(responseOnce.getMessage());
		logResult("Add mandant");

		final WebserviceResponse2 responseAgain = addmandantWebTarget.queryParam("token", superUserToken)
				.queryParam("mandant", mandantName).request().get(WebserviceResponse2.class);

		assertFalse(responseAgain.getSuccessful());
		assertNotNull(responseAgain.getMessage());
		logResult("Add mandant, avoid second mandant with same name");
		reporter.end("mandant test");
	}

	@Test
	@Order(4)
	public void userTest() {
		reporter.start("user test");
		final String superUserToken = properties.getToken(properties.getAdmUserName());

		final String mandantSuperUserName = properties.getMandantSuperUserName();
		final String password = properties.getPassword(mandantSuperUserName);
		final String mandant = properties.getMandantName();

		final WebserviceResponse2 responseAdduser = request(adduserWebTaget.queryParam("username", mandantSuperUserName)
				.queryParam("password", password).queryParam("mandant", mandant).queryParam("token", superUserToken))
						.get(WebserviceResponse2.class);

		assertTrue(responseAdduser.getSuccessful());
		assertNotNull(responseAdduser.getMessage());
		logResult("Mandant User Registration");

		final StringWebserviceResponse2 loginMandantSuperUser = request(
				loginWebTarget.queryParam("username", mandantSuperUserName).queryParam("password", password)
						.queryParam("mandant", mandant)).get(StringWebserviceResponse2.class);

		assertNotNull(loginMandantSuperUser);

		assertTrue(loginMandantSuperUser.getSuccessful());
		List<String> value = loginMandantSuperUser.getValue();
		assertTrue(!value.isEmpty());
		final String mandantSuperUserToken = value.get(0);
		properties.putToken(mandantSuperUserName, mandantSuperUserToken);
		logResult("Mandant User Login");
		reporter.end("user test");
	}
	
	@Test
	@Order(5)
	public void createConfigTest() {
		reporter.start("create config test");
		final String mandantSuperUserName = properties.getMandantSuperUserName();
		final String token = properties.getToken(mandantSuperUserName);
		final Response response = request(configCreateWebTarget.queryParam("token", token)).get();
		assertNotNull(response);
		assertEquals(200, response.getStatus());
		final String readEntity = response.readEntity(String.class);
		assertNotNull(readEntity);
		reporter.end("create config test");
	}

	@Test
	@Order(9)
	public void createTransactionTest() {
		reporter.start("create transaction test");
		final String mandantSuperUserName = properties.getMandantSuperUserName();
		final String token = properties.getToken(mandantSuperUserName);
		
		final Response response = request(transactionCreateWebTarget.queryParam("token", token)).get();
		assertNotNull(response);
		assertEquals(200, response.getStatus());
		final String newId = response.readEntity(String.class);
		assertNotNull(newId);
		properties.put(FIRST_TRANSACTION_ID, newId);

		reporter.end("create transaction test");
	}

	@Test
	@Order(10)
	public void readTransactionTest() {
		reporter.start("read transaction test");
		final String mandantSuperUserName = properties.getMandantSuperUserName();
		final String token = properties.getToken(mandantSuperUserName);		
		String id = properties.get(FIRST_TRANSACTION_ID);
		
		final Response response = request(transactionLoadWebTarget.queryParam("id", id).queryParam("token", token)).get();
		assertNotNull(response);
		final CommonDto readEntity = response.readEntity(CommonDto.class);
		assertNotNull(readEntity);
		reporter.end("read transaction test");
	}

	@AfterAll
	public static void shutDown() throws Exception {
		int size = testProtocol.size();
		testProtocol.add(String.format("test count: %s", size));
		testProtocol.add(String.format("runtime: %s seconds", (System.currentTimeMillis() - start) / 1000.0));
		LOG.info("Results:\n" + String.join("\n", testProtocol));

		LOG.info("Test results:\n" + String.join("\n", testProtocol));
		reporter.close();
		LOG.info(reporter);
	}

	@Test
	@Order(999)
	public void testWithUrl() {
		reporter.start("with url test");
		// status test
		final String mandantSuperUserName = properties.getMandantSuperUserName();
		final String url = String.format(BASE_URL + "/status?token=%s", properties.getToken(mandantSuperUserName));

		final WebTarget build = new WebTargetBuilder().withUrl(url).build();
		final StringWebserviceResponse2 response = request(build).get(StringWebserviceResponse2.class);

		assertNotNull(response);
		assertTrue(response.getSuccessful());
		assertTrue(response.getValue().size() == 1);
		logResult("Get Status with Mandant User Token");
		reporter.end("with url test");
	}

	private Builder request(WebTarget wt) {
		log(wt);
		return wt.request();
	}

	private void log(WebTarget wt) {
		LOG.info(wt.getUri());
	}

	private void logResult(String testName) {
		logResult(testName, TestResult.OK);
	}

	private void logResult(String testName, TestResult testResult) {
		testProtocol.add(String.format("[%s] %s", testResult, testName));
	}

	private enum TestResult {
		OK,

		FAIL
	}
}
