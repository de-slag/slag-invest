package de.slag.invest.onv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.Builder;

public class OnvRunnerBuilder implements Builder<OnvRunner> {

	private Map<String, String> notationWknIsinMap = new HashMap<>();

	private List<String> notations = new ArrayList<>();
	
	private String baseUrl;
	
	private String outputFolder;
	
	public OnvRunnerBuilder withOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
		return this;
	}
	
	public OnvRunnerBuilder withBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
		return this;
	}

	public OnvRunnerBuilder withNotationWknIsinMap(Map<String, String> notationWknIsinMap) {
		this.notationWknIsinMap.putAll(notationWknIsinMap);
		return this;
	}

	public OnvRunnerBuilder withNotations(List<String> notations) {
		this.notations.addAll(notations);
		return this;
	}

	@Override
	public OnvRunner build() {
		return new OnvRunner(baseUrl, notations, notationWknIsinMap, outputFolder);
	}

}
