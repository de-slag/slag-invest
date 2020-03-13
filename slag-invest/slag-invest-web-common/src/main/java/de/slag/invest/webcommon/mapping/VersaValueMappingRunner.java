package de.slag.invest.webcommon.mapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import de.slag.invest.webcommon.model.CommonDto;

public class VersaValueMappingRunner implements Runnable {

	private final CommonDto from;
	private final Object to;

	private List<String> attributeList = new ArrayList<>();
	private Map<String, Consumer<Object>> consumers = new HashMap<>();
	private Map<String, Supplier<Object>> suppliers = new HashMap<>();

	public VersaValueMappingRunner(CommonDto from, Object to) {
		this.from = from;
		this.to = to;
	}

	public void prepare() {
		determineAttributes();
		createConsumers();
		createSuppliers();
	}

	private void createSuppliers() {
		attributeList.forEach(a -> {
			suppliers.put(a.toUpperCase(), () -> from.getValues().get(a.toUpperCase()));
		});

	}

	private void createConsumers() {
		attributeList.forEach(a -> {
			final Method[] methods = to.getClass().getMethods();
			final Optional<Method> findAny = Arrays.asList(methods).stream()
					.filter(m -> m.getName().equalsIgnoreCase("SET" + a.toUpperCase())).findAny();
			if (findAny.isEmpty()) {
				throw new RuntimeException();
			}
			final Method method = findAny.get();
			consumers.put(a.toUpperCase(), (value) -> {
				try {
					method.invoke(to, value);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			});
		});

	}

	private void determineAttributes() {
		attributeList.addAll(Arrays.asList(to.getClass().getMethods()).stream().filter(Filter.ATTRIBUTE_SETTER_METHODS)
				.map(Mapper.ATTRIBUTE_NAME_FROM_SETTER).collect(Collectors.toList()));
	}

	@Override
	public void run() {
		attributeList.forEach(a -> {
			final Supplier<Object> supplier = suppliers.get(a.toUpperCase());
			final Consumer<Object> consumer = consumers.get(a.toUpperCase());
			final Object value = supplier.get();
			consumer.accept(value);
		});

	}

}
