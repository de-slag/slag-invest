package de.slag.invest.domain.model.security;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.slag.common.model.EntityBean;

@Entity
public class SecurityPrice extends EntityBean implements Importable {

	@ManyToOne
	private Security security;

	@Temporal(TemporalType.TIMESTAMP)
	private Date retrievedAt;

	@Basic
	private BigDecimal open;

	@Basic
	private BigDecimal close;

	@Basic
	private BigDecimal high;

	@Basic
	private BigDecimal low;

	@Basic
	private Integer volume;

	public Security getSecurity() {
		return security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}

	public Date getRetrievedAt() {
		return retrievedAt;
	}

	public void setRetrievedAt(Date retrievedAt) {
		this.retrievedAt = retrievedAt;
	}

	public BigDecimal getOpen() {
		return open;
	}

	public void setOpen(BigDecimal open) {
		this.open = open;
	}

	public BigDecimal getClose() {
		return close;
	}

	public void setClose(BigDecimal close) {
		this.close = close;
	}

	public BigDecimal getHigh() {
		return high;
	}

	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	public BigDecimal getLow() {
		return low;
	}

	public void setLow(BigDecimal low) {
		this.low = low;
	}

	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}

}
