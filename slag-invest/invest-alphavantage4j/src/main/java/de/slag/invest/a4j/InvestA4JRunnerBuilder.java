package de.slag.invest.a4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.builder.Builder;

/**
 * access class to this module
 * 
 * @author sebastian
 *
 */

public class InvestA4JRunnerBuilder implements Builder<InvestA4JRunner> {

	private String apiKey;
	private List<String> symbols = new ArrayList<>();
	private String outputFolder;
	private String symbolWknIsinMapping;

	/**
	 * 
	 * @param symbolWknIsinMapping - a key value mapping: 'sym1=wkn1;sym2=isin2;...'
	 * @return
	 */
	public InvestA4JRunnerBuilder withSymbolWknIsinMapping(String symbolWknIsinMapping) {
		this.symbolWknIsinMapping = symbolWknIsinMapping;
		return this;
	}

	public InvestA4JRunnerBuilder withSymbols(List<String> symbols) {
		this.symbols.addAll(symbols);
		return this;
	}

	public InvestA4JRunnerBuilder withOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
		return this;
	}

	public InvestA4JRunnerBuilder withApiKey(String apiKey) {
		this.apiKey = apiKey;
		return this;
	}

	@Override
	public InvestA4JRunner build() {
		Objects.requireNonNull(apiKey, "api key not setted");
		Objects.requireNonNull(outputFolder, "outputFolder not setted");
		Objects.requireNonNull(symbolWknIsinMapping, "symbolWknIsinMapping not setted");

		String[] split = symbolWknIsinMapping.split(";");
		List<String> asList = Arrays.asList(split);
		Map<String, String> symbolWknIsinMap = new HashMap<>();
		asList.forEach(entry -> {
			String[] keyValue = entry.split(":");
			symbolWknIsinMap.put(keyValue[0], keyValue[1]);
		});

		for (String symbol : symbols) {
			String wknIsin = symbolWknIsinMap.get(symbol);
			if (wknIsin == null) {
				throw new RuntimeException("no wknIsin found for symbol: " + symbol);
			}
		}
		if (symbols.size() > 4) {
			return new InvestA4JRunner(apiKey, symbols, outputFolder, symbolWknIsinMap, 15000);
		}

		return new InvestA4JRunner(apiKey, symbols, outputFolder, symbolWknIsinMap);

	}

}
