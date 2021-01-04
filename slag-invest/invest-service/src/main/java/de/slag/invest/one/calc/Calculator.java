package de.slag.invest.one.calc;

import java.util.concurrent.Callable;

public interface Calculator<T> extends Callable<T>{
	
	@Override
	default T call() throws Exception {
		return calculate();
	}

	T calculate();
}
