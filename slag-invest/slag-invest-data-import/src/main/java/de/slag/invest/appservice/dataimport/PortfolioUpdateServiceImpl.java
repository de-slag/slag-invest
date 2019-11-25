package de.slag.invest.appservice.dataimport;

import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import de.slag.invest.filerepo.PortfolioPositionDto;
import de.slag.invest.filerepo.PortfolioPositionFileRepoService;
import de.slag.invest.service.PortfolioPositionService;

@Service
public class PortfolioUpdateServiceImpl implements PortfolioUpdateService {
	
	@Resource
	private PortfolioPositionFileRepoService portfolioPositionFileRepoService;
	
	@Resource
	private PortfolioPositionService portfolioPositionService;

	@Override
	public void updatePortfolios() {
		final Collection<PortfolioPositionDto> dtos = portfolioPositionFileRepoService.findAll();
		// FIXME
		
	}

}
