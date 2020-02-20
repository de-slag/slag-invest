package de.slag.invest.model;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class User extends MandantBean {

	public User(Mandant mandant) {
		super(mandant);
	}

	User() {
		this(null);
	}

	@Column(unique = true)
	@Basic
	private String username;

	@Basic
	private String passwordHash;
	
	@Transient
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public PasswordHash getPasswordHash() {
		return new PasswordHash(passwordHash);
	}

	public void setPasswordHash(PasswordHash passwordHash) {
		this.passwordHash = passwordHash.getHash();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
