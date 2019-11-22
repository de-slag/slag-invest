package de.slag.invest.service;

import org.springframework.stereotype.Service;

import de.slag.invest.model.Portfolio;

@Service
public class PortfolioServiceImpl extends AbstractDomainServiceImpl<Portfolio> implements PortfolioService {

	@Override
	protected Class<Portfolio> getType() {
		return Portfolio.class;
	}

}
