package de.slag.invest.one;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.slag.common.util.ResourceUtils;
import de.slag.invest.one.model.IsSecurity;
import de.slag.invest.one.portfolio.IsSecurityProvider;

public class TestIsSecurityProvider implements IsSecurityProvider {

	private List<IsSecurity> securities = new ArrayList<>();

	public static TestIsSecurityProvider build() throws Exception {
		final File securitiesFolder = ResourceUtils.getFileFromResources("securities");
		final List<IsSecurity> securityList = Arrays.asList(securitiesFolder.listFiles())
				.stream()
				.filter(f -> f.isFile())
				.filter(f -> f.getName().endsWith(".csv"))
				.map(file -> {
					final String[] split = file.getName().split("-");
					String wknIsin = split[0];
					String description = split[1];
					return new IsSecurity(wknIsin, description);
				})
				.collect(Collectors.toList());
		return new TestIsSecurityProvider(securityList);
	}

	@Override
	public Optional<IsSecurity> provide(String isinWkn) {
		return securities.stream().filter(s -> isinWkn.equals(s.getWknIsin())).findFirst();
	}

	private TestIsSecurityProvider(List<IsSecurity> securities) {
		super();
		this.securities.addAll(securities);
	}

}
