package de.slag.invest.appservice.dataimport;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import de.slag.invest.filerepo.PortfolioTransactionDto;
import de.slag.invest.filerepo.PortfolioTransactionFileRepo;
import de.slag.invest.model.PortfolioTransaction;
import de.slag.invest.model.PortfolioTransaction.PortfolioTransactionType;
import de.slag.invest.service.PortfolioTransactionService;

@Service
public class PortfolioTransactionImportServiceImpl implements PortfolioTransactionImportService {

	private static final Log LOG = LogFactory.getLog(PortfolioTransactionImportServiceImpl.class);

//
//	@Resource
//	private AdmCache admCache;
	@Resource
	private PortfolioTransactionFileRepo portfolioTransactionFileRepo;

	@Resource
	private PortfolioTransactionService portfolioTransactionService;

	@Override
	public void importTransactions() {
		final Collection<PortfolioTransactionDto> findAll = portfolioTransactionFileRepo.findAll();
		findAll.forEach(dto -> updateOrCreate(dto));

	}

	private void updateOrCreate(PortfolioTransactionDto dto) {
		LOG.info("update or create: " + dto);
		final String portfolioNumber = dto.getPortfolioNumber();
		final PortfolioTransactionType type = typeOf(dto.getType());
		final LocalDate date = dto.getDate();
		final String isin = dto.getIsin();

		final PortfolioTransaction transaction = loadTransactionBy(portfolioNumber, type, date, isin)
				.orElse(new PortfolioTransaction());
		
		transaction.setPortfolioNumber(portfolioNumber);
		transaction.setType(type);
		transaction.setDate(date);
		transaction.setIsin(isin);
		
		transaction.setCount(dto.getCount());
		transaction.setTotalPrice(dto.getTotalPrice());
		
		portfolioTransactionService.save(transaction);
		

	}

	private Optional<PortfolioTransaction> loadTransactionBy(String portfolioNumber, PortfolioTransactionType type,
			LocalDate date, String isin) {
		return Optional.empty();

	}

	private PortfolioTransactionType typeOf(String typeString) {
		return PortfolioTransactionType.valueOf(typeString.toUpperCase());
	}

}
