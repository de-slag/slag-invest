package de.slag.invest.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import de.slag.basic.backend.api.BasicBackendService;
import de.slag.basic.model.ConfigProperty;
import de.slag.basic.model.EntityDto;
import de.slag.basic.model.Token;

@Service
public class InvBackendServiceImpl implements BasicBackendService {

	private static final Log LOG = LogFactory.getLog(InvBackendServiceImpl.class);
	
	private static final String MEMBER = "Member";
	private static final String SPECIES = "Species";
	private static final String FAMILY = "Family";
	private static final List<String> TYPES = Arrays.asList(FAMILY, SPECIES, MEMBER);

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
	public BackendState putConfigProperty(String token, ConfigProperty configProperty) {
		return BackendState.OK;
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
		final EntityDto entityDto = new EntityDto();
		final ArrayList<String> properties = entityDto.getProperties();
		entityDto.setType(type);
		entityDto.setId(id);

		switch (type) {
		case FAMILY:
			properties.add(String.format("name=%s", id % 2 == 0 ? "bird" : "mammalian"));
			break;
		case SPECIES:
			properties.add(String.format("name=%", id % 3 == 0 ? "A" : "B"));
		case MEMBER:
			properties.add(String.format("name=%-%", "Pit", id));
		default:
			return BasicBackendService.super.getEntity(type, id);
		}
		return entityDto;

	}
}
