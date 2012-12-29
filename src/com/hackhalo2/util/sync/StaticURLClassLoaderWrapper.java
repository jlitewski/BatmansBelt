package com.hackhalo2.util.sync;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.hackhalo2.util.IterationEnumerator;

public class StaticURLClassLoaderWrapper extends URLClassLoader {

	protected StaticURLClassLoaderWrapper() {
		super(new URL[0], ClassLoader.getSystemClassLoader());
	}
	
	protected Class<?> findClassWrapper(String name) throws ClassNotFoundException {
		return this.findClass(name);
	}
	
	protected Class<?> defineClassWrapper(String name, byte[] data, int offset, int length, CodeSource source) {
		return this.defineClass(name, data, offset, length, source);
	}
	
	protected InputStream getResourceStreamWrapper(String resource) {
		return this.getResourceAsStream(resource);
	}
	
	protected Path getResourceWrapper(String resource) {
		URL url = this.getResource(resource);
		Path path = null;
		
		try {
			path = Paths.get(url.toURI());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return path;
	}
	
	protected Enumeration<Path> getResourcesWrapper(String resource) throws IOException {
		Enumeration<URL> temp = this.getResources(resource);
		List<Path> list = new ArrayList<Path>();
		
		while(temp.hasMoreElements()) {
			Path path;
			try {
				path = Paths.get(temp.nextElement().toURI());
				list.add(path);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		
		return new IterationEnumerator<Path>(list.iterator());
	}
	
}