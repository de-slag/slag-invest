package de.slag.invest;

import java.util.Date;
import java.util.function.Predicate;

import de.slag.invest.model.DomainBean;

public class DomainBeanValidFilter implements Predicate<DomainBean>{

	@Override
	public boolean test(DomainBean t) {
		return t.getValidUntil().after(new Date());
	}

}
