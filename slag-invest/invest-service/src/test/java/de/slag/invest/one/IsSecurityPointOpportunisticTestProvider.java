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

public class IsSecurityPointOpportunisticTestProvider implements IsSecurityPointProvider {

	static String alphaNumeric = "abcdefghijklmnopqrstuvwxyz0123456789";

	static List<Character> an = new ArrayList<>();

	static {
		final char[] charArray = alphaNumeric.toCharArray();
		for (char c : charArray) {
			an.add(c);
		}
	}

	public IsSecurity apply(String isinWkn) {

		return new IsSecurity(isinWkn, "synthetic-" + isinWkn);
	}

	@Override
	public Optional<IsSecurityPoint> apply0(IsIdentifier<IsSecurityPoint> id) {

		if (!(id instanceof IsSecurityPointIdentifier)) {
			return Optional.empty();
		}

		IsSecurityPointIdentifier identifier = (IsSecurityPointIdentifier) id;

		final String isinWkn = identifier.getIsinWkn();
		final LocalDate date = identifier.getDate();

		int sum = 0;
		final char[] charArray = isinWkn.toCharArray();
		for (char c : charArray) {
			sum += an.indexOf(c);
		}
		final IsSecurity security = new IsSecurity(isinWkn, "synthetic-" + isinWkn);
		final IsSecurityPoint isSecurityPoint = new IsSecurityPoint(security, CurrencyUtils.newAmount(sum), date);
		return Optional.of(isSecurityPoint);
	}

}
