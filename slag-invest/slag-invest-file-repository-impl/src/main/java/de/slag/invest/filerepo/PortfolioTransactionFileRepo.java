package de.slag.invest.filerepo;

import java.util.Collection;

public interface PortfolioTransactionFileRepo {
	
	Collection<PortfolioTransactionDto> findAll();

}
