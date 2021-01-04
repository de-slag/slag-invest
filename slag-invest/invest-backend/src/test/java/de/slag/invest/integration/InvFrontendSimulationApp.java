package de.slag.invest.integration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import de.slag.basic.frontend.PropertiesSupplier;
import de.slag.basic.frontend.call.DemoCall;
import de.slag.basic.frontend.call.LoginCall;
import de.slag.basic.frontend.call.SaveEntityCall;
import de.slag.basic.frontend.call.builder.DemoCallBuilder;
import de.slag.basic.frontend.call.builder.LoginCallBuilder;
import de.slag.basic.frontend.call.builder.SaveEntityCallBuilder;
import de.slag.basic.model.EntityDto;
import de.slag.basic.model.Token;
import de.slag.common.base.BaseException;
import de.slag.common.core.eop.EopEntitiesBuilder;
import de.slag.common.core.eop.EopEntity;

public class InvFrontendSimulationApp implements Runnable {

	private static final String BACKEND_URL = "http://localhost:18080/invest-backend/";

	private Collection<EopEntity> eopEntities = new ArrayList<>();

	private void run0() throws Exception {
		final DemoCall demoCall = new DemoCallBuilder().withTarget(BACKEND_URL).build();
		final String call = demoCall.call();
		out(call);

		// login
		final Properties properties = new Properties();
		properties.put(PropertiesSupplier.FRONTEND_BACKEND_URL, BACKEND_URL);
		properties.put(PropertiesSupplier.FRONTEND_USER, "user0");
		properties.put(PropertiesSupplier.FRONTEND_PASSWORD, "pass");
		final PropertiesSupplier propertiesSupplier = new PropertiesSupplier() {

			@Override
			public Properties get() {
				return properties;
			}
		};
		final LoginCall build = new LoginCallBuilder(propertiesSupplier).build();
		final Token token = build.call();
		out("TokenString: " + token.getTokenString());

		saveAll(token);
		if (true) {
			return;
		}

		this.getClass();
	}

	private void saveAll(Token token) throws Exception {
		Collection<EntityDto> dtos = dtosOf(eopEntities);
		for (EntityDto entityDto : dtos) {
			save(entityDto, token);
		}
	}

	private Collection<EntityDto> dtosOf(Collection<EopEntity> eopEntities2) {
		return eopEntities2.stream()
				.map(e -> new EntityDtoBuilder().withEopEntity(e).build())
				.collect(Collectors.toList());
	}

	private void save(EntityDto entityDto, Token token) throws Exception {
		final SaveEntityCall saveEntityCall = new SaveEntityCallBuilder().withBackendUrl(BACKEND_URL)
				.withToken(token.getTokenString())
				.withEntityDto(entityDto)
				.build();

		final String saveSecurityResult = saveEntityCall.call();
		out(String.format("saved entity: ", saveSecurityResult));

	}

	private void out(Object o) {
		System.out.println(o);
	}

	@Override
	public void run() {
		try {
			init();
		} catch (IOException e) {
			throw new BaseException(e);
		}
		try {
			run0();
		} catch (Exception e) {
			throw new BaseException(e);
		}
	}

	private void init() throws FileNotFoundException, IOException {
		final Properties properties = new Properties();
		final InputStream resourceAsStream = this.getClass()
				.getClassLoader()
				.getResourceAsStream("integration/entities.properties");
		properties.load(resourceAsStream);
		this.eopEntities.addAll(new EopEntitiesBuilder().withProperties(properties).build());
	}

	public static void main(String[] args) {
		new InvFrontendSimulationApp().run();
	}

}
