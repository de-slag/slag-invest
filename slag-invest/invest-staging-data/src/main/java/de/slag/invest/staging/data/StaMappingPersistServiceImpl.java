package de.slag.invest.staging.data;

import de.slag.common.data.AbstractPersistService;
import de.slag.invest.staging.model.StaMapping;

public class StaMappingPersistServiceImpl extends AbstractPersistService<StaMapping>
		implements StaMappingPersistService {

	@Override
	protected Class<StaMapping> getType() {
		return StaMapping.class;
	}

}
