package de.slag.invest.imp.portfolio;

import java.util.ArrayList;
import java.util.Collection;

public class ImpPortfolioDto {

	private String number;

	private final Collection<ImpPortfolioPositionDto> positions = new ArrayList<>();

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Collection<ImpPortfolioPositionDto> getPositions() {
		return positions;
	}
	
	
	

}
