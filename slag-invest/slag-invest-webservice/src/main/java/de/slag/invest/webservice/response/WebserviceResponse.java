package de.slag.invest.webservice.response;

import java.util.Collection;
import java.util.stream.Collectors;

public interface WebserviceResponse {

	default <T> Collection<T> getBeans(Class<T> type) {
		return getBeans().stream().map(b -> type.cast(b)).collect(Collectors.toList());
	}

	Collection<?> getBeans();

}
