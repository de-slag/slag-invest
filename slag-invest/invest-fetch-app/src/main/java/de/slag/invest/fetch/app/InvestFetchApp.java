package de.slag.invest.fetch.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import de.slag.invest.a4j.InvestA4JRunner;
import de.slag.invest.a4j.InvestA4JRunnerBuilder;
import de.slag.invest.onv.OnvRunner;
import de.slag.invest.onv.OnvRunnerBuilder;

public class InvestFetchApp implements Runnable {

	private Properties properties = new Properties();

	public void init(String configFileName) throws IOException {
		properties.load(new FileInputStream(configFileName));
	}

	public void run() {
		fetchFromAlphavantage();
		fetchFromOnv();
	}

	private void fetchFromOnv() {
		final List<String> notations = Arrays
				.asList(properties.getProperty(InvestFetchProperty.NOTATION_IDS).split(";"));
		final List<String> notationEntries = Arrays
				.asList(properties.getProperty(InvestFetchProperty.NOTATION_WKN_ISIN_MAPPING).split(";"));
		final Map<String, String> notationWknIsinMap = new HashMap<>();
		for (String notationMappingEntry : notationEntries) {
			final String[] split = notationMappingEntry.split(":");
			notationWknIsinMap.put(split[0], split[1]);
		}

		final OnvRunner runner = new OnvRunnerBuilder()
				.withBaseUrl(properties.getProperty(InvestFetchProperty.BASE_URL))
				.withNotations(notations)
				.withNotationWknIsinMap(notationWknIsinMap)
				.withOutputFolder(properties.getProperty(InvestFetchProperty.OUTPUT_FOLDER))
				.build();

		runner.run();

	}

	private void fetchFromAlphavantage() {
		final String sym = properties.getProperty(InvestFetchProperty.SYMBOLS);

		final List<String> symbols = Arrays.asList(sym.split(";"));
		final InvestA4JRunner runner = new InvestA4JRunnerBuilder()
				.withApiKey(properties.getProperty(InvestFetchProperty.API_KEY))
				.withOutputFolder(properties.getProperty(InvestFetchProperty.OUTPUT_FOLDER))
				.withSymbolWknIsinMapping(properties.getProperty(InvestFetchProperty.SYMBOL_WKN_ISIN_MAPPING))
				.withSymbols(symbols).build();

		runner.run();
	}

	public static void main(String[] args) throws IOException {
		final InvestFetchApp investFetchApp = new InvestFetchApp();
		investFetchApp.init(args[0]);
		investFetchApp.run();
	}

}
