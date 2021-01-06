package de.slag.invest.one.model;

import java.time.LocalDate;

public interface IsSecurityPointIdentifier extends IsIdentifier<IsSecurityPoint> {

	String getIsinWkn();

	LocalDate getDate();

}
