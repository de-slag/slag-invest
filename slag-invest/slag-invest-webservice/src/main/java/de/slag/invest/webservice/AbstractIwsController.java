package de.slag.invest.webservice;

import javax.annotation.Resource;

public abstract class AbstractIwsController {
	
	@Resource
	private IwsCredentialComponent invCredentialsComponent;
	
	protected IwsCredentialComponent getCredentialsComponent() {
		return invCredentialsComponent;
	}
	
	protected void assertValidToken(String token) {
		invCredentialsComponent.useToken(token);
	}
}
