package de.slag.invest.service;

import java.util.Collection;

import de.slag.invest.model.PortfolioPosition;

public interface PortfolioPositionService extends DomainService<PortfolioPosition> {

	Collection<PortfolioPosition> findByPortfolioNumber(String portfolioNumber);

}
