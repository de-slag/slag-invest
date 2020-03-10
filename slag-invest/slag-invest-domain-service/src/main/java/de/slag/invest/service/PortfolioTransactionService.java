package de.slag.invest.service;

import java.util.Collection;

import de.slag.invest.model.Portfolio;
import de.slag.invest.model.PortfolioTransaction;

public interface PortfolioTransactionService extends MandantBeanService<PortfolioTransaction> {
	
	Collection<PortfolioTransaction> findBy(Portfolio portfolio);

}
