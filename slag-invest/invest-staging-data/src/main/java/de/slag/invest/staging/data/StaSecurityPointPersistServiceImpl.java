package de.slag.invest.staging.data;

import de.slag.common.data.AbstractPersistService;
import de.slag.invest.staging.model.StaSecurityPoint;

public class StaSecurityPointPersistServiceImpl extends AbstractPersistService<StaSecurityPoint>
		implements StaSecurityPointPersistService {

	@Override
	protected Class<StaSecurityPoint> getType() {
		return StaSecurityPoint.class;
	}

}
