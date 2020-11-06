package de.slag.invest.a4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.patriques.output.timeseries.data.StockData;

import de.slag.invest.a4j.call.Alphavantage4jCall;
import de.slag.invest.a4j.call.Alphavantage4jCallBuilder;
import de.slag.staging.StagingCsvLineBuilder;
import de.slag.staging.StagingModelUtils;
import de.slag.staging.StagingStockData;
import de.slag.staging.model.SecurityCsv;

public class InvestA4JRunner implements Runnable {

	private String apiKey;

	private List<String> symbols = new ArrayList<String>();

	private Map<String, String> symbolWknIsinMap = new HashMap<>();

	private String outputFolder;

	private long sleepTimeBetween;

	InvestA4JRunner(String apiKey, List<String> symbols, String outputFolder, Map<String, String> symbolWknIsinMap) {
		this(apiKey, symbols, outputFolder, symbolWknIsinMap, 0);
	}

	InvestA4JRunner(String apiKey, List<String> symbols, String outputFolder, Map<String, String> symbolWknIsinMap,
			long sleepTimeBetween) {
		super();
		this.apiKey = apiKey;
		this.symbols.addAll(symbols);
		this.outputFolder = outputFolder;
		this.symbolWknIsinMap.putAll(symbolWknIsinMap);
		this.sleepTimeBetween = sleepTimeBetween;
	}

	@Override
	public void run() {
		final Date fetchTimestamp = new Date();
		final List<List<String>> csvLines = new ArrayList<>();
		for (String symbol : symbols) {
			final Alphavantage4jCall a4jCall = new Alphavantage4jCallBuilder().withApiKey(apiKey).withSymbol(symbol)
					.build();

			final List<StockData> stockData;
			try {
				stockData = a4jCall.call();
			} catch (Exception e) {
				throw new RuntimeException("at symbol: " + symbol, e);
			}

			stockData.forEach(data -> {
				final List<String> csvLine = new StagingCsvLineBuilder()
						.withFetchTimestamp(fetchTimestamp)
						.withWknIsin(symbolWknIsinMap.get(symbol))
						.withStockData(stagingDataOf(data))
						.withSourceInfo("av:" + symbol)
						.build();
				csvLines.add(csvLine);
			});
			sleepFor(sleepTimeBetween);
		}
		System.out.println(csvLines.size() + " datasets fetched.");
		writeOut(csvLines);
	}

	private StagingStockData stagingDataOf(StockData data) {
		return new StagingStockData() {

			@Override
			public long getVolume() {
				return data.getVolume();
			}

			@Override
			public double getOpen() {
				return data.getOpen();
			}

			@Override
			public double getLow() {
				return data.getLow();
			}

			@Override
			public double getHigh() {
				return data.getHigh();
			}

			@Override
			public LocalDateTime getDateTime() {
				return data.getDateTime();
			}

			@Override
			public double getClose() {
				return data.getClose();
			}
		};
	}

	private void writeOut(List<List<String>> csvLines) {
		StagingModelUtils.writeOut(csvLines, outputFolder);
	}

	private void sleepFor(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
