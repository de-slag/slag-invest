package de.slag.invest.webservice;

import java.util.Arrays;
import java.util.Collection;

public class HtmlDecorator {

	public String decorate(Object... objects) {
		return decorateList(Arrays.asList(objects));
	}

	public String h1(String s) {
		return "<h1>" + s + "</h1>";
	}

	public String a(String s) {
		return "<a href=" + s + ">" + s + "</a>";
	}

	public String decorateList(Collection<? extends Object> asList) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<head></head>");
		sb.append("<body>");
		sb.append("<table>");

		asList.forEach(obj -> {
			sb.append("<tr><td>");
			sb.append(obj.toString());
			sb.append("</td></tr>");
		});

		sb.append("</table>");
		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}

}
