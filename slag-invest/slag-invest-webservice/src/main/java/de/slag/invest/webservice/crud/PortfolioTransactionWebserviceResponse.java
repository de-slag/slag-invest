package de.slag.invest.webservice.crud;

import java.util.ArrayList;
import java.util.Collection;

import de.slag.invest.model.PortfolioTransaction;
import de.slag.invest.webservice.response.WebserviceResponse;

public class PortfolioTransactionWebserviceResponse implements WebserviceResponse {

	private final Collection<PortfolioTransaction> beans = new ArrayList<>();

	public PortfolioTransactionWebserviceResponse(Collection<PortfolioTransaction> beans) {
		this.beans.addAll(beans);
	}

	@Override
	public Collection<PortfolioTransaction> getBeans() {
		return beans;
	}

}
