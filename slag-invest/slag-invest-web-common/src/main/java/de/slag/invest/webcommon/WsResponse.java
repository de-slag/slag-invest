package de.slag.invest.webcommon;

import java.util.Date;

public abstract class WsResponse<T> {
	
	private Date createdAt = new Date();

	private String message;
	
	private Exception exception;
	
	private Boolean successful;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public Boolean getSuccessful() {
		return successful;
	}

	public void setSuccessful(Boolean successful) {
		this.successful = successful;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
