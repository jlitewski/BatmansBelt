package com.hackhalo2.util.sync;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;

public class JarUtils extends URLClassLoader {
	
	private HashMap<String, Class<?>> loadedClasses = new HashMap<String, Class<?>>(10000);
	private HashSet<String> preloaded = new HashSet<String>();
	private HashMap<String, Path> classLocations = new HashMap<String, Path>(10000);
	
	protected JarUtils(ClassLoader parent) {
		super(new URL[0], parent);
	}
	
	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
		Class<?> result = null;
		result = this.loadedClasses.get(name);
		
		if(result != null) return result;
		
		Path p = this.classLocations.get(name);
		if(p != null) {
			
		}
		
		return result;
	}

}
