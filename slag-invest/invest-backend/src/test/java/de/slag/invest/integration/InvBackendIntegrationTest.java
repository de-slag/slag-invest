package de.slag.invest.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.slag.basic.frontend.call.DemoCall;
import de.slag.basic.frontend.call.LoginCall;
import de.slag.basic.frontend.call.builder.DemoCallBuilder;
import de.slag.basic.frontend.call.builder.LoginCallBuilder;
import de.slag.basic.model.Token;

@TestMethodOrder(OrderAnnotation.class)
public class InvBackendIntegrationTest {

	private static final String BACKEND_TARGET = InvBackendIntegrationConstants.BACKEND_TARGET;

	private static final String USER = InvBackendIntegrationConstants.BACKEND_TEST_USER;
	
	private static final String PASSWORD = InvBackendIntegrationConstants.BACKEND_TEST_PASSWORD;
	
	@Test
	@Order(1)
	void loginTest() throws Exception {
		final LoginCall loginCall = new LoginCallBuilder().withBackendUrl(BACKEND_TARGET)
				.withUser(USER)
				.withPassword(PASSWORD)
				.build();
		final Token token = loginCall.call();
		assertNotNull(token);
		assertNotNull(token.getTokenString());
		assertTrue(token.getTokenString().length() > 0);
	}

	@Test
	@Order(0)
	void demoTest() throws Exception {
		final DemoCall demoCall = new DemoCallBuilder().withTarget(BACKEND_TARGET).build();
		final String result = demoCall.call();
		assertNotNull(result);
		assertTrue(result.contains("ok"));
		assertTrue(result.contains("InvBackendServiceImpl"));
	}

}
