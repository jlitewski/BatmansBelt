package com.hackhalo2.util.async;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import com.hackhalo2.util.sync.HTTPUtils;

public class FileGetTask implements Runnable  {
	
	private URL fileURL = null;
	private Path outputDirectory = null;
	
	protected FileGetTask(URL fileURL, Path outputDirectory) {
		try {
			this.fileURL = HTTPUtils.sanitizeURL(fileURL);
			this.outputDirectory = outputDirectory;
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		if(this.fileURL != null) {
			if(!Files.exists(this.outputDirectory)) {
				
			}
			
		} else {
			System.out.println("fileURL was null, aborting...");
		}
	}
}
