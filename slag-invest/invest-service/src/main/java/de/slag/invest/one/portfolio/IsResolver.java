package de.slag.invest.one.portfolio;

import java.util.concurrent.Callable;

public interface IsResolver<T> extends Callable<T> {
	
	@Override
	default T call() throws Exception {
		return resolve();
	}
	
	T resolve() throws Exception;

}
