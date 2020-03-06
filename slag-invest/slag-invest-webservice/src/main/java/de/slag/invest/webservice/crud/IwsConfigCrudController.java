package de.slag.invest.webservice.crud;

import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.slag.invest.model.ConfigProperty;
import de.slag.invest.service.ConfigPropertyService;
import de.slag.invest.webcommon.model.ConfigDto;
import de.slag.invest.webservice.CredentialToken;
import de.slag.invest.webservice.IwsCredentialComponent;

@RestController
@RequestMapping("/config")
public class IwsConfigCrudController extends AbstractIwsCrudController<ConfigDto> {

	private Function<ConfigProperty, ConfigDto> PROPERTY_TO_DTO = p -> {
		final ConfigDto dto = new ConfigDto();
		dto.setId(p.getId());
		dto.setKey(p.getKey());
		dto.setValue(p.getValue());
		return dto;
	};

	private BiConsumer<ConfigDto, ConfigProperty> DTO_TO_PROPERTY = (d, p) -> {
		p.setValue(d.getValue());
	};

	@Resource
	private ConfigPropertyService configPropertyService;

	@Resource
	private IwsCredentialComponent iwsCredentialComponent;

	@Override
	protected Long create0() {
		final ConfigProperty bean = configPropertyService.create(null);
		configPropertyService.save(bean);
		return bean.getId();
	}

	@Override
	protected ConfigDto load0(long id) {
		final ConfigProperty configProperty = configPropertyService.loadById(id);
		return PROPERTY_TO_DTO.apply(configProperty);
	}

	@Override
	protected void save0(ConfigDto dto) {
		final ConfigProperty property = configPropertyService.loadById(dto.getId());
		DTO_TO_PROPERTY.accept(dto, property);
		configPropertyService.save(property);

	}

	@Override
	protected void delete0(long id) {
		configPropertyService.deleteBy(id);
	}

}
