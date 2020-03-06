package de.slag.invest.webcommon.model;

import de.slag.invest.Dto;
import de.slag.invest.model.ConfigProperty;

public class ConfigDto implements Dto<ConfigProperty> {

	private Long id;

	private String key;

	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
