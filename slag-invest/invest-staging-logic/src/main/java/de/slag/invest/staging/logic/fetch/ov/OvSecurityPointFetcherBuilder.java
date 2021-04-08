package de.slag.invest.staging.logic.fetch.ov;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import org.apache.commons.lang3.builder.Builder;

import de.slag.invest.staging.logic.mapping.IsinWkn;
import de.slag.invest.staging.logic.mapping.IsinWknOvNotationIdMapper;

public class OvSecurityPointFetcherBuilder implements Builder<OvSecurityPointFetcher> {

	private Collection<IsinWkn> isinWkns = new ArrayList<>();
	private IsinWknOvNotationIdMapper notationIdMapper;

	public OvSecurityPointFetcherBuilder withIsinWkns(Collection<IsinWkn> isinWkns) {
		this.isinWkns.addAll(isinWkns);
		return this;
	}
	
	public OvSecurityPointFetcherBuilder withNotationIdMapper(IsinWknOvNotationIdMapper notationIdMapper) {
		this.notationIdMapper = notationIdMapper;
		return this;
	}

	@Override
	public OvSecurityPointFetcher build() {
		Objects.requireNonNull(notationIdMapper, "mapper not setted");
		return new OvSecurityPointFetcher(notationIdMapper, isinWkns);
	}

}
