package com.hackhalo2.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

public class BatmansBelt {
	
	private FileSystem fs = null;
	private OS os = null;
	private ClassLoader cl = this.getClass().getClassLoader();
	private FileHandler fh = null;
	
	public BatmansBelt() {
		this.fs = FileSystems.getDefault();
		this.fh = new FileHandler(this.fs.provider());
		this.os = OS.getOperatingSystem();
	}
	
	public ClassLoader getClassLoader() {
		return this.cl;
	}
	
	public FileHandler getFileHandler() {
		return this.fh;
	}
	
	public OS getOS() {
		return this.os;
	}
	
	public InputStream getJarResource(String resourceName) {
		return this.getJarResource(resourceName, false);
	}
	
	public InputStream getJarResource(String resourceName, boolean useCache) {
		if(resourceName == null || resourceName == "" || resourceName.equals(null)) {
			throw new IllegalArgumentException("The resource cannot be null!");
		}

		try {
			URL url = this.cl.getResource(resourceName);
			
			if(url == null || url.equals(null)) {
				return null;
			}
			
			URLConnection conn = url.openConnection();
			conn.setUseCaches(useCache);
			
			return conn.getInputStream();
		} catch(IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

}