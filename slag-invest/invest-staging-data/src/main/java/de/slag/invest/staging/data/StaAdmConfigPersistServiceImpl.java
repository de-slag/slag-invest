package de.slag.invest.staging.data;

import org.springframework.stereotype.Service;

import de.slag.common.data.AbstractPersistService;
import de.slag.invest.staging.model.StaAdmConfig;

//@Service
public class StaAdmConfigPersistServiceImpl extends AbstractPersistService<StaAdmConfig>
		implements StaAdmConfigPersistService {

	@Override
	protected Class<StaAdmConfig> getType() {
		return StaAdmConfig.class;
	}

}
