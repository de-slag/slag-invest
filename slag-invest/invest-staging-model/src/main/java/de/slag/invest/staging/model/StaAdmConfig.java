package de.slag.invest.staging.model;

import javax.persistence.Basic;
import javax.persistence.Entity;

import de.slag.common.model.EntityBean;

@Entity
public class StaAdmConfig extends EntityBean {

	@Basic
	private String configKey;

	@Basic
	private String configValue;

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

}
