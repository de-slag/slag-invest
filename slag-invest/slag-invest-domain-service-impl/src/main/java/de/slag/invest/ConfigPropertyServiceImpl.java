package de.slag.invest;

import java.util.Optional;

import org.springframework.stereotype.Service;

import de.slag.invest.model.ConfigProperty;
import de.slag.invest.model.Mandant;
import de.slag.invest.service.AbstractDomainServiceImpl;
import de.slag.invest.service.ConfigPropertyService;

@Service
public class ConfigPropertyServiceImpl extends AbstractDomainServiceImpl<ConfigProperty>
		implements ConfigPropertyService {

	@Override
	protected Class<ConfigProperty> getType() {
		return ConfigProperty.class;
	}

	@Override
	public Optional<ConfigProperty> loadBy(String key, Mandant mandant) {
		return loadBy(c -> c.getKey().equals(key) && c.getMandant().equals(mandant));
	}

}
