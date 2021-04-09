package de.slag.invest.staging.logic.fetch.av;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.slag.common.util.SleepUtils;
import de.slag.invest.a4j.call.Alphavantage4jCallBuilder;
import de.slag.invest.staging.logic.a4j.call.Alphavantage4jCallUtils;
import de.slag.invest.staging.logic.a4j.call.AlphavantageStockDataModel;
import de.slag.invest.staging.logic.fetch.SecurityPointsFetcher;
import de.slag.invest.staging.logic.fetch.model.FetchSecurityPoint;
import de.slag.invest.staging.logic.mapping.IsinWkn;
import de.slag.invest.staging.logic.mapping.IsinWknSybmolMapper;
import de.slag.invest.staging.logic.mapping.Symbol;
import de.slag.invest.staging.model.SecurityPointSource;

public class AvSecurityPointFetcher implements SecurityPointsFetcher {
	
	private static final Log LOG = LogFactory.getLog(AvSecurityPointFetcher.class);

	private final Collection<IsinWkn> isinWkns = new ArrayList<>();

	private final String apiKey;

	private IsinWknSybmolMapper sybmolMapper;

	private int maxPerMinute;

	AvSecurityPointFetcher(String apiKey, Collection<IsinWkn> isinWkns, IsinWknSybmolMapper sybmolMapper,
			int maxPerMinute) {
		super();

		Objects.requireNonNull(apiKey, "apikey null");

		this.apiKey = apiKey;
		this.isinWkns.addAll(isinWkns);
		this.sybmolMapper = sybmolMapper;
		this.maxPerMinute = maxPerMinute;
	}

	@Override
	public Collection<FetchSecurityPoint> fetchSecurityPoints() throws Exception {
		ArrayList<FetchSecurityPoint> arrayList = new ArrayList<>();

		List<List<IsinWkn>> isinWknChunks = buildChunksOf(maxPerMinute, isinWkns);

		int count = 0;

		for (List<IsinWkn> isinWknChunk : isinWknChunks) {
			for (IsinWkn isinWkn : isinWknChunk) {
				Symbol symbol = sybmolMapper.apply(isinWkn);
				Alphavantage4jCallBuilder avCallBuilder = new Alphavantage4jCallBuilder();
				avCallBuilder.withApiKey(apiKey);
				avCallBuilder.withSymbol(symbol.getValue());
				Collection<Map<String, String>> call = Alphavantage4jCallUtils.call(avCallBuilder.build());
				call.forEach(e -> arrayList.add(createFetchSecurityPoint(e, isinWkn)));
				count++;
			}
			if (count >= isinWkns.size()) {
				break;
			}
			SleepUtils.sleepFor(60000);

		}
		


		LOG.info("recieved points: " + arrayList.size());

		return arrayList;
	}

	private List<List<IsinWkn>> buildChunksOf(int bunchSize, Collection<IsinWkn> all) {
		final AtomicInteger counter = new AtomicInteger();
		final Collection<List<IsinWkn>> result = all.stream()
				.collect(Collectors.groupingBy(it -> counter.getAndIncrement() / bunchSize)).values();
		return new ArrayList<List<IsinWkn>>(result);

	}

	private FetchSecurityPoint createFetchSecurityPoint(Map<String, String> pointData, IsinWkn isinWkn) {
		FetchSecurityPoint fetchSecurityPoint = new FetchSecurityPoint();
		fetchSecurityPoint.setIsinWkn(isinWkn.getValue());
		fetchSecurityPoint.setSource(SecurityPointSource.ALPHAVANTAGE);

		String string = pointData.get(AlphavantageStockDataModel.CLOSE);
		Double valueOf = Double.valueOf(string);
		BigDecimal value = BigDecimal.valueOf(valueOf);

		fetchSecurityPoint.setCurrency("EUR");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AlphavantageStockDataModel.DEFAULT_DATE_FORMAT);
		String source = pointData.get(AlphavantageStockDataModel.DATE);
		Date parse;
		try {
			parse = simpleDateFormat.parse(source);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		fetchSecurityPoint.setTimestamp(parse);

		fetchSecurityPoint.setValue(value);
		return fetchSecurityPoint;
	}

}