package de.slag.invest.webservice.response;

import org.apache.commons.lang3.BooleanUtils;

public class SimpleWebserviceResponse {

	private Boolean fail;

	private String text;

	private Exception exception;

	public SimpleWebserviceResponse(boolean fail, String text) {
		super();
		this.fail = fail;
		this.text = text;
	}

	public SimpleWebserviceResponse(Exception exception) {
		super();
		this.fail = true;
		this.exception = exception;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public boolean isFail() {
		return BooleanUtils.isTrue(fail);
	}

	public void setFail(Boolean fail) {
		this.fail = fail;
	}

}
