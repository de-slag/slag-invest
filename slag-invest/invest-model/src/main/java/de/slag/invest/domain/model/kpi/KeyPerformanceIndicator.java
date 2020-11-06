package de.slag.invest.domain.model.kpi;

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
public class KeyPerformanceIndicator extends EntityBean {

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Basic
	private String wknIsin;

	@Enumerated(EnumType.STRING)
	private KeyPerformanceIndicatorType type;
	
	@Basic
	private BigDecimal value;
	
	@Basic
	private String parameters;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getWknIsin() {
		return wknIsin;
	}

	public void setWknIsin(String wknIsin) {
		this.wknIsin = wknIsin;
	}

	public KeyPerformanceIndicatorType getType() {
		return type;
	}

	public void setType(KeyPerformanceIndicatorType type) {
		this.type = type;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	
}
