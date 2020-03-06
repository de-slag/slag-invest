package de.slag.invest.webcommon;

public class WsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WsException() {
		this("something went wrong");
	}

	public WsException(int httpStatus) {
		this("something went wrong, http status: " + httpStatus);
	}

	public WsException(String s, int httpStatus) {
		this("http status: " + httpStatus + ", " + s);
	}

	public WsException(String s) {
		super(s);
	}

}
