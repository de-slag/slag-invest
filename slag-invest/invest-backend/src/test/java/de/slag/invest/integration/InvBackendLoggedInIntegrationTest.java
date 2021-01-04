package de.slag.invest.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.slag.basic.frontend.PropertiesSupplier;
import de.slag.basic.frontend.call.ConfigCall;
import de.slag.basic.frontend.call.LoginCall;
import de.slag.basic.frontend.call.RunDefaultCall;
import de.slag.basic.frontend.call.SaveEntityCall;
import de.slag.basic.frontend.call.builder.LoginCallBuilder;
import de.slag.basic.frontend.call.builder.RunDefaultCallBuilder;
import de.slag.basic.frontend.call.builder.SaveConfigCallBuilder;
import de.slag.basic.frontend.call.builder.SaveEntityCallBuilder;
import de.slag.basic.model.EntityDto;
import de.slag.basic.model.Token;
import de.slag.common.core.eop.EopEntitiesBuilder;
import de.slag.common.core.eop.EopEntity;

@TestMethodOrder(OrderAnnotation.class)
public class InvBackendLoggedInIntegrationTest {

	private final Collection<EopEntity> eopEntities = new ArrayList<>();

	private final Collection<EntityDto> entityDtos = new ArrayList<>();

	String target = InvBackendIntegrationConstants.BACKEND_TARGET;

	String user = InvBackendIntegrationConstants.BACKEND_TEST_USER;

	String password = InvBackendIntegrationConstants.BACKEND_TEST_PASSWORD;

	Token token;

	@BeforeEach
	void setUp() throws IOException {
		final Properties properties = new Properties();
		final InputStream resourceAsStream = this.getClass()
				.getClassLoader()
				.getResourceAsStream("integration/entities.properties");
		properties.load(resourceAsStream);
		eopEntities.addAll(new EopEntitiesBuilder().withProperties(properties).build());
		eopEntities.forEach(e -> entityDtos.add(new EntityDtoBuilder().withEopEntity(e).build()));

	}

	@BeforeEach
	void logIn() throws Exception {
		final LoginCall loginCall = new LoginCallBuilder().withBackendUrl(target)
				.withUser(user)
				.withPassword(password)
				.build();
		token = loginCall.call();
		assertNotNull(token);
		assertNotNull(token.getTokenString());
		assertTrue(token.getTokenString().length() > 0);
	}

	@Test
	@Order(0)
	void saveConfigTest() throws Exception {
		final Properties properties = new Properties();
		properties.put("test", "test-value");
		final ConfigCall configCall = SaveConfigCallBuilder.instance()
				.withToken(token.getTokenString())
				.withBackendUrl(target)
				.withProperties(properties)
				.build();
		final String result = configCall.call();
	}

	@Test
	@Order(1)
	void saveEntitiesTest() {
		AtomicInteger succesfulCalls = new AtomicInteger(0);
		entityDtos.forEach(e -> {
			final SaveEntityCallBuilder withBackendUrl = new SaveEntityCallBuilder().withToken(token.getTokenString())
					.withEntityDto(e)
					.withBackendUrl(target);
			final SaveEntityCall saveEntityCall = withBackendUrl.build();
			String response;
			try {
				response = saveEntityCall.call();
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}
			assertNotNull(response);
			succesfulCalls.incrementAndGet();
		});
		assertEquals(2, succesfulCalls.intValue());
	}

	@Test
	@Order(66)
	void runDefaultTest() throws Exception {
		final RunDefaultCall runDefaultCall = new RunDefaultCallBuilder().withTarget(target)
				.withToken(token.getTokenString())
				.build();
		final String response = runDefaultCall.call();
		assertNotNull(response);
	}

}
