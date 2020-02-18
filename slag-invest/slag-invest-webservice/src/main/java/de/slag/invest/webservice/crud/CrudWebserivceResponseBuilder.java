package de.slag.invest.webservice.crud;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.Builder;

import de.slag.invest.model.DomainBean;
import de.slag.invest.model.PortfolioTransaction;
import de.slag.invest.webservice.response.WebserviceResponse;

public class CrudWebserivceResponseBuilder implements Builder<WebserviceResponse> {

	private EntityType type;

	private Collection<DomainBean> beans = new ArrayList<>();

	public CrudWebserivceResponseBuilder withType(EntityType type) {
		this.type = type;
		return this;
	}

	public CrudWebserivceResponseBuilder withBeans(Collection<DomainBean> beans) {
		this.beans.addAll(beans);
		return this;
	}

	@Override
	public WebserviceResponse build() {
		switch (type) {
		case PORTFOLIO_TRANSACTION:
			final List<PortfolioTransaction> collect = beans.stream().map(b -> (PortfolioTransaction) b)
					.collect(Collectors.toList());

			final PortfolioTransactionWebserviceResponse resp = new PortfolioTransactionWebserviceResponse(collect);
			break;

		default:
			break;
		}
		// TODO Auto-generated method stub
		return null;
	}

}
