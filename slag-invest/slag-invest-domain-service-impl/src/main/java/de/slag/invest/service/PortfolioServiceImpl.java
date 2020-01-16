package de.slag.invest.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import de.slag.invest.model.Portfolio;

@Service
public class PortfolioServiceImpl extends AbstractDomainServiceImpl<Portfolio> implements PortfolioService {

	@Override
	protected Class<Portfolio> getType() {
		return Portfolio.class;
	}

	@Override
	public Optional<Portfolio> loadBy(String portfolioNumber) {
		return findAll().stream().filter(p -> portfolioNumber.equals(p.getPortfolioNumber())).findAny();
	}

}
