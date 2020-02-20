package de.slag.invest;

import org.springframework.stereotype.Service;

import de.slag.invest.model.ConfigProperty;
import de.slag.invest.service.AbstractDomainServiceImpl;
import de.slag.invest.service.ConfigPropertyService;

@Service
public class ConfigPropertyServiceImpl extends AbstractDomainServiceImpl<ConfigProperty>
		implements ConfigPropertyService {

	@Override
	protected Class<ConfigProperty> getType() {
		return ConfigProperty.class;
	}

}
