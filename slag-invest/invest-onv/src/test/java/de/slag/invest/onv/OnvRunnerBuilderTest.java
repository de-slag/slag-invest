package de.slag.invest.onv;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OnvRunnerBuilderTest {

	String notation = "20735";

	final String baseUrl = "https://www.onvista.de/onvista/boxes/historicalquote/export.csv?notationId=%s&dateStart=04.11.2019&interval=Y1";

	private Map<String, String> notationWknIsinMap = new HashMap<>();
	
	private String outputFolder = "/tmp";
	
	@BeforeEach
	public void setUp() {
		notationWknIsinMap.put("20735", "846900");
	}

	@Test
	void it() {
		final OnvRunner onvRunner = new OnvRunnerBuilder()
		   .withBaseUrl(baseUrl)
		   .withNotations(Collections.singletonList(notation))
		   .withNotationWknIsinMap(notationWknIsinMap)
		   .withOutputFolder(outputFolder)
		   .build();
		onvRunner.run();
	}

}
