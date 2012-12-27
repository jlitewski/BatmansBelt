package com.hackhalo2.util.async;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolUtils {
	
	private static ThreadPoolExecutor pool = ((ThreadPoolExecutor)Executors.newCachedThreadPool());
	
	private ThreadPoolUtils() { }
	
	public static ThreadPoolExecutor getThreadPool() {
		return pool;
	}

}
