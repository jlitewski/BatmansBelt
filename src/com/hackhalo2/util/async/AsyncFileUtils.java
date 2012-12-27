package com.hackhalo2.util.async;

import java.io.IOException;
import java.net.URI;
import java.nio.file.AccessMode;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class AsyncFileUtils {
	
	private static FileSystemProvider fsp = FileSystems.getDefault().provider();
	private static ThreadPoolExecutor pool = ThreadPoolUtils.getThreadPool();
	
	private AsyncFileUtils() { }
	
	public static Future<Boolean> copy(final URI from, final URI to) {
		Path fromPath = Paths.get(from);
		Path toPath = Paths.get(to);
		return copy(fromPath, toPath);
	}

	public static Future<Boolean> copy(final Path from, final Path to) {
		try {
			fsp.checkAccess(to, AccessMode.WRITE);
			fsp.checkAccess(from, AccessMode.READ);
		} catch(UnsupportedOperationException | IOException e) {
			System.err.println("Unable to copy the File due to the folling Exception: ");
			e.printStackTrace();
			return null;
		}

		return pool.submit(new CopyTask(from, to), Boolean.TRUE);
	}
}
