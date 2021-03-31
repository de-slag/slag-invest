package de.slag.invest.staging.logic.fetch.av;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.builder.Builder;

import de.slag.invest.staging.logic.mapping.IsinWknSybmolMapper;

public class AvSecurityPointFetcherBuilder implements Builder<AvSecurityPointFetcher> {

	private String apiKey;
	private final Collection<String> isinWkns = new ArrayList<>();
	private IsinWknSybmolMapper sybmolMapper;
	private int maxPerMinute;
	
	public AvSecurityPointFetcherBuilder withMaxPerMinute(int maxPerMinute) {
		this.maxPerMinute = maxPerMinute;
		return this;
	}

	public AvSecurityPointFetcherBuilder withIsinWknSybmolMapper(IsinWknSybmolMapper sybmolMapper) {
		this.sybmolMapper = sybmolMapper;
		return this;
	}
	
	
	public AvSecurityPointFetcherBuilder withIsinWkns(Collection<String> isinWkns) {
		this.isinWkns.addAll(isinWkns);
		return this;
	}
	
	public AvSecurityPointFetcherBuilder withApiKey(String apiKey) {
		this.apiKey = apiKey;
		return this;
	}

	@Override
	public AvSecurityPointFetcher build() {
		return new AvSecurityPointFetcher(apiKey, isinWkns, sybmolMapper, maxPerMinute);
	}

}
