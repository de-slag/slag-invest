package de.slag.invest.webgui;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;

import org.apache.commons.lang3.StringUtils;

import de.slag.invest.webcommon.WsResponseSupport;

@ManagedBean
@SessionScoped
public class WebGuiController {

	private int submit = 0;

	private String url;

	private String username;

	private String password;

	private String endpoint;

	private List<String> contentList = new ArrayList<>();

	public String getAppName() {
		return "SLAG-Invest WebGUI";
	}

	public String getContent() {
		return String.join("\n", contentList);
	}

	public String submit() {
		contentList.clear();
		if (StringUtils.isBlank(endpoint)) {
			contentList.add("no endpoint setted");
			return null;
		}

		contentList.add("URL: " + url);
		contentList.add("Username: " + username);
		contentList.add("submits: " + submit);

		final long start = System.currentTimeMillis();
		final String token = getToken();
		contentList.add(token != null ? "recieved token" : "NO token recieved");

		final Client client = ClientBuilder.newClient();
		final WebTarget target = client.target(url + endpoint).queryParam("token", token);

		final Builder request = target.request();
		final String value = new WsResponseSupport().getValue(request, String.class);
		contentList.add(value);

		contentList.add("took (ms): " + (System.currentTimeMillis() - start));
		submit++;
		return null;
	}

	private String getToken() {
		final Builder request = ClientBuilder.newClient().target(url + "login").queryParam("username", username)
				.queryParam("password", password).request();

		final String value = new WsResponseSupport().getValue(request, String.class);
		return value;

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

}
