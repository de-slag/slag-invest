package de.slag.invest.staging.logic.fetch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.slag.common.util.ResourceUtils;
import de.slag.invest.staging.model.SecurityPointSource;
import de.slag.invest.staging.model.StaSecurityBasePoint;
import de.slag.invest.staging.model.StaSecurityPoint;

class FetchingRunnerIntegrationTest {

	private Properties properties = new Properties();
	private Collection<StaSecurityBasePoint> securityPoints = new ArrayList<>();

	private Function<String, String> configurationProvider;
	private Consumer<StaSecurityBasePoint> securityPointPersister;
	private Supplier<StaSecurityBasePoint> newSecurityPointSupplier;

	@BeforeEach
	void setUp() throws IOException {

		properties.load(new FileInputStream(ResourceUtils.getFileFromResources("config.properties")));

		properties.put("mapping.file", ResourceUtils.getFileFromResources("mapping.csv").toString());

		configurationProvider = key -> properties.getProperty(key);
		securityPointPersister = point -> securityPoints.add(point);
		newSecurityPointSupplier = () -> new StaSecurityBasePoint();

	}

	@Test
	void integrationTest() {

		FetchingRunner fetchingRunner = new FetchingRunner(configurationProvider, securityPointPersister,
				newSecurityPointSupplier);

		fetchingRunner.run();

		List<StaSecurityBasePoint> avPoints = securityPoints.stream()
				.filter(p -> p.getSource() == SecurityPointSource.ALPHAVANTAGE).collect(Collectors.toList());

		List<StaSecurityBasePoint> xstuPoints = securityPoints.stream()
				.filter(p -> p.getSource() == SecurityPointSource.BOERSE_STUTTGART).collect(Collectors.toList());

		List<StaSecurityBasePoint> ovPoints = securityPoints.stream()
				.filter(p -> p.getSource() == SecurityPointSource.ONVISTA).collect(Collectors.toList());

		assertEquals(100, avPoints.size());

		boolean equals40 = Integer.valueOf(40).equals(xstuPoints.size());
		boolean equals29 = Integer.valueOf(29).equals(xstuPoints.size());
		assertTrue(equals29 || equals40);

		boolean equals251 = Integer.valueOf(251).equals(ovPoints.size());
		boolean equals253 = Integer.valueOf(253).equals(ovPoints.size());

		assertTrue(equals251 || equals253);
	}

}
