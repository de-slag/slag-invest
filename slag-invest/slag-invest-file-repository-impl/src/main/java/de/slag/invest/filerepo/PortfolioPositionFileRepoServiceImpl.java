package de.slag.invest.filerepo;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import de.slag.common.base.AdmCache;
import de.slag.common.base.BaseException;

public class PortfolioPositionFileRepoServiceImpl implements PortfolioPositionFileRepoService {

	private static final String DE_SLAG_INVEST_FILEREPO = "de.slag.invest.filerepo";

	@Resource
	private AdmCache admCache;

	@PostConstruct
	public void setUp() {
		admCache.getValue(DE_SLAG_INVEST_FILEREPO)
				.orElseThrow(() -> new BaseException("not configured: '%s'", DE_SLAG_INVEST_FILEREPO));
	}

	public Collection<PortfolioPositionDto> loadAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
