package de.slag.invest.one.calc;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVRecord;

import de.slag.common.base.BaseException;
import de.slag.common.util.CsvUtils;
import de.slag.common.util.DateUtils;
import de.slag.common.util.ResourceUtils;
import de.slag.invest.one.model.IsSecurity;
import de.slag.invest.one.model.IsTransaction;
import de.slag.invest.one.model.IsTransactionType;
import de.slag.invest.one.portfolio.IsSecurityProvider;

public class TransactionLoadingTestSupport {

	private IsSecurityProvider securityProvider;

	private Function<CSVRecord, IsTransaction> recordToTransaction = rec -> {
		final IsTransactionType transactionType = IsTransactionType.valueOf(rec.get("TRANSACTION_TYPE"));
		final String isinWkn = rec.get("ISIN_WKN");
		final Integer count = Integer.valueOf(rec.get("COUNT"));
		LocalDate date;
		try {
			date = DateUtils.toLocalDate(new SimpleDateFormat("yyyy-MM-dd").parse(rec.get("DATE")));
		} catch (ParseException e) {
			throw new BaseException(e);
		}
		final IsSecurity security = securityProvider.apply(isinWkn).get();
		return new IsTransaction(transactionType, security, date, count);
	};

	public TransactionLoadingTestSupport(IsSecurityProvider securityProvider) {
		super();
		this.securityProvider = securityProvider;
	}

	public Collection<IsTransaction> loadTransactions(String portfolioName) throws Exception {
		final File resourceFile = ResourceUtils.getFileFromResources("portfolios/" + portfolioName + ".csv");
		if (!resourceFile.exists()) {
			throw new BaseException("file not found for: " + portfolioName);
		}
		final Collection<CSVRecord> csvRecords = CsvUtils.readRecords(resourceFile.getAbsolutePath());
		return csvRecords.stream().map(recordToTransaction).collect(Collectors.toList());

	}

}
