package de.slag.invest.webcommon;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Invocation.Builder;

public class WsResponseSupport {

	private Map<Class<?>, Class<? extends WsResponse<?>>> map = new HashMap<>();

	public WsResponseSupport() {
		map.put(String.class, WsStringResponse.class);	
	}

	public <T> T getValue(Builder request, Class<T> responseType) {
		final Class<? extends WsResponse<?>> class1 = map.get(responseType);
		final WsResponse<?> wsResponse = request.get(class1);
		final Method getValueMethod = getGetValueMethod(wsResponse);
		final Object invoke = invoke(wsResponse, getValueMethod);

		return responseType.cast(invoke);
	}

	private Object invoke(final WsResponse<?> wsResponse, final Method getValueMethod){
		try {
			return getValueMethod.invoke(wsResponse);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	private Method getGetValueMethod(final WsResponse<?> wsResponse) {
		try {
			return wsResponse.getClass().getMethod("getValue");
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

}
