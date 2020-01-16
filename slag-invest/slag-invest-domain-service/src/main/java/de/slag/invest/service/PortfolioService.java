package de.slag.invest.service;

import java.util.Optional;

import de.slag.invest.model.Portfolio;

public interface PortfolioService extends DomainService<Portfolio> {
	
	Optional<Portfolio> loadBy(String portfolioNumber);

}
