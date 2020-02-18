package de.slag.invest.webservice;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class CredentialsWsIntegrationTest extends AbstractWsIntegrationTest {

	private static final String TEST = "test";

	@Test
	public void test() {
		final String response = getResponse("http://localhost:18080/slag-invest-webservice/test", String.class);
		response.getClass();
	}

	@Disabled
	@Test
	public void registrationProcess() {
		final String tokenFail = getWebTarget("login").queryParam("username", TEST).queryParam("password", TEST)
				.request().get(String.class);
		Assertions.assertTrue(StringUtils.isEmpty(tokenFail));

		final String r = getWebTarget("register").queryParam("susername", TEST).queryParam("password", TEST).request()
				.get(String.class);
		Assertions.assertEquals("OK", r);

		final String token = getWebTarget("login").queryParam("username", TEST).queryParam("password", TEST).request()
				.get(String.class);
		Assertions.assertTrue(StringUtils.isNotBlank(token));

	}

}
