package com.hackhalo2.util.async;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

public class CopyTask implements Runnable {
	
	Path from = null;
	Path to = null;
	
	protected CopyTask(final Path from, final Path to) {
		this.from = from;
		this.to = to;
	}

	@Override
	public void run() {
		final long length = this.from.toFile().length();
		
		try {
			FileInputStream in = new FileInputStream(this.from.toFile());
			FileOutputStream out = new FileOutputStream(this.to.toFile());
			
			FileChannel input = in.getChannel();
			FileChannel output = out.getChannel();
			
			input.transferTo(0, length, output);
			
			input.close();
			output.close();
			
			in.close();
			out.close();
		} catch(IOException e) {
			System.err.println("Unable to copy the File due to the folling Exception: ");
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return "CopyTask[from="+this.from.toString()+", to:"+this.to.toString()+"];";
	}

}
