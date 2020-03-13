package de.slag.invest.webservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.slag.invest.webservice.it.AbstractWsIntegrationTest;
import de.slag.invest.webservice.response.WebserviceResponse2;

public class LoginSuperUserTest extends AbstractWsIntegrationTest {

	@Test
	public void test() {
		final WebserviceResponse2 response = getResponse(getUri() + "/login?username=sysadm&password=adm_User" , WebserviceResponse2.class);
		Assertions.assertNotNull(response);
		Assertions.assertTrue(response.getSuccessful());
	}

}
