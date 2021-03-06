package de.slag.invest.webcommon.model;

import java.util.HashMap;
import java.util.Map;

public class CommonDto {

	private Long id;

	private DtoType type;

	private Map<String, Object> values = new HashMap<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DtoType getType() {
		return type;
	}

	public void setType(DtoType type) {
		this.type = type;
	}

	public Map<String, Object> getValues() {
		return values;
	}

	public void setValues(Map<String, Object> values) {
		this.values = values;
	}

}
