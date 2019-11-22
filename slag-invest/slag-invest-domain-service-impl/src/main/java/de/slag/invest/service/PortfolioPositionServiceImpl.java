package de.slag.invest.service;

import java.util.Collection;
import java.util.stream.Collectors;

import de.slag.invest.model.Portfolio;
import de.slag.invest.model.PortfolioPosition;

public class PortfolioPositionServiceImpl extends AbstractDomainServiceImpl<PortfolioPosition>
		implements PortfolioPositionService {

	public Collection<PortfolioPosition> findBy(Portfolio portfolio) {
		return findAllIds().stream()
				.map(id -> loadById(id))
				.filter(pp -> pp.getPortfolio().equals(portfolio))
				.collect(Collectors.toList());

	}

	@Override
	protected Class<PortfolioPosition> getType() {
		return PortfolioPosition.class;
	}

}
