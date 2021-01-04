package de.slag.invest.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import de.slag.basic.backend.api.BasicBackendService;
import de.slag.basic.backend.api.BasicBackendServiceReturnValue;
import de.slag.basic.model.ConfigProperty;
import de.slag.basic.model.EntityDto;
import de.slag.basic.model.Token;
import de.slag.common.core.flattener.Flattener;
import de.slag.common.core.flattener.UnFlattener;
import de.slag.common.model.EntityBean;
import de.slag.invest.one.api.EntityType;
import de.slag.invest.one.api.InvOneService;

@Service
public class InvBackendServiceImpl implements BasicBackendService {

	private static final Log LOG = LogFactory.getLog(InvBackendServiceImpl.class);

	private static final String MEMBER = "Member";
	private static final String SPECIES = "Species";
	private static final String FAMILY = "Family";
	private static final List<String> TYPES = Arrays.asList(FAMILY, SPECIES, MEMBER);

	@Resource
	private InvOneService invOneService;

	@PostConstruct
	public void init() {
		LOG.info("initialized");
	}

	@Override
	public Token getLogin(String username, String password) {
		Token token = new Token();
		token.setTokenString(String.valueOf(System.currentTimeMillis()));
		return token;
	}

	@Override
	public BasicBackendServiceReturnValue putConfigProperty(String token, ConfigProperty configProperty) {
		return BasicBackendServiceReturnValue.of("ok");
	}

	@Override
	public String runDefault(String token) {
		return this.getClass().getName() + ", OK!";
	}

	@Override
	public Collection<String> getDataTypes() {
		return TYPES;
	}

	@Override
	public EntityDto getEntity(String type, Long id) {
		final EntityType typeA = EntityType.valueOf(type.toUpperCase());
		final EntityBean bean = invOneService.loadOrCreate(typeA, id);

		final EntityDto domainEntity = new EntityDto();
		domainEntity.setType(type);
		domainEntity.setId(id);
		domainEntity.setProperties(new ArrayList<String>());

		Map<String, String> attributeValues = new HashMap<>();
		new Flattener().accept(bean, attributeValues);
		attributeValues.keySet().forEach(key -> {
			String value = attributeValues.get(key);
			domainEntity.getProperties().add(key + "=" + value);
		});

		return domainEntity;

	}

	@Override
	public BasicBackendServiceReturnValue save(EntityDto entityDto) {
		LOG.info(String.format("save entity type '%s' with id: '%s'... ", entityDto.getType(), entityDto.getId()));
		final EntityType type = EntityType.valueOf(entityDto.getType().toUpperCase());
		final EntityBean domainBean = invOneService.loadOrCreate(type, entityDto.getId());
		final ArrayList<String> properties = entityDto.getProperties();

		final Map<String, String> attributeValues = keyValueMapOf(properties);

		new UnFlattener().accept(attributeValues, domainBean);
		invOneService.save(domainBean);
		final String msg = String.format("saved: '%s' with id '%s'", type, domainBean.getId());
		LOG.info(msg);
		return BasicBackendServiceReturnValue.of(msg);
	}

	private Map<String, String> keyValueMapOf(final ArrayList<String> properties) {
		final Map<String, String> attributeValues = new HashMap<>();
		properties.forEach(property -> {
			final String[] split = property.split("=");
			final String key = split[0];
			final String value = split[1];
			attributeValues.put(key.toUpperCase(), value);
		});
		return attributeValues;
	}
}
