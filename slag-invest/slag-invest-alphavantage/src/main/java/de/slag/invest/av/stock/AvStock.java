package de.slag.invest.av.stock;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface AvStock {
	
	String getSymbol();
	
	BigDecimal getOpen();
	
	BigDecimal getHigh();
	
	BigDecimal getLow();
	
	BigDecimal getClose();
	
	long getVolume();
	
	LocalDate getDate();

}
