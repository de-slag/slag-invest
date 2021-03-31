package de.slag.invest.staging.logic.fetch;

import java.util.function.Function;

import org.apache.commons.lang3.builder.Builder;

import de.slag.common.util.CsvUtils;

public class AvSecurityPointFetcherBuilder implements Builder<AvSecurityPointFetcher> {

	private final Function<String, String> configuration;

	public AvSecurityPointFetcherBuilder(Function<String, String> configuration) {
		super();
		this.configuration = configuration;
	}

	@Override
	public AvSecurityPointFetcher build() {
		
		// TODO Auto-generated method stub
		return null;
	}

}
