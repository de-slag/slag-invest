package de.slag.invest.webservice.response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleHtmlResponse {
	
	private String title;
	
	private String text;
	
	private Collection<String> table  = new ArrayList<>();

	public SimpleHtmlResponse(String title, String text, Collection<String> table) {
		super();
		this.title = title;
		this.text = text;
		this.table = table;
	}
	
	@Override
	public String toString() {
		final HtmlDecorator decorator = new HtmlDecorator();
		List<String>responseList = new ArrayList<>();
		responseList.add(decorator.h1(title));
		responseList.add(text);
		responseList.addAll(table);
		return decorator.decorateList(responseList);
		
	}
}
