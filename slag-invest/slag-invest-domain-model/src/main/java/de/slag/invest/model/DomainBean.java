package de.slag.invest.model;

import java.util.UUID;

import javax.persistence.Transient;

import de.slag.common.model.EntityBean;


public abstract class DomainBean extends EntityBean {

	@Transient
	private UUID uuid = UUID.randomUUID();

	public UUID getUuid() {
		return uuid;
	}

}
