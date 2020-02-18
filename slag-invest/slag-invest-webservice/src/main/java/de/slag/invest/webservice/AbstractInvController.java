package de.slag.invest.webservice;

import javax.annotation.Resource;

public abstract class AbstractInvController {
	
	@Resource
	private InvCredentialsComponent invCredentialsComponent;
	
	protected InvCredentialsComponent getCredentialsComponent() {
		return invCredentialsComponent;
	}
	
	protected void assertValidToken(String token) {
		invCredentialsComponent.useToken(token);
	}
}
