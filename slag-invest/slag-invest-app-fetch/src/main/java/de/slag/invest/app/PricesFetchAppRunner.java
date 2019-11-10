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
import de.slag.invest.av.call.StockValueCall;
import de.slag.invest.av.call.StockValueCallBuilder;
import de.slag.invest.av.model.AvStockValue;
import de.slag.invest.av.response.AvStockValueResponse;

public class PricesFetchAppRunner implements Runnable {

	private static final Log LOG = LogFactory.getLog(PricesFetchAppRunner.class);

	private AdmSupport admSuppoprt = AdmSupport.getInstance();

	private static String ISIN = "ISIN";
	private static String SYMBOL = "SYMBOL";

	private Map<String, Collection<AvStockValue>> allValues = new HashMap<>();

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

			StockValueCallBuilder stockValueCallBuilder = new StockValueCallBuilder().symbol(symbol).apiKey(apiKey);

			final Optional<String> timeoutOptional = admSuppoprt.getProperty(AvProperties.TIMEOUT);
			if (timeoutOptional.isPresent()) {
				stockValueCallBuilder.timeOut(Integer.valueOf(timeoutOptional.get()));
			}

			final StockValueCall stockValueCall = stockValueCallBuilder.symbol(symbol).apiKey(apiKey).build();

			final StockValueCallRunner runner = new StockValueCallRunner(stockValueCall);
			tasks.add(runner);
		});

		final TimedExecutor timedExecutor = new TimedExecutor(5, tasks);
		timedExecutor.execute();

		tasks.forEach(runner -> {
			final AvStockValueResponse response = runner.getResponse();
			final String symbol = runner.getSymbol();
			Collection<AvStockValue> values = response.getValues();
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

			final Collection<AvStockValue> values = allValues.get(isin);
			values.forEach(value -> {
				List<String> row = new ArrayList<>();
				row.add(isin);
				row.add(value.getDate().toString());
				row.add(value.getOpen().toString());
				row.add(value.getHigh().toString());
				row.add(value.getLow().toString());
				row.add(value.getClose().toString());
				row.add(value.getVolume().toString());
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