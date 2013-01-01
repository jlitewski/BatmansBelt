package com.hackhalo2.util.async;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

import com.hackhalo2.util.HTTPStatusCode;
import com.hackhalo2.util.sync.HTTPUtils;

public class FileGetTask implements Runnable  {

	private URL fileURL = null;
	private Path outputDirectory = null;
	private String fileName = null;
	private long downloadSize = -1;

	protected FileGetTask(URL fileURL, Path outputDirectory, String fileName) {
		try {
			this.fileURL = HTTPUtils.sanitizeURL(fileURL);
			this.outputDirectory = outputDirectory;
			this.fileName = fileName;
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		ReadableByteChannel rbc = null;
		FileOutputStream fos = null;
		File outFile = new File(this.outputDirectory.toString()+this.fileName);
		
		if(this.fileURL != null) {
			if(!Files.exists(this.outputDirectory)) {
				try {
					Files.createDirectories(this.outputDirectory);
				} catch (IOException e) {
					System.err.println("Unable to make directories, aborting...");
					e.printStackTrace();
					return;
				}
			}

			try {
				URLConnection connection = this.fileURL.openConnection();
				connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.162 Safari/535.19");
				connection.setDoInput(true);
				connection.setDoOutput(false);
				connection.setUseCaches(false);
				
				HttpURLConnection.setFollowRedirects(true);
				((HttpURLConnection)connection).setInstanceFollowRedirects(true);
				
				HTTPStatusCode returnStatus = HTTPStatusCode.getStatusFromConnection(connection);
				switch(returnStatus) {
				case OK:
					break;
				default:
					System.err.println("The Connection returned: "+returnStatus.getDescription());
					System.err.println("Aborting...");
					return;
				}
				
				InputStream in = connection.getInputStream();
				this.downloadSize = connection.getContentLengthLong();
				if(this.downloadSize < 0) this.downloadSize = Long.MAX_VALUE;
				
				Files.deleteIfExists(outFile.toPath());
				
				rbc = Channels.newChannel(in);
				fos = new FileOutputStream(outFile);
				
				fos.getChannel().transferFrom(rbc, 0, this.downloadSize);
				
				in.close();
				rbc.close();
				
				System.out.println("Download complete!");
				if(this.downloadSize != outFile.length()) {
					System.err.println("The downloaded size of the file differs from the actual file length!");
					System.err.println("Expected download size: "+this.downloadSize);
					System.err.println("Actual file size: "+outFile.length());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if(rbc != null) rbc.close();
				} catch(Exception ignore) { }
				try {
					if(fos != null) fos.close();
				} catch(Exception ignore) { }
			}
		} else {
			System.out.println("fileURL was null, aborting...");
		}
	}
}
