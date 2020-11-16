package de.slag.invest.one.api;

import de.slag.common.model.EntityBean;

public interface InvOneService {
	
	void runScheduled();
	
	EntityBean loadOrCreate(EntityType type, Long id);
		
	void save(EntityBean bean);

}
