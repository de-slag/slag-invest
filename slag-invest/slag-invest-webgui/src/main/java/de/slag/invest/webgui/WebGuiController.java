package de.slag.invest.webgui;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ManagedBean
@SessionScoped
public class WebGuiController {

	private int submit = 0;

	private String url;

	private String username;

	private String password;

	private String content;

	public String getAppName() {
		return "SLAG-Invest WebGUI";
	}

	public String getContent() {
		final List<String> strings = new ArrayList<String>();
		strings.add("URL: " + url);
		strings.add("Username: " + username);
		strings.add("submits: " + submit);
		strings.add("Content: " + content);
		return String.join("\n", strings);
	}

	public String submit() {
		final String token = getToken();
		final Client client = ClientBuilder.newClient();
		final WebTarget target = client.target(url + "status").queryParam("token", token);
		final long start = System.currentTimeMillis();
		content = target.request().get(String.class);
		content += "took (ms): " + (System.currentTimeMillis() - start);
		submit++;
		return null;
	}

	private String getToken() {
		return ClientBuilder.newClient().target(url + "login").queryParam("username", username)
				.queryParam("password", password).request().get(String.class);
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

}
