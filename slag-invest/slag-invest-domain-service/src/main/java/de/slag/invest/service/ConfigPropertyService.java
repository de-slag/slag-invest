package de.slag.invest.service;

import java.util.Optional;

import de.slag.invest.model.ConfigProperty;
import de.slag.invest.model.Mandant;

public interface ConfigPropertyService extends DomainService<ConfigProperty> {
	
	Optional<ConfigProperty> loadBy(String key, Mandant mandant);

}
