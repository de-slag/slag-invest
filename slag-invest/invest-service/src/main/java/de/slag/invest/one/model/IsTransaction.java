package de.slag.invest.one.model;

import java.time.LocalDate;

public class IsTransaction {

	private IsTransactionType transactionType;

	private IsSecurity security;

	private LocalDate date;
	
	private int count;

	public IsTransaction(IsTransactionType transactionType, IsSecurity security, LocalDate date, int count) {
		super();
		this.transactionType = transactionType;
		this.security = security;
		this.date = date;
		this.count = count;
	}

	public IsTransactionType getTransactionType() {
		return transactionType;
	}

	public IsSecurity getSecurity() {
		return security;
	}

	public LocalDate getDate() {
		return date;
	}

	public int getCount() {
		return count;
	}

	@Override
	public String toString() {
		return "IsTransaction [transactionType=" + transactionType + ", security=" + security + ", date=" + date
				+ ", count=" + count + "]";
	}

}
