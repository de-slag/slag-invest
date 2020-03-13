package de.slag.invest.webcommon.mapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

public interface Filter {

	Collection<String> NO_ATTRIBUTES = Arrays.asList("CLASS");

	Predicate<Method> ATTRIBUTE_SETTER_METHODS = m -> {
		if (!m.getReturnType().getName().equals("void")) {
			return false;
		}

		if (m.getParameterCount() != 1) {
			return false;
		}
		return m.getName().startsWith("set");
	};

	Predicate<Method> ATTRIBUTE_GETTER_METHODS = m -> {
		if (m.getReturnType().getName().equals("void")) {
			return false;
		}
		if (m.getParameterCount() > 0) {
			return false;
		}

		final String name = m.getName();
		if (NO_ATTRIBUTES.stream().anyMatch(noAttribute -> {
			if (name.equalsIgnoreCase("GET" + noAttribute)) {
				return true;
			}
			return name.equalsIgnoreCase("IS" + noAttribute);
		})) {
			return false;
		}

		if (name.startsWith("get")) {
			return true;
		}
		return name.startsWith("is");
	};
}
