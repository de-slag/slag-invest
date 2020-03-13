package de.slag.invest.webcommon.mapping;

import java.lang.reflect.Method;
import java.util.function.Function;

public interface Mapper {

	Function<Method, String> ATTRIBUTE_NAME_FROM_SETTER = m -> {
		return m.getName().substring(3).toUpperCase();
	};

	Function<Method, String> ATTRIBUTE_NAME_FROM_GETTER = m -> {
		final String name = m.getName();
		if (m.getName().startsWith("is")) {
			return name.substring(2).toUpperCase();
		}
		return name.substring(3).toUpperCase();
	};

}
