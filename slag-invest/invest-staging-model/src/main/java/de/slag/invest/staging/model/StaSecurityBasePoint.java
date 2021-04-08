package de.slag.invest.staging.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.slag.common.model.EntityBean;

@Entity
public class StaSecurityBasePoint extends EntityBean {

	@Basic
	private String isinWkn;

	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

	@Basic
	private BigDecimal value;

	@Basic
	private String currency;

	@Enumerated(EnumType.STRING)
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
