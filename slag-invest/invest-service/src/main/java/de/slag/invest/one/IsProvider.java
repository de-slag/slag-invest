package de.slag.invest.one;

import java.util.Optional;
import java.util.function.Function;

import de.slag.invest.one.model.IsIdentifier;

public interface IsProvider<T> extends Function<IsIdentifier<T>, Optional<T>> {

}
