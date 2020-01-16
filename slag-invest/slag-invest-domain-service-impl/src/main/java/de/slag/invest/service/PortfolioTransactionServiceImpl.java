package de.slag.invest.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.slag.invest.DomainBeanValidFilter;
import de.slag.invest.model.Portfolio;
import de.slag.invest.model.PortfolioTransaction;

@Service
public class PortfolioTransactionServiceImpl extends AbstractDomainServiceImpl<PortfolioTransaction>
		implements PortfolioTransactionService {

	@Override
	protected Class<PortfolioTransaction> getType() {
		return PortfolioTransaction.class;
	}

	@Override
	public Collection<PortfolioTransaction> findBy(Portfolio portfolio) {
		return findAll().stream().filter(t -> new DomainBeanValidFilter().test(t))
				.filter(t -> t.getPortfolioNumber().equals(portfolio.getPortfolioNumber()))
				.collect(Collectors.toList());
	}

}
