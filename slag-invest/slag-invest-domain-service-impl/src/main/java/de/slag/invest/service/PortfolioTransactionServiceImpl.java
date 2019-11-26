package de.slag.invest.service;

import org.springframework.stereotype.Service;

import de.slag.invest.model.PortfolioTransaction;

@Service
public class PortfolioTransactionServiceImpl extends AbstractDomainServiceImpl<PortfolioTransaction>
		implements PortfolioTransactionService {

	@Override
	protected Class<PortfolioTransaction> getType() {
		return PortfolioTransaction.class;
	}

}
