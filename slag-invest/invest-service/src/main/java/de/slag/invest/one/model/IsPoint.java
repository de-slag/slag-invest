package de.slag.invest.one.model;

import java.time.LocalDate;

import javax.money.MonetaryAmount;

/**
 * A point in time with a monetary value. 
 *
 */
public interface IsPoint {
	
	LocalDate getPointDate();
	
	MonetaryAmount getPointAmount();

}
