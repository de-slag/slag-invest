package de.slag.invest.webcommon.mapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import de.slag.invest.webcommon.model.CommonDto;

public class ValueMappingRunner implements Runnable {

	private Object from;

	private CommonDto to;

	private List<String> propertyNames = new ArrayList<>();

	private Map<String, Consumer<Object>> setters = new HashMap<>();
	private Map<String, Supplier<Object>> getters = new HashMap<>();

	public ValueMappingRunner(Object from, CommonDto to) {
		this.from = from;
		this.to = to;
	}

	public void prepare() {
		propertyNames();
		setters();
		getters();
	}

	private void getters() {
		final List<Method> methods = Arrays.asList(from.getClass().getMethods());
		propertyNames.forEach(p -> {
			final Optional<Method> getMethod = findMethod("get" + p, methods);
			final Optional<Method> isMethod = findMethod("is", methods);

			final Method getterMethod;
			if (getMethod.isPresent()) {
				getterMethod = getMethod.get();
			} else if (isMethod.isPresent()) {
				getterMethod = isMethod.get();
			} else {
				throw new RuntimeException("no getter found for: " + p);
			}
			getters.put(p, () -> {
				final Object value;
				try {
					value = getterMethod.invoke(from);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new RuntimeException(e);
				}
				return value;
			});

		});

	}

	private Optional<Method> findMethod(String string, Collection<Method> methods) {
		return methods.stream().filter(m -> m.getName().toUpperCase().equals(string.toUpperCase())).findAny();

	}

	private void setters() {
		propertyNames.forEach(p -> {
			setters.put(p, (value) -> to.getValues().put(p, value));
		});

	}

	private void propertyNames() {
		final List<Method> asList = Arrays.asList(from.getClass().getMethods());
		final List<String> collect = asList.stream().filter(Filter.ATTRIBUTE_GETTER_METHODS).map(Mapper.ATTRIBUTE_NAME_FROM_GETTER).collect(Collectors.toList());
		
		final List<String> collect2 = asList.stream().map(m -> m.getName())
				.filter(n -> n.startsWith("set")).map(n -> n.substring(3)).map(n -> n.toUpperCase())
				.collect(Collectors.toList());
		propertyNames.addAll(collect);
	}

	@Override
	public void run() {
		propertyNames.forEach(p -> setters.get(p).accept(getters.get(p).get()));

	}

}
