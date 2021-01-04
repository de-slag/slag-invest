package de.slag.invest.one.calc;

import java.math.MathContext;
import java.math.RoundingMode;

public interface InvCalcConstants {
	
	MathContext DEFAULT_MATH_CONTEXT = new MathContext(7, RoundingMode.HALF_UP);

}
