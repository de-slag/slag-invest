package de.slag.invest.webcommon;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ResponseSupport {

	private static final Log LOG = LogFactory.getLog(ResponseSupport.class);

	private final Response response;

	public ResponseSupport(Response response) {
		super();
		this.response = response;
	}

	public String getStringEntity() {
		return readEntity(String.class);
	}

	public Long getLongEntity() {
		return readEntity(Long.class);
	}

	private <T> T readEntity(Class<T> type) {
		validate();
		return response.readEntity(type);
	}

	private void validate() {
		if (response.getStatusInfo() == Status.OK) {
			return;
		}
		handleError();
	}

	private void handleError() {
		final int status = response.getStatus();
		if (IwsStatus.NOT_SUCCESSFUL != status) {
			throw new WsException(status);
		}
		try {
			throw response.readEntity(WsException.class);
		} catch (ProcessingException e) {
			LOG.trace(e);
		}

		String message = null;
		try {
			message = response.readEntity(String.class);
		} catch (ProcessingException e) {
			LOG.trace(e);
		}
		if (StringUtils.isEmpty(message)) {
			throw new WsException(status);
		}
		throw new WsException(message, status);
	}
}
