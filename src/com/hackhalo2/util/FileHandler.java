package com.hackhalo2.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.nio.file.AccessMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;

public class FileHandler {

	private FileSystemProvider fsp = null;
	//private ExecutorService executor = Executors.newCachedThreadPool();

	protected FileHandler(FileSystemProvider fsp) {
		this.fsp = fsp;
	}

	public void copy(URI from, URI to) {
		Path fromPath = Paths.get(from);
		Path toPath = Paths.get(to);
		this.copy(fromPath, toPath);
	}

	public void copy(Path from, Path to) {
		try {
			this.fsp.checkAccess(to, AccessMode.WRITE);
			this.fsp.checkAccess(from, AccessMode.READ);
		} catch(UnsupportedOperationException | IOException e) {
			System.err.println("Unable to copy the File due to the folling Exception: ");
			e.printStackTrace();
			return;
		}

		final long length = from.toFile().length();

		FileChannel input = null;
		FileChannel output = null;

		try {
			input = new FileInputStream(from.toFile()).getChannel();
			output = new FileOutputStream(to.toFile()).getChannel();

			input.transferTo(0, length, output);
		} catch(IOException e) {
			System.err.println("Unable to copy the File due to the folling Exception: ");
			e.printStackTrace();
			return;
		} finally {
			try {
				if(input != null) input.close();
			} catch(Exception ignore) { }

			try {
				if(output != null) output.close();
			} catch(Exception ignore) { }
		}
	}

	public void createSymlink(URI from, URI to) {
		Path fromPath = Paths.get(from);
		Path toPath = Paths.get(to);
		this.createSymlink(fromPath, toPath);
	}

	public void createSymlink(Path from, Path to) {
		try {
			this.fsp.checkAccess(to, AccessMode.WRITE);
			this.fsp.checkAccess(from, AccessMode.READ);

			Files.createSymbolicLink(from, to);
		} catch(UnsupportedOperationException | IOException e) {
			System.err.println("Unable to create a symlink due to the folling Exception: ");
			e.printStackTrace();
			return;
		}
	}

	public void delete(URI file) {
		Path path = Paths.get(file);
		this.delete(path);
	}

	public void delete(Path file) {
		try {
			this.fsp.checkAccess(file, AccessMode.WRITE);
			Files.deleteIfExists(file);
		} catch(UnsupportedOperationException | IOException e) {
			System.err.println("Unable to delete file due to the following Exception: ");
			e.printStackTrace();
			return;
		}
	}
	
	public File createTemporaryFile(URI file) {
		Path path = Paths.get(file);
		return this.createTemporaryFile(path);
	}
	
	public File createTemporaryFile(Path file) {
		Path temp = null;
		try {
			this.fsp.checkAccess(file, AccessMode.WRITE);
			if(Files.notExists(file)) {
				Path dirPath = file.getParent().normalize();
				if(Files.notExists(dirPath)) {
					Files.createTempDirectory(dirPath, "temp");
				}
				
				temp = Files.createTempFile(file, ""+System.nanoTime(), "temp");
			}
		} catch(UnsupportedOperationException | IOException e) {
			System.err.println("Unable to delete file due to the following Exception: ");
			e.printStackTrace();
			return null;
		}
		
		return temp.toFile();
	}

	public File getTemporaryDirectory() {
		return new File(System.getProperty("java.io.tmpdir")+"/"+System.currentTimeMillis()+"/");
	}

	public String getFileExtention(String file) {
		if(file.contains(".")) {
			return file.substring(file.lastIndexOf(".")+1, file.length());
		} else return null;
	}
}