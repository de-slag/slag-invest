package de.slag.invest.one;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.slag.common.util.CurrencyUtils;
import de.slag.invest.one.model.IsIdentifier;
import de.slag.invest.one.model.IsSecurity;
import de.slag.invest.one.model.IsSecurityPoint;
import de.slag.invest.one.model.IsSecurityPointIdentifier;
import de.slag.invest.one.portfolio.IsSecurityPointProvider;
import de.slag.invest.one.portfolio.IsSecurityProvider;

public class IsSecurityPointOpportunisticTestProvider implements IsSecurityPointProvider {

	static String alphaNumeric = "abcdefghijklmnopqrstuvwxyz0123456789";

	static List<Character> an = new ArrayList<>();

	static {
		final char[] charArray = alphaNumeric.toCharArray();
		for (char c : charArray) {
			an.add(c);
		}
	}

	private IsSecurityProvider securityProvider;

	public IsSecurityPointOpportunisticTestProvider(IsSecurityProvider securityProvider) {
		super();
		this.securityProvider = securityProvider;
	}

	public IsSecurityPointOpportunisticTestProvider() {
		this(new IsSecurityOpportunisticTestProvider());
	}

	@Override
	public Optional<IsSecurityPoint> provide(IsSecurityPointIdentifier identifier) {
		if (!(identifier instanceof IsSecurityPointIdentifier)) {
			return Optional.empty();
		}
		
		IsSecurityPointIdentifier securityPointIdentifier = (IsSecurityPointIdentifier) identifier;
		
		final String isinWkn = securityPointIdentifier.getIsinWkn();
		final LocalDate date = securityPointIdentifier.getDate();
		
		int sum = 0;
		final char[] charArray = isinWkn.toCharArray();
		for (char c : charArray) {
			sum += an.indexOf(c);
		}
		final IsSecurity security = securityProvider.apply(isinWkn).get();
		final IsSecurityPoint isSecurityPoint = new IsSecurityPoint(security, CurrencyUtils.newAmount(sum), date);
		return Optional.of(isSecurityPoint);
	}

}
