package de.slag.invest.webservice;

import java.util.Objects;

public class IwsCredentialToken implements Comparable<IwsCredentialToken> {

	private final String tokenString;

	public static IwsCredentialToken of(String tokenString) {
		return new IwsCredentialToken(tokenString);
	}

	private IwsCredentialToken(String tokenString) {
		super();
		this.tokenString = Objects.requireNonNull(tokenString, "token string is not setted");
	}

	public String getTokenString() {
		return tokenString;
	}

	@Override
	public int compareTo(IwsCredentialToken o) {
		return this.tokenString.compareTo(o.getTokenString());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tokenString == null) ? 0 : tokenString.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IwsCredentialToken other = (IwsCredentialToken) obj;
		if (tokenString == null) {
			if (other.tokenString != null)
				return false;
		} else if (!tokenString.equals(other.tokenString))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return getTokenString();
	}
	
	

}
