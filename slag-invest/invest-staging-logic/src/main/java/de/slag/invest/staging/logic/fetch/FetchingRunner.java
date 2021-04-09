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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.slag.common.model.beans.XiData;
import de.slag.invest.staging.logic.fetch.av.AvSecurityPointFetcherBuilder;
import de.slag.invest.staging.logic.fetch.model.FetchSecurityPoint;
import de.slag.invest.staging.logic.fetch.ov.OvSecurityPointFetcherBuilder;
import de.slag.invest.staging.logic.fetch.xstu.XstuSecurityPointFetcherBuilder;
import de.slag.invest.staging.logic.mapping.IsinWkn;
import de.slag.invest.staging.logic.mapping.IsinWknOvNotationIdMapper;
import de.slag.invest.staging.logic.mapping.IsinWknOvNotationIdMapperBuilder;
import de.slag.invest.staging.logic.mapping.IsinWknSybmolMapper;
import de.slag.invest.staging.logic.mapping.IsinWknSybmolMapperBuilder;
import de.slag.invest.staging.logic.mapping.IsinWknXstuNotationIdMapper;
import de.slag.invest.staging.logic.mapping.IsinWknXstuNotationIdMapperBuilder;

public class FetchingRunner implements Runnable {

	private static final Log LOG = LogFactory.getLog(FetchingRunner.class);

	private Function<String, String> configurationProvider;

	private Consumer<XiData> securityPointPersister;

	private Supplier<XiData> newSecurityPointSupplier;

	private IsinWknSybmolMapper isinWknSybmolMapper;

	private IsinWknXstuNotationIdMapper isinWknXstuNotationIdMapper;

	private IsinWknOvNotationIdMapper isinWknOvNotationIdMapper;

	public FetchingRunner(Function<String, String> configurationProvider, Consumer<XiData> securityPointPersister,
			Supplier<XiData> newSecurityPointSupplier, IsinWknSybmolMapper isinWknSybmolMapper,
			IsinWknXstuNotationIdMapper xstuNotationIdMapper, IsinWknOvNotationIdMapper isinWknOvNotationIdMapper) {
		super();
		this.configurationProvider = configurationProvider;
		this.securityPointPersister = securityPointPersister;
		this.newSecurityPointSupplier = newSecurityPointSupplier;
		this.isinWknSybmolMapper = isinWknSybmolMapper;
		this.isinWknXstuNotationIdMapper = xstuNotationIdMapper;
		this.isinWknOvNotationIdMapper = isinWknOvNotationIdMapper;

	}

	@Override
	public void run() {
		String stagingFetcher = getConfig("staging.fetcher");

		List<String> asList = Arrays.asList(stagingFetcher.split(";"));

		if (asList.isEmpty()) {
			return;
		}

//		String mappingFileName = getConfig("mapping.file");

//		isinWknSybmolMapper = new IsinWknSybmolMapperBuilder().withSourceFileName(mappingFileName).build();
//		isinWknXstuNotationIdMapper = new IsinWknXstuNotationIdMapperBuilder().withSourceFileName(mappingFileName)
//				.build();
//		isinWknOvNotationIdMapper = new IsinWknOvNotationIdMapperBuilder().withSourceFileName(mappingFileName).build();

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
		allFetchSecurityPoints.stream().map(f -> createExchangeImportData(f))
				.forEach(e -> securityPointPersister.accept(e));
	}

	private XiData createExchangeImportData(FetchSecurityPoint f) {
		XiData p = newSecurityPointSupplier.get();
		p.setType(FetchSecurityPoint.class.getSimpleName());
		p.addValue(FetchSecurityPoint.CURRENCY, f.getCurrency());
		p.addValue(FetchSecurityPoint.ISIN_WKN, f.getIsinWkn());
		p.addValue(FetchSecurityPoint.SECURITY_POINT_SOURCE, f.getSource().toString());
		p.addValue(FetchSecurityPoint.TIMESTAMP, f.getTimestamp().toString());
		p.addValue(FetchSecurityPoint.VALUE, f.getValue().toString());

		return p;
	}

	private Optional<SecurityPointsFetcher> createFetchers(String fetcherName) {
		switch (fetcherName) {
		case "av":
			return createAvFetcher();
		case "ov":
			return createOvFetcher();
		case "xstu":
			return createXstuFetcher();
		default:
			return Optional.empty();
		}
	}

	private Optional<SecurityPointsFetcher> createOvFetcher() {

		String isinWknsConfig = getConfig("staging.fetcher.ov.ininWkns");
		if (StringUtils.isEmpty(isinWknsConfig)) {
			return Optional.empty();
		}

		final List<IsinWkn> isinWkns = Arrays.asList(isinWknsConfig.split(";")).stream().map(e -> IsinWkn.of(e))
				.collect(Collectors.toList());

		return Optional.of(new OvSecurityPointFetcherBuilder().withNotationIdMapper(isinWknOvNotationIdMapper)
				.withIsinWkns(isinWkns).build());
	}

	private Optional<SecurityPointsFetcher> createXstuFetcher() {
		String isinWknsConfig = getConfig("staging.fetcher.xstu.isinWkns");
		if (StringUtils.isEmpty(isinWknsConfig)) {
			return Optional.empty();
		}

		final List<IsinWkn> isinWkns = Arrays.asList(isinWknsConfig.split(";")).stream().map(e -> IsinWkn.of(e))
				.collect(Collectors.toList());

		return Optional.of(new XstuSecurityPointFetcherBuilder().withIsinWkns(isinWkns)
				.withMapper(isinWknXstuNotationIdMapper).build());
	}

	private Optional<SecurityPointsFetcher> createAvFetcher() {
		String isinWknsConfig = getConfig("staging.fetcher.av.isinWkns");
		if (StringUtils.isEmpty(isinWknsConfig)) {
			return Optional.empty();
		}
		final List<IsinWkn> isinWkns = Arrays.asList(isinWknsConfig.split(";")).stream().map(e -> IsinWkn.of(e))
				.collect(Collectors.toList());

		String apikey = getConfig("staging.fetcher.av.apikey");

		Integer maxPerMinute = Integer.valueOf(getConfig("staging.fetcher.av.maxPerMinute", "4"));

		return Optional.of(new AvSecurityPointFetcherBuilder().withApiKey(apikey).withIsinWkns(isinWkns)
				.withIsinWknSybmolMapper(isinWknSybmolMapper).withMaxPerMinute(maxPerMinute).build());

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
