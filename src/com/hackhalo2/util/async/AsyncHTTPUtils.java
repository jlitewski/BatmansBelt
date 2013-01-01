package com.hackhalo2.util.async;

import java.net.URL;
import java.nio.file.Path;

import com.hackhalo2.util.sync.FileUtils;

public class AsyncHTTPUtils {
	
	private AsyncHTTPUtils() { }
	
	public static void getFile(URL path, String fileName) {
		Path outputDirectory = FileUtils.getTemporaryDirectory().toPath();
		getAndSaveFile(path, outputDirectory, fileName);
	}
	
	public static void getAndSaveFile(URL path, Path outputDirectory, String fileName) {
		
	}

}
