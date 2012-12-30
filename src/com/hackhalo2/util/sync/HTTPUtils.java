package com.hackhalo2.util.sync;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class HTTPUtils {
	
	private HTTPUtils() { }
	
	public static URL sanitizeURL(final URL path) throws IOException, URISyntaxException {
		return path.toURI().toURL();
	}

}
