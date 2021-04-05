package de.slag.invest.staging.logic;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import de.slag.common.data.PersistService;
import de.slag.common.logic.AbstractBusinessService;
import de.slag.invest.staging.data.StaAdmConfigPersistService;
import de.slag.invest.staging.model.StaAdmConfig;

@Service
public class StaAdmConfigBusinessServiceImpl extends AbstractBusinessService<StaAdmConfig>
		implements StaAdmConfigBusinessService {

	@Resource
	private StaAdmConfigPersistService staAdmConfigPersistService;

	@Override
	public StaAdmConfig create() {
		return new StaAdmConfig();
	}

	@Override
	protected PersistService<StaAdmConfig> getPersistService() {
		return staAdmConfigPersistService;
	}

	@Override
	public Collection<StaAdmConfig> findBy(String key) {
		return staAdmConfigPersistService.findAllIds().stream().map(id -> staAdmConfigPersistService.loadById(id))
				.filter(o -> o.isPresent()).map(o -> o.get()).filter(e -> e.getConfigKey().startsWith(key))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<StaAdmConfig> loadBy(String key) {
		return pickUnique(() -> findBy(key));
	}
}
