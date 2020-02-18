package de.slag.invest.webservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.commons.lang3.builder.Builder;

public class WebTargetBuilder implements Builder<WebTarget> {

	private String url;

	public WebTargetBuilder withUrl(String url) {
		this.url = url;
		return this;
	}

	@Override
	public WebTarget build() {
		if (!url.contains("?")) {
			return buildWithoutParameters();
		}

		final String[] split = url.split("\\?");
		final String endpoint = split[0];
		final String parameter = split[1];

		WebTarget target = ClientBuilder.newClient().target(endpoint);

		List<WebTarget> targets = new ArrayList<>();
		targets.add(0, target);

		final List<String> asList = Arrays.asList(parameter.split("&"));
		asList.forEach(p -> {
			final String[] split2 = p.split("=");
			final String key = split2[0];
			final String value = split2[1];
			targets.add(0, targets.get(0).queryParam(key, value));
		});

		return targets.get(0);
	}

	private WebTarget buildWithoutParameters() {
		return ClientBuilder.newClient().target(url);
	}
}
