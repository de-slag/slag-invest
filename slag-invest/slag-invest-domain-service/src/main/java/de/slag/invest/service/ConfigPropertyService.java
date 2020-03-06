package de.slag.invest.service;

import java.util.Optional;

import de.slag.invest.model.ConfigProperty;
import de.slag.invest.model.Mandant;

public interface ConfigPropertyService extends MandantBeanService<ConfigProperty> {
	
	Optional<ConfigProperty> loadBy(String key, Mandant mandant);

}
