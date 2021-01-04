package de.slag.invest.integration;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.Builder;

import de.slag.basic.model.EntityDto;
import de.slag.common.core.eop.EopEntity;

public class EntityDtoBuilder implements Builder<EntityDto> {

	private static final String KEY_VALUE_PATTERN = "%s=%s";
	private EopEntity eopEntity;

	public EntityDtoBuilder withEopEntity(EopEntity eopEntity) {
		this.eopEntity = eopEntity;
		return this;
	}

	@Override
	public EntityDto build() {
		final EntityDto entityDto = new EntityDto();
		entityDto.setType(eopEntity.getType().getName());

		final Collection<String> attributeKeys = eopEntity.getAttributeKeys();
		final List<String> propertyList = attributeKeys.stream()
				.map(key -> key + "=" + eopEntity.get(key))
				.collect(Collectors.toList());

		propertyList.add(String.format(KEY_VALUE_PATTERN, "type", eopEntity.getType().getName()));
		entityDto.getProperties().addAll(propertyList);
		return entityDto;
	}

}
