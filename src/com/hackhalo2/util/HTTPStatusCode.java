package com.hackhalo2.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLConnection;

public enum HTTPStatusCode {
	BAD_HTTP_RESPONSE("-1: Bad HTTP Response", true, -1),
	UNKNOWN("0: Unknown", true, 0),
	OK("200: OK", false, 200),
	BAD_REQUEST("400: Bad Request", true, 400),
	FORBIDDEN("403: Forbidden", true, 403),
	NOT_FOUND("404: Not Found", true, 404),
	INTERNAL_SERVER_ERROR("500: Internal Server Error", true, 500),
	BAD_GATEWAY("502: Bad Gateway", true, 502),
	UNAVAILABLE("503: Service Unavailable", true, 503);
	
	private String description;
	private boolean isError;
	private int statusCode;
	
	private HTTPStatusCode(String description, boolean error, int statusCode) {
		this.description = description;
		this.isError = error;
		this.statusCode = statusCode;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public boolean hasError() {
		return this.isError;
	}
	
	public int getStatus() {
		return this.statusCode;
	}
	
	public static HTTPStatusCode getStatusFromConnection(HttpURLConnection connection) throws IOException {
		final int status = connection.getResponseCode();
		for(HTTPStatusCode current : HTTPStatusCode.values()) {
			if(current.getStatus() == status) {
				return current;
			}
		}
		
		return HTTPStatusCode.UNKNOWN;
	}
	
	public static HTTPStatusCode getStatusFromConnection(URLConnection connection) throws IOException {
		return getStatusFromConnection(((HttpURLConnection)connection));
	}
}
