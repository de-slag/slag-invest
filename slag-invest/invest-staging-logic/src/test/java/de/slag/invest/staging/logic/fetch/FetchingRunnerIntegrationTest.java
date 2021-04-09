package de.slag.invest.staging.logic.fetch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.slag.common.model.beans.XiData;
import de.slag.common.util.CsvUtils;
import de.slag.common.util.ResourceUtils;
import de.slag.invest.staging.logic.fetch.model.FetchSecurityPoint;
import de.slag.invest.staging.logic.mapping.IsinWknOvNotationIdMapper;
import de.slag.invest.staging.logic.mapping.IsinWknOvNotationIdMapperBuilder;
import de.slag.invest.staging.logic.mapping.IsinWknSybmolMapper;
import de.slag.invest.staging.logic.mapping.IsinWknSybmolMapperBuilder;
import de.slag.invest.staging.logic.mapping.IsinWknXstuNotationIdMapper;
import de.slag.invest.staging.logic.mapping.IsinWknXstuNotationIdMapperBuilder;
import de.slag.invest.staging.model.SecurityPointSource;

class FetchingRunnerIntegrationTest {

	private Properties properties = new Properties();
	private Collection<XiData> securityPoints = new ArrayList<>();

	private Function<String, String> configurationProvider;
	private Consumer<XiData> securityPointPersister;
	private Supplier<XiData> newSecurityPointSupplier;

	IsinWknXstuNotationIdMapper isinWknXstuNotationIdMapper;

	IsinWknSybmolMapper isinWknSybmolMapper;

	IsinWknOvNotationIdMapper isinWknOvNotationIdMapper;

	@BeforeEach
	void setUp() throws IOException {

		properties.load(new FileInputStream(ResourceUtils.getFileFromResources("config.properties")));

		File csvfile = ResourceUtils.getFileFromResources("mapping.csv");
		properties.put("mapping.file", csvfile.toString());

		configurationProvider = key -> properties.getProperty(key);
		securityPointPersister = point -> securityPoints.add(point);
		newSecurityPointSupplier = () -> new XiData();

		isinWknSybmolMapper = new IsinWknSybmolMapperBuilder().withProvider(provider(csvfile, "SYMBOL"))
				.build();

		isinWknXstuNotationIdMapper = new IsinWknXstuNotationIdMapperBuilder()
				.withProvider(provider(csvfile, "XSTU_NOTATION_ID")).build();

		isinWknOvNotationIdMapper = new IsinWknOvNotationIdMapperBuilder()
				.withProvider(provider(csvfile, "OV_NOTATION_ID")).build();
	}

	private Function<String, Optional<String>> provider(File csvfile, String fieldName) {
		return isinWkn -> {
			Collection<CSVRecord> readRecords = CsvUtils.readRecords(csvfile.toString());
			Optional<CSVRecord> findAny = readRecords.stream().filter(rec -> rec.get("ISIN_WKN").equals(isinWkn))
					.findAny();
			if (findAny.isEmpty()) {
				return Optional.empty();
			}
			CSVRecord csvRecord = findAny.get();
			String value = csvRecord.get(fieldName);
			if (value == null) {
				return Optional.empty();
			}
			return Optional.of(value);
		};
	}

	@Test
	void integrationTest() {

		FetchingRunner fetchingRunner = new FetchingRunner(configurationProvider, securityPointPersister,
				newSecurityPointSupplier, isinWknSybmolMapper, isinWknXstuNotationIdMapper, isinWknOvNotationIdMapper);

		fetchingRunner.run();

		List<XiData> avPoints = securityPoints.stream().filter(createFilterOf(SecurityPointSource.ALPHAVANTAGE))
				.collect(Collectors.toList());

		List<XiData> xstuPoints = securityPoints.stream().filter(createFilterOf(SecurityPointSource.BOERSE_STUTTGART))
				.collect(Collectors.toList());

		List<XiData> ovPoints = securityPoints.stream().filter(createFilterOf(SecurityPointSource.ONVISTA))
				.collect(Collectors.toList());

		assertEquals(100, avPoints.size());

		boolean equals40 = Integer.valueOf(40).equals(xstuPoints.size());
		boolean equals29 = Integer.valueOf(29).equals(xstuPoints.size());
		boolean equals71 = Integer.valueOf(71).equals(xstuPoints.size());
		assertTrue(equals29 || equals40 || equals71);

		boolean equals251 = Integer.valueOf(251).equals(ovPoints.size());
		boolean equals253 = Integer.valueOf(253).equals(ovPoints.size());

		assertTrue(equals251 || equals253);
	}

	private Predicate<XiData> createFilterOf(SecurityPointSource sps) {
		return xiData -> {
			String s = xiData.getValue(FetchSecurityPoint.SECURITY_POINT_SOURCE);
			SecurityPointSource valueOf = SecurityPointSource.valueOf(s);
			return valueOf == sps;
		};
	}

}
