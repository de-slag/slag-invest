package de.slag.invest.a4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvestA4JRunnerTest {

	private final static BiFunction<ClassLoader, String, Collection<String>> LIST_FROM_RESOURCE_FILE = (classLoader, fileName) -> {

		InputStream resourceAsStream = classLoader.getResourceAsStream(fileName);
		return new BufferedReader(new InputStreamReader(resourceAsStream)).lines().parallel()
				.collect(Collectors.toList());
	};
	
	private final static Runnable SLEEP = () -> {
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	};

	private static final String API_KEY = "SEOG69AIA6X9PGR2";

	private final List<String> symbols = new ArrayList<>();

	private String symbolWknIsinMapping;

	@BeforeEach
	public void setUp() {
		ClassLoader classLoader = this.getClass().getClassLoader();
		Collection<String> apply = LIST_FROM_RESOURCE_FILE.apply(classLoader, "symbol-wkn-isin-mapping.properties");
		symbolWknIsinMapping = String.join(";", apply).replace("=", ":");
	
		symbols.clear();
		symbols.addAll(LIST_FROM_RESOURCE_FILE.apply(classLoader, "symbols.txt"));
		SLEEP.run();
	}

	@Test
	void integrationTest() {
		InvestA4JRunner investA4JRunner = new InvestA4JRunnerBuilder().withApiKey(API_KEY).withSymbols(symbols)
				.withOutputFolder(System.getProperty("java.io.tmpdir")).withSymbolWknIsinMapping(symbolWknIsinMapping)
				.build();
		investA4JRunner.run();
	}

	@Test
	void integration2Test() {
		List<String> subList = symbols.subList(0, 2);
		InvestA4JRunner investA4JRunner = new InvestA4JRunnerBuilder().withApiKey(API_KEY).withSymbols(subList)
				.withOutputFolder(System.getProperty("java.io.tmpdir")).withSymbolWknIsinMapping(symbolWknIsinMapping)
				.build();
		investA4JRunner.run();
	}

}
