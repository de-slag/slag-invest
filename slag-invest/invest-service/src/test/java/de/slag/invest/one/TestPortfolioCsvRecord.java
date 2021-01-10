package de.slag.invest.one;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import org.apache.commons.csv.CSVRecord;

import de.slag.common.util.DateUtils;
import de.slag.invest.one.model.IsTransactionType;

public class TestPortfolioCsvRecord {
	
	private IsTransactionType transactionType;
	
	private String isinWkn;
	
	private Integer count;
	
	private LocalDate date;
	
	public static TestPortfolioCsvRecord buildWith(CSVRecord record) throws Exception {
		final IsTransactionType transactionType = IsTransactionType.valueOf(record.get("TRANSACTION_TYPE"));
		final String isinWkn = record.get("ISIN_WKN");
		final Integer count = Integer.valueOf(record.get("COUNT"));
		final Date parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(record.get("DATE"));
		final LocalDate date = DateUtils.toLocalDate(parsedDate);		
		return new TestPortfolioCsvRecord(transactionType, isinWkn, count, date);
	}

	private TestPortfolioCsvRecord(IsTransactionType transactionType, String isinWkn, Integer count, LocalDate date) {
		super();
		this.transactionType = transactionType;
		this.isinWkn = isinWkn;
		this.count = count;
		this.date = date;
	}

	public IsTransactionType getTransactionType() {
		return transactionType;
	}

	public String getIsinWkn() {
		return isinWkn;
	}

	public Integer getCount() {
		return count;
	}

	public LocalDate getDate() {
		return date;
	}
	
	

}
