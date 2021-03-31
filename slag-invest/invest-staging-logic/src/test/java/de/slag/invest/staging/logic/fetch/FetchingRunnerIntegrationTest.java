package de.slag.invest.staging.logic.fetch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.slag.common.util.ResourceUtils;
import de.slag.invest.staging.model.StaSecurityPoint;

class FetchingRunnerIntegrationTest {

	private static final Log LOG = LogFactory.getLog(FetchingRunnerIntegrationTest.class);

	private Map<String, String> configuration = new HashMap<>();
	private Collection<StaSecurityPoint> securityPoints = new ArrayList<>();

	private Function<String, String> configurationProvider;
	private Consumer<StaSecurityPoint> securityPointPersister;
	private Supplier<StaSecurityPoint> newSecurityPointSupplier;

	@BeforeEach
	void setUp() throws IOException {
		File fileFromResources = ResourceUtils.getFileFromResources("mapping.csv");

		configuration.put("staging.fetcher", "av");
		configuration.put("staging.fetcher.av.apikey", "SEOG69AIA6X9PGR2");
		configuration.put("staging.fetcher.av.isinWkns", "DE0007164600");
		configuration.put("mapping.file", fileFromResources.toString());

		configurationProvider = key -> configuration.get(key);
		securityPointPersister = point -> securityPoints.add(point);
		newSecurityPointSupplier = () -> new StaSecurityPoint();

	}

	@Test
	void integrationTest() {
		FetchingRunner fetchingRunner = new FetchingRunner(configurationProvider, securityPointPersister,
				newSecurityPointSupplier);

		fetchingRunner.run();

		assertEquals(100, securityPoints.size());
	}

}
