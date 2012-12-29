package com.hackhalo2.util.sync;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

import com.hackhalo2.util.IterationEnumerator;

public class JarUtils {
	
	private static HashMap<String, Class<?>> loadedClasses = new HashMap<String, Class<?>>(10000);
	private static HashMap<String, Path> classLocations = new HashMap<String, Path>(10000);
	private static HashMap<String, List<Path>> resources = new HashMap<String, List<Path>>();
	private static StaticURLClassLoaderWrapper parent = new StaticURLClassLoaderWrapper();
	
	private JarUtils() { }
	
	public static Class<?> findClass(String name) throws ClassNotFoundException {
		Class<?> result = null;
		result = loadedClasses.get(name);
		
		if(result != null) return result;
		
		Path p = classLocations.get(name);
		if(p != null) {
			result = findClassInJar(name, p);
			if(result != null) return result;
		}
		
		return parent.findClassWrapper(name);
	}
	
	public static Class<?> findClassInJar(final String name, final Path path) throws ClassNotFoundException {
		byte classByte[];
		Class<?> result;
		JarFile jar = null;
		final String className = (name.replace(".", "/")+".class");
		
		try {
			jar = new JarFile(path.toFile());
			JarEntry entry = jar.getJarEntry(className);
			
			if(entry != null) {
				InputStream is = jar.getInputStream(entry);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int next = is.read();
				
				while(next != -1) {
					baos.write(next);
					next = is.read();
				}
				
				classByte = baos.toByteArray();
				CodeSource source = new CodeSource(path.toUri().toURL(), (CodeSigner[]) null);
				result = parent.defineClassWrapper(name, classByte, 0, classByte.length, source);
				loadedClasses.put(name, result);
				return result;
			}
		} catch (FileNotFoundException e) {
			if (!Thread.currentThread().isInterrupted()) {
				e.printStackTrace();
			}
		} catch (ZipException zipEx) {
			System.out.println("Failed to open " + name + " from " + path.toString());
			zipEx.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				jar.close();
			} catch (IOException ignore) { }
		}
		
		return null;
	}
	
	public static InputStream getResourceAsStream(String resource) {
		Path result = getResource(resource);
		
		if(result != null) {
			try {
				return result.toUri().toURL().openStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return parent.getResourceAsStream(resource);
	}
	
	public static Path getResource(String resource) {
		Enumeration<Path> results;
		try {
			results = getResources(resource);
			while (results.hasMoreElements()) {
				return results.nextElement();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return parent.getResourceWrapper(resource);
		
	}
	
	public static Enumeration<Path> getResources(String resource) throws IOException {
		if(resource != null || resource != "") {
			if(resources.containsKey(resource))
				return new IterationEnumerator<Path>(resources.get(resource).iterator());
			
			if(resource.startsWith("res/") || resource.startsWith("/res/")) {
				String substring = "";
				Path resourceDir = null;
				List<Path> list = new ArrayList<Path>();
				
				if(resource.startsWith("res/")) substring = resource.substring(3);
				else substring = resource.substring(4);
				
				resourceDir = Paths.get(FileUtils.getAssetsDirectoryAsFile().getCanonicalPath()+substring);
				
				if(Files.exists(resourceDir)) {
					list.add(resourceDir);
					resources.put(resource, list);
					return new IterationEnumerator<Path>(list.iterator());
				}
			}
		}
		
		return parent.getResourcesWrapper(resource);
	}
}
