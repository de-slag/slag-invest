package de.slag.invest.one.model;

import java.util.ArrayList;
import java.util.List;

public class IsPortfolio {
	
	private final List<IsSecurityPosition> securityPositions = new ArrayList<>();

	public List<IsSecurityPosition> getSecurityPositions() {
		return securityPositions;
	}
	

}
