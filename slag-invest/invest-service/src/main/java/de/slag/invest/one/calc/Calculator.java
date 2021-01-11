package de.slag.invest.one.calc;


import java.math.BigDecimal;
import java.util.concurrent.Callable;

public interface Calculator extends Callable<BigDecimal>{
	
	@Override
	default BigDecimal call() throws Exception {
		return calculate();
	}

	BigDecimal calculate();
}
