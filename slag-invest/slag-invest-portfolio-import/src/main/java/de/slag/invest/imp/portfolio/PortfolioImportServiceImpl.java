package de.slag.invest.imp.portfolio;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import de.slag.common.base.AdmCache;
import de.slag.common.base.BaseException;

@Service
public class PortfolioImportServiceImpl implements PortfolioImportService {

	private static final String PORTFOLIO_IMPORT_PATH = "de.slag.invest.imp.portfolio.path";

	@Resource
	private AdmCache admCache;

	@PostConstruct
	public void setUp() {
		admCache.getValue(PORTFOLIO_IMPORT_PATH)
				.orElseThrow((() -> new BaseException("not configured: " + PORTFOLIO_IMPORT_PATH)));
	}

	public Collection<ImpPortfolioDto> fetchData() {
		// TODO Auto-generated method stub
		return null;
	}

}
