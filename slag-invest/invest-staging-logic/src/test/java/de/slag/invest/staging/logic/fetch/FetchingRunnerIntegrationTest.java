package de.slag.invest.staging.logic.fetch;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import java.util.stream.Stream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.slag.common.util.ResourceUtils;
import de.slag.invest.staging.model.SecurityPointSource;
import de.slag.invest.staging.model.StaSecurityPoint;

class FetchingRunnerIntegrationTest {

	private static final Log LOG = LogFactory.getLog(FetchingRunnerIntegrationTest.class);

	private Properties properties = new Properties();
	private Collection<StaSecurityPoint> securityPoints = new ArrayList<>();

	private Function<String, String> configurationProvider;
	private Consumer<StaSecurityPoint> securityPointPersister;
	private Supplier<StaSecurityPoint> newSecurityPointSupplier;

	@BeforeEach
	void setUp() throws IOException {

		properties.load(new FileInputStream(ResourceUtils.getFileFromResources("config.properties")));

		properties.put("mapping.file", ResourceUtils.getFileFromResources("mapping.csv").toString());

		configurationProvider = key -> properties.getProperty(key);
		securityPointPersister = point -> securityPoints.add(point);
		newSecurityPointSupplier = () -> new StaSecurityPoint();

	}

	@Test
	void integrationTest() {
		FetchingRunner fetchingRunner = new FetchingRunner(configurationProvider, securityPointPersister,
				newSecurityPointSupplier);

		fetchingRunner.run();

		List<StaSecurityPoint> avPoints = securityPoints.stream()
				.filter(p -> p.getSource() == SecurityPointSource.ALPHAVANTAGE).collect(Collectors.toList());

		List<StaSecurityPoint> xstuPoints = securityPoints.stream()
				.filter(p -> p.getSource() == SecurityPointSource.BOERSE_STUTTGART).collect(Collectors.toList());

		assertEquals(100, avPoints.size());
		assertEquals(50, xstuPoints.size());
	}

}
