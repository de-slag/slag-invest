package de.slag.invest.accounting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import de.slag.common.base.BaseException;
import de.slag.invest.facades.PortfolioFacade;
import de.slag.invest.model.PortfolioTransaction;
import de.slag.invest.model.PortfolioTransaction.PortfolioTransactionType;

@Service
public class PortfolioAccountingServiceImpl implements PortfolioAccoutingService {

	public void account(PortfolioFacade portfolioFacade) {
		final List<PortfolioTransaction> transactions = getTransactions(portfolioFacade);
		AccountingNotes accountingNotes = new AccountingNotes();
		AccountingNotesValidator validator = new AccountingNotesValidator();
		transactions.forEach(t -> {
			account(accountingNotes, t);
			if (!validator.isValid(accountingNotes)) {
				throw new BaseException(validator.toString());
			}
		});
		portfolioFacade.getPortfolio().setCash(accountingNotes.getCash());

	}

	private List<PortfolioTransaction> getTransactions(PortfolioFacade portfolioFacade) {
		return new ArrayList<PortfolioTransaction>(portfolioFacade.getTransactions());
	}

	private void account(AccountingNotes notes, PortfolioTransaction transaction) {
		PortfolioTransactionType type = transaction.getType();
		switch (type) {
		case BUY:
			notes.add(transaction.getIsin(), transaction.getCount(), transaction.getTotalPrice());
			break;
		case SELL:
			notes.sub(transaction.getIsin(), transaction.getCount(), transaction.getTotalPrice());
			break;

		case CASH_IN:
		case YIELD:
			notes.add(transaction.getTotalPrice());
			break;
		case CASH_OUT:
		case COST:
			notes.sub(transaction.getTotalPrice());
			break;

		default:
			throw new BaseException("not supported: " + type);

		}
	}

}
