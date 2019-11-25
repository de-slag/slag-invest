package de.slag.invest.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import de.slag.invest.model.PortfolioPosition;

public class PortfolioPositionServiceImpl extends AbstractDomainServiceImpl<PortfolioPosition>
		implements PortfolioPositionService {

	@Override
	protected Class<PortfolioPosition> getType() {
		return PortfolioPosition.class;
	}

	@Override
	public Collection<PortfolioPosition> findByPortfolioNumber(String portfolioNumber) {
		assert StringUtils.isNotBlank(portfolioNumber);	
		return findAll().stream().filter(pp -> portfolioNumber.equals(pp.getPortfolioNumber()))
				.collect(Collectors.toList());
	}
}
