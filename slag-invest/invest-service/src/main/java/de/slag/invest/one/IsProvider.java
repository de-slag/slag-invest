package de.slag.invest.one;

import java.util.Optional;
import java.util.function.Function;

public interface IsProvider<I, T> extends Function<I, Optional<T>> {
	
	@Override
	default Optional<T> apply(I identifier) {
		return provide(identifier);
	}

	Optional<T> provide(I identifier);

}
