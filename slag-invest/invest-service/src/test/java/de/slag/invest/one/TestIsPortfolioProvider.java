package de.slag.invest.one;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.csv.CSVRecord;

import de.slag.common.base.BaseException;
import de.slag.common.util.CsvUtils;
import de.slag.common.util.ResourceUtils;
import de.slag.invest.one.model.IsPortfolio;
import de.slag.invest.one.model.IsSecurity;
import de.slag.invest.one.model.IsTransaction;
import de.slag.invest.one.model.IsTransactionType;
import de.slag.invest.one.portfolio.IsPortfolioProvider;
import de.slag.invest.one.portfolio.IsSecurityProvider;

public class TestIsPortfolioProvider implements IsPortfolioProvider {

	private Map<String, IsPortfolio> map = new HashMap<>();

	public static TestIsPortfolioProvider buildWith(IsSecurityProvider securityProvider) throws Exception {

		final File portfoliosFolder = ResourceUtils.getFileFromResources("portfolios");
		final File[] a = portfoliosFolder.listFiles();
		Map<String, IsPortfolio> mapFromCsv = new HashMap<>();
		Arrays.asList(a).stream().filter(file -> file.getName().endsWith(".csv")).forEach(file -> {
			final String portfolioName = file.getName().split("\\.")[0];
			Collection<IsTransaction> transactions = new ArrayList<>();
			final Collection<CSVRecord> records = CsvUtils.readRecords(file.getAbsolutePath());
			records.forEach(rec -> {
				TestPortfolioCsvRecord portfolioCsvRecord;
				try {
					portfolioCsvRecord = TestPortfolioCsvRecord.buildWith(rec);
				} catch (Exception e) {
					throw new BaseException(e);
				}
				final IsTransactionType transactionType = portfolioCsvRecord.getTransactionType();
				final IsSecurity security = securityProvider.provide(portfolioCsvRecord.getIsinWkn()).get();
				final LocalDate date = portfolioCsvRecord.getDate();
				final int count = portfolioCsvRecord.getCount();
				final IsTransaction isTransaction = new IsTransaction(transactionType, security, date, count);
				transactions.add(isTransaction);
			});

			IsPortfolio portfolio = new IsPortfolio(transactions);
			mapFromCsv.put(portfolioName, portfolio);
		});
		return new TestIsPortfolioProvider(mapFromCsv);
	}

	@Override
	public Optional<IsPortfolio> provide(String name) {
		if (!map.containsKey(name)) {
			return Optional.empty();
		}
		return Optional.of(map.get(name));
	}

	private TestIsPortfolioProvider(Map<String, IsPortfolio> map) {
		super();
		this.map.putAll(map);
	}

}
