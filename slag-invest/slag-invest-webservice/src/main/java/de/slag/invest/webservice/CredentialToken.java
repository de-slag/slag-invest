package de.slag.invest.webservice;

public class CredentialToken implements Comparable<CredentialToken> {

	private final String tokenString;

	public static CredentialToken of(String tokenString) {
		return new CredentialToken(tokenString);
	}

	private CredentialToken(String tokenString) {
		super();
		this.tokenString = tokenString;
	}

	public String getTokenString() {
		return tokenString;
	}

	@Override
	public int compareTo(CredentialToken o) {
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
		CredentialToken other = (CredentialToken) obj;
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
