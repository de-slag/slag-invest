package de.slag.invest.one.portfolio;

import java.util.Optional;

import de.slag.invest.one.IsProvider;
import de.slag.invest.one.model.IsIdentifier;
import de.slag.invest.one.model.IsSecurityPoint;

public interface IsSecurityPointProvider extends IsProvider<IsSecurityPoint> {

	Optional<IsSecurityPoint> apply(IsIdentifier<IsSecurityPoint> identifier);

}
