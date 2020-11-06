package de.slag.invest.domain.model.kpi;

public enum KeyPerformanceIndicatorType {
	/**
	 * simple moving average
	 */
	SMA(1);

	private final int parameterCount;

	KeyPerformanceIndicatorType(int parameterCount) {
		this.parameterCount = parameterCount;
	}

	public int getParameterCount() {
		return parameterCount;
	}

}
