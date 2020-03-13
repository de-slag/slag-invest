package de.slag.invest.webcommon.mapping;

import java.time.LocalDateTime;

public class MappingTestEntity {

	private Long id;

	private String name;

	private String title;

	private Integer number;

	private LocalDateTime timestamp;

	private MappingTestEntityType type;

	public MappingTestEntity(long id) {
		super();
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public MappingTestEntityType getType() {
		return type;
	}

	public void setType(MappingTestEntityType type) {
		this.type = type;
	}

}
