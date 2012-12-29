package com.hackhalo2.util.async;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtils {
	
	private static ThreadPoolExecutor pool = ((ThreadPoolExecutor)Executors.newCachedThreadPool());
	
	private ThreadPoolUtils() { }
	
	public static ThreadPoolExecutor getThreadPool() {
		return pool;
	}
	
	public static int getPoolSize() {
		return pool.getPoolSize();
	}
	
	public static void prestartThreads() {
		int threads = pool.prestartAllCoreThreads();
		System.out.println("Started "+threads+" threads on preload");
	}
	
	public static int getWorkingThreads() {
		return pool.getActiveCount();
	}
	
	public static void shutdown() {
		try {
			pool.awaitTermination(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.err.println("Shutdown was interrupted, forcing shutdown...");
			pool.shutdownNow();
			e.printStackTrace();
		}
	}

}
