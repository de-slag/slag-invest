package de.slag.invest.staging.logic;

import java.util.Collection;
import java.util.Optional;

import de.slag.common.logic.BusinessService;
import de.slag.invest.staging.model.StaAdmConfig;

public interface StaAdmConfigBusinessService extends BusinessService<StaAdmConfig> {

	Collection<StaAdmConfig> findBy(String key);

	Optional<StaAdmConfig> loadBy(String key);

}
