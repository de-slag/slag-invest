package de.slag.invest.accounting;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import de.slag.common.base.BaseException;
import de.slag.common.base.NotSupportedException;
import de.slag.common.util.DateUtils;
import de.slag.invest.accounting.model.AccPortfolio;
import de.slag.invest.accounting.model.AccPortfolioStatement;
import de.slag.invest.model.PortfolioTransaction;
import de.slag.invest.service.PortfolioTransactionService;

@Service
public class AccountServiceImpl {

	@Resource
	private PortfolioTransactionService portfolioTransactionService;

	public Collection<AccPortfolioStatement> account(String number, AccountingPeriod period) {
		final AccPortfolio portfolio = createPortfolio(number);
		final LocalDate earliestDate = determineEarliestDate(portfolio.getTransactions());
		final LocalDate beginOfPeriod = determineBegin(period, earliestDate);
		final LocalDate endOfPeriod = determineEnd(period, earliestDate);
		
		throw new NotImplementedException("");

	}

	LocalDate determineEnd(AccountingPeriod period, LocalDate earliestDate) {
		if (AccountingPeriod.QUATERLY == period) {
			return DateUtils.lastDayOfQuater(earliestDate);
		}
		throw new NotSupportedException(period);

	}

	LocalDate determineBegin(AccountingPeriod period, LocalDate date) {
		switch (period) {
		case QUATERLY:
			return DateUtils.firstDayOfQuater(date);
		default:
			throw new NotSupportedException(period);
		}
	}

	LocalDate determineEarliestDate(List<PortfolioTransaction> transactions) {
		return transactions.stream().map(t -> t.getTimestamp()).findFirst()
				.orElseThrow(() -> new BaseException("no earliest date found in:" + transactions)).toLocalDate();
	}

	private AccPortfolio createPortfolio(String number) {
		final List<PortfolioTransaction> transactions = portfolioTransactionService.findAll().stream()
				.filter(t -> number.equals(t.getPortfolioNumber())).collect(Collectors.toList());

		final AccPortfolio accPortfolio = new AccPortfolio(number);
		accPortfolio.getTransactions().addAll(transactions);
		return accPortfolio;
	}

}
