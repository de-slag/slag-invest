package de.slag.invest.staging.logic.fetch.xstu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import org.apache.commons.lang3.builder.Builder;

import de.slag.invest.staging.logic.mapping.IsinWkn;
import de.slag.invest.staging.logic.mapping.IsinWknXstuNotationIdMapper;

public class XstuSecurityPointFetcherBuilder implements Builder<XstuSecurityPointFetcher> {

	private Collection<IsinWkn> isinWkns = new ArrayList<>();
	private IsinWknXstuNotationIdMapper mapper;

	public XstuSecurityPointFetcherBuilder withIsinWkns(Collection<IsinWkn> isinWkns) {
		this.isinWkns.addAll(isinWkns);
		return this;
	}

	public XstuSecurityPointFetcherBuilder withMapper(IsinWknXstuNotationIdMapper mapper) {
		this.mapper = mapper;
		return this;
	}

	@Override
	public XstuSecurityPointFetcher build() {
		Objects.requireNonNull(mapper, "mapper not setted");
		return new XstuSecurityPointFetcher(isinWkns, mapper);
	}

}
