package de.slag.invest.staging.data;

import org.springframework.stereotype.Service;

import de.slag.common.data.AbstractPersistService;
import de.slag.invest.staging.model.StaSecurityPoint;

@Service
public class StaSecurityPointPersistServiceImpl extends AbstractPersistService<StaSecurityPoint>
		implements StaSecurityPointPersistService {

	@Override
	protected Class<StaSecurityPoint> getType() {
		return StaSecurityPoint.class;
	}

}
