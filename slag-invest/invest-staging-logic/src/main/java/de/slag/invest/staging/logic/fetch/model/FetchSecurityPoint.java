package de.slag.invest.staging.logic.fetch.model;

import java.math.BigDecimal;
import java.util.Date;

import de.slag.invest.staging.model.SecurityPointSource;

public class FetchSecurityPoint {
	
	public static final String ISIN_WKN = "ISIN_WKN";
	
	public static final String TIMESTAMP = "TIMESTAMP";
	
	public static final String VALUE = "VALUE";
	
	public static final String CURRENCY = "CURRENCY";
	
	public static final String SECURITY_POINT_SOURCE = "SECURITY_POINT_SOURCE";
	
	

	private String isinWkn;

	private Date timestamp;

	private BigDecimal value;

	private String currency;

	private SecurityPointSource source;

	public String getIsinWkn() {
		return isinWkn;
	}

	public void setIsinWkn(String isinWkn) {
		this.isinWkn = isinWkn;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public SecurityPointSource getSource() {
		return source;
	}

	public void setSource(SecurityPointSource source) {
		this.source = source;
	}

}
