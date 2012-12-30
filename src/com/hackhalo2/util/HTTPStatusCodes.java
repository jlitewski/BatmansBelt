package com.hackhalo2.util;

public enum HTTPStatusCodes {
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
	
	private HTTPStatusCodes(String description, boolean error, int statusCode) {
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
}
