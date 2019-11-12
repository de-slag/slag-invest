package de.slag.invest.app;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.slag.common.base.BaseException;
import de.slag.common.utils.CsvUtils;
import de.slag.common.utils.DateUtils;
import de.slag.invest.av.AvProperties;
import de.slag.invest.av.stock.AvStock;
import de.slag.invest.av.stock.AvStockCall;
import de.slag.invest.av.stock.AvStockCallBuilder;
import de.slag.invest.av.stock.AvStockResponse;

public class PricesFetchAppRunner implements Runnable {

	private static final Log LOG = LogFactory.getLog(PricesFetchAppRunner.class);

	private AdmSupport admSuppoprt = AdmSupport.getInstance();

	private static String ISIN = "ISIN";
	private static String SYMBOL = "SYMBOL";

	private Map<String, Collection<AvStock>> allValues = new HashMap<>();

	private Map<String, String> SYMBOL_ISIN_MAP = new HashMap<String, String>();

	@Override
	public void run() {
		final String fetchValuesFilePath = getProperty(PricesFetchAppProperties.FETCHVALUES_FILE);
		String fetchValuesOutputDirPath = getProperty(PricesFetchAppProperties.FETCHVALUES_OUTPUT_DIR);
		final String apiKey = getProperty(AvProperties.API_KEY);
		final LocalDateTime fetchTime = LocalDateTime.now();

		validateHeader(fetchValuesFilePath);
		Collection<CSVRecord> records = CsvUtils.getRecords(fetchValuesFilePath);
		Collection<StockValueCallRunner> tasks = new ArrayList<>();

		records.forEach(record -> {
			final String symbol = record.get(SYMBOL);
			final String isin = record.get(ISIN);

			SYMBOL_ISIN_MAP.put(symbol, isin);

			AvStockCallBuilder stockValueCallBuilder = new AvStockCallBuilder().symbol(symbol).apiKey(apiKey);

			final Optional<String> timeoutOptional = admSuppoprt.getProperty(AvProperties.TIMEOUT);
			if (timeoutOptional.isPresent()) {
				stockValueCallBuilder.readTimeOut(Integer.valueOf(timeoutOptional.get()));
			}

			final AvStockCall stockValueCall = stockValueCallBuilder.symbol(symbol).apiKey(apiKey).build();

			final StockValueCallRunner runner = new StockValueCallRunner(stockValueCall);
			tasks.add(runner);
		});

		final TimedExecutor timedExecutor = new TimedExecutor(5, tasks);
		timedExecutor.execute();

		tasks.forEach(runner -> {
			final AvStockResponse response = runner.getResponse();
			Collection<AvStock> values = response.getStocks();
			Optional<AvStock> findAny = values.stream().findAny();
			if(!findAny.isPresent()) {
				LOG.debug("no stocks found");
				return;
			}
			AvStock avStock = findAny.get();
			String symbol = avStock.getSymbol();
			LOG.info(String.format("call for symbol '%s' done. %s values recieved.", symbol, values.size()));

			final String isin = SYMBOL_ISIN_MAP.get(symbol);
			allValues.put(isin, values);

		});

		final List<String> conclusion = new ArrayList<>();
		allValues.keySet().forEach(isin -> conclusion.add(String.format("%s: %s", isin, allValues.get(isin).size())));
		LOG.info(String.format("all fetched:\n%s", String.join("\n", conclusion)));

		final List<String> header = Arrays.asList(ISIN, "DATE", "OPEN", "HIGH", "LOW", "CLOSE", "VOLUME", "FETCHED");
		final String formattedFetchTime = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss")
				.format(DateUtils.toDate(fetchTime));
		allValues.keySet().forEach(isin -> {

			Collection<Collection<String>> resultList = new ArrayList<>();

			final Collection<AvStock> values = allValues.get(isin);
			values.forEach(value -> {
				List<String> row = new ArrayList<>();
				row.add(isin);
				row.add(value.getDate().toString());
				row.add(value.getOpen().toString());
				row.add(value.getHigh().toString());
				row.add(value.getLow().toString());
				row.add(value.getClose().toString());
				row.add(Long.toString(value.getVolume()));
				row.add(formattedFetchTime);
				resultList.add(row);
			});

			final String filename = isin + "_" + DateUtils.toDate(fetchTime).getTime();
			final String filePath = fetchValuesOutputDirPath + "/" + filename + ".csv";
			try {
				CsvUtils.write(filePath, header, resultList);
			} catch (IOException e) {
				throw new BaseException(e);
			}

		});

		LOG.info("all done");

	}

	private void validateHeader(String fetchValuesFilePath) {
		final Collection<String> header = CsvUtils.getHeader(fetchValuesFilePath);
		if (!header.contains(ISIN)) {
			throw new BaseException("header does not contains: %s", ISIN);
		}
		if (!header.contains(SYMBOL)) {
			throw new BaseException("header does not contains: %s", SYMBOL);
		}
	}

	private String getProperty(String fetchvaluesFile) {
		return admSuppoprt.getProperty(fetchvaluesFile)
				.orElseThrow(() -> new BaseException("not setted: '%s'", fetchvaluesFile));
	}

};