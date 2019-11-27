package de.slag.invest.accounting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import de.slag.common.base.BaseException;
import de.slag.invest.model.Portfolio;
import de.slag.invest.model.PortfolioTransaction;
import de.slag.invest.model.PortfolioTransaction.PortfolioTransactionType;

@Service
public class PortfolioAccountingServiceImpl implements PortfolioAccoutingService {

	public void account(Portfolio portfolio) {
		final List<PortfolioTransaction> transactions = new ArrayList<>(portfolio.getTransactions());
		AccountingNotes accountingNotes = new AccountingNotes();
		AccountingNotesValidator validator = new AccountingNotesValidator();
		transactions.forEach(t -> {
			account(accountingNotes, t);
			if (!validator.isValid(accountingNotes)) {
				throw new BaseException(validator.toString());
			}
		});
		portfolio.setCash(accountingNotes.getCash());
		
		
		
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
