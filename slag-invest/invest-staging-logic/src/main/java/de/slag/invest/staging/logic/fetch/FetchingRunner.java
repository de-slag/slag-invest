package de.slag.invest.staging.logic.fetch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.slag.invest.staging.logic.fetch.model.FetchSecurityPoint;
import de.slag.invest.staging.logic.mapping.IsinWknSybmolMapper;
import de.slag.invest.staging.logic.mapping.IsinWknSybmolMapperBuilder;
import de.slag.invest.staging.logic.mapping.IsinWknXstuNotationIdMapper;
import de.slag.invest.staging.logic.mapping.IsinWknXstuNotationIdMapperBuilder;
import de.slag.invest.staging.model.StaSecurityPoint;

public class FetchingRunner implements Runnable {

	private static final Log LOG = LogFactory.getLog(FetchingRunner.class);

	private Function<String, String> configurationProvider;

	private Consumer<StaSecurityPoint> securityPointPersister;

	private Supplier<StaSecurityPoint> newSecurityPointSupplier;

	private IsinWknSybmolMapper isinWknSybmolMapper;

	private IsinWknXstuNotationIdMapper isinWknXstuNotationIdMapper;

	public FetchingRunner(Function<String, String> configurationProvider,
			Consumer<StaSecurityPoint> securityPointPersister, Supplier<StaSecurityPoint> newSecurityPointSupplier) {
		super();
		this.configurationProvider = configurationProvider;
		this.securityPointPersister = securityPointPersister;
		this.newSecurityPointSupplier = newSecurityPointSupplier;
	}

	@Override
	public void run() {
		String stagingFetcher = getConfig("staging.fetcher");

		List<String> asList = Arrays.asList(stagingFetcher.split(";"));

		if (asList.isEmpty()) {
			return;
		}

		String mappingFileName = getConfig("mapping.file");

		isinWknSybmolMapper = new IsinWknSybmolMapperBuilder().withSourceFileName(mappingFileName).build();
		isinWknXstuNotationIdMapper = new IsinWknXstuNotationIdMapperBuilder().withSourceFileName(mappingFileName)
				.build();

		List<SecurityPointsFetcher> fetchers = asList.stream().map(c -> createFetchers(c)).filter(o -> o.isPresent())
				.map(o -> o.get()).collect(Collectors.toList());

		List<FetchSecurityPoint> allFetchSecurityPoints = new ArrayList<>();
		for (SecurityPointsFetcher securityPointsFetcher : fetchers) {
			Collection<FetchSecurityPoint> fetchSecurityPoints;
			try {
				fetchSecurityPoints = securityPointsFetcher.fetchSecurityPoints();
			} catch (Exception e) {
				LOG.error("error fetching security points", e);
				continue;
			}
			allFetchSecurityPoints.addAll(fetchSecurityPoints);
		}
		allFetchSecurityPoints.stream().map(f -> createStaSecurityPoint(f))
				.forEach(e -> securityPointPersister.accept(e));
	}

	private StaSecurityPoint createStaSecurityPoint(FetchSecurityPoint f) {
		StaSecurityPoint p = newSecurityPointSupplier.get();
		p.setCurrency(f.getCurrency());
		p.setIsinWkn(f.getIsinWkn());
		p.setSource(f.getSource());
		p.setTimestamp(f.getTimestamp());
		p.setValue(f.getValue());

		return p;
	}

	private Optional<SecurityPointsFetcher> createFetchers(String c) {
		switch (c) {
		case "av":
			return createAvFetcher();
		case "ov":
			// TODO implement
		case "xstu":
			// TODO implement
		default:
			return Optional.empty();
		}
	}

	private Optional<SecurityPointsFetcher> createAvFetcher() {
		String apikey = getConfig("staging.fetcher.av.apikey");

		String isinWkns = getConfig("staging.fetcher.av.isinWkns");

		Integer maxPerMinute = Integer.valueOf(getConfig("staging.fetcher.av.maxPerMinute", "4"));

		AvSecurityPointFetcher av = new AvSecurityPointFetcher(apikey, Arrays.asList(isinWkns), isinWknSybmolMapper,
				maxPerMinute);

		return Optional.of(av);
	}

	private String getConfig(String key) {
		return getConfig(key, null);
	}

	private String getConfig(String key, String defaultValue) {
		String apply = configurationProvider.apply(key);
		if (apply == null) {
			if (defaultValue == null) {
				throw new NoSuchElementException(key);
			}
			return defaultValue;
		}
		return apply;
	}

}
