package de.slag.invest.one.portfolio;

import java.util.Optional;

import de.slag.invest.one.IsProvider;
import de.slag.invest.one.model.IsSecurity;

public interface IsSecurityProvider extends IsProvider<IsSecurity> {
	
	@Override
	default Optional<IsSecurity> apply(Object arg0) {
		return apply0((String)arg0);
	}
	
	Optional<IsSecurity> apply0(String isinWkn);
	

}
