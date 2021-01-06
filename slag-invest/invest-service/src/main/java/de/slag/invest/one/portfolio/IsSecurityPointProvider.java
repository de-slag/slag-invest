package de.slag.invest.one.portfolio;

import java.util.Optional;

import de.slag.invest.one.IsProvider;
import de.slag.invest.one.model.IsIdentifier;
import de.slag.invest.one.model.IsSecurityPoint;

public interface IsSecurityPointProvider extends IsProvider<IsSecurityPoint> {

	default Optional<IsSecurityPoint> apply(Object arg0) {
		return apply0((IsIdentifier<IsSecurityPoint>)arg0);
	}

	Optional<IsSecurityPoint> apply0(IsIdentifier<IsSecurityPoint> identifier);

}
