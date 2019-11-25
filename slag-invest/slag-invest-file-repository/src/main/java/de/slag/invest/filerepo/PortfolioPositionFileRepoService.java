package de.slag.invest.filerepo;

import java.util.Collection;

public interface PortfolioPositionFileRepoService {
	
	Collection<PortfolioPositionDto> loadAll();

}
