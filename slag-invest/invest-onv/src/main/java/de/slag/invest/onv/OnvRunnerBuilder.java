package de.slag.invest.onv;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.Builder;

import de.slag.common.base.BaseException;

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
		Objects.requireNonNull(outputFolder, "output folder not setted");
		if (!new File(outputFolder).exists()) {
			throw new BaseException("output folder not found: " + outputFolder);
		}

		if (StringUtils.isBlank(baseUrl)) {
			throw new BaseException("baseUrl not setted");
		}

		notations.forEach(notation -> {
			if (!notationWknIsinMap.containsKey(notation)) {
				throw new BaseException(
						String.format("notation '%s' not in notationWknIsinMap '%s", notation, notationWknIsinMap));
			}
		});

		return new OnvRunner(baseUrl, notations, notationWknIsinMap, outputFolder);
	}

}
