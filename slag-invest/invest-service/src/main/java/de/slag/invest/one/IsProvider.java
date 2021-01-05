package de.slag.invest.one;

import java.util.Optional;
import java.util.function.Function;

public interface IsProvider<T> extends Function<String, Optional<T>> {

}
