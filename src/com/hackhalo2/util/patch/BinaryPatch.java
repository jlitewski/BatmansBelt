package com.hackhalo2.util.patch;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

/**
 * Java Binary patcher (based on bspatch by Colin Percival)
 * 
 * @author Joe Desbonnet, jdesbonnet@gmail.com
 */
public class BinaryPatch {

	private static final String VERSION = "jbdiff-0.1.1";

	/**
	 * Run JBPatch from the command line. Params: oldfile newfile patchfile.
	 * newfile will be created.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		if (args.length != 3) {
			System.err
					.println("usage example: java -Xmx200m ie.wombat.jbdiff.JBPatch oldfile newfile patchfile");
		}

		File oldFile = new File(args[0]);
		File newFile = new File(args[1]);
		File diffFile = new File(args[2]);

		bspatch(oldFile, new FileOutputStream(newFile), diffFile);
	}

	public static void bspatch(File oldFile, OutputStream newFile, File diffFile) throws IOException {

		int oldpos, newpos;

		DataInputStream diffIn = new DataInputStream(new FileInputStream(diffFile));

		// headerMagic at header offset 0 (length 8 bytes)
		long headerMagic = diffIn.readLong();

		// ctrlBlockLen after gzip compression at heater offset 8 (length 8
		// bytes)
		long ctrlBlockLen = diffIn.readLong();

		// diffBlockLen after gzip compression at header offset 16 (length 8
		// bytes)
		long diffBlockLen = diffIn.readLong();

		// size of new file at header offset 24 (length 8 bytes)
		int newsize = (int) diffIn.readLong();

		FileInputStream in;
		in = new FileInputStream(diffFile);
		in.skip(ctrlBlockLen + 32);
		GZIPInputStream diffBlockIn = new GZIPInputStream(in);

		in = new FileInputStream(diffFile);
		in.skip(diffBlockLen + ctrlBlockLen + 32);
		GZIPInputStream extraBlockIn = new GZIPInputStream(in);

		/*
		 * Read in old file (file to be patched) to oldBuf
		 */
		int oldsize = (int) oldFile.length();
		byte[] oldBuf = new byte[oldsize + 1];
		FileInputStream oldIn = new FileInputStream(oldFile);
		PatchUtil.readFromStream(oldIn, oldBuf, 0, oldsize);
		oldIn.close();

		byte[] newBuf = new byte[newsize + 1];

		oldpos = 0;
		newpos = 0;
		int[] ctrl = new int[3];
		int nbytes;
		while (newpos < newsize) {

			for (int i = 0; i <= 2; i++) {
				ctrl[i] = diffIn.readInt();
			}

			if (newpos + ctrl[0] > newsize) {
				System.err.println("Corrupt patch\n");
				return;
			}

			/*
			 * Read ctrl[0] bytes from diffBlock stream
			 */

			if (!PatchUtil.readFromStream(diffBlockIn, newBuf, newpos, ctrl[0])) {
				System.err.println("error reading from diffIn");
				return;
			}

			for (int i = 0; i < ctrl[0]; i++) {
				if ((oldpos + i >= 0) && (oldpos + i < oldsize)) {
					newBuf[newpos + i] += oldBuf[oldpos + i];
				}
			}

			newpos += ctrl[0];
			oldpos += ctrl[0];

			if (newpos + ctrl[1] > newsize) {
				System.err.println("Corrupt patch");
				return;
			}

			if (!PatchUtil.readFromStream(extraBlockIn, newBuf, newpos, ctrl[1])) {
				System.err.println("error reading from extraIn");
				return;
			}

			newpos += ctrl[1];
			oldpos += ctrl[2];
		}

		// TODO: Check if at end of ctrlIn
		// TODO: Check if at the end of diffIn
		// TODO: Check if at the end of extraIn

		diffBlockIn.close();
		extraBlockIn.close();
		diffIn.close();
		in.close();

		newFile.write(newBuf, 0, newBuf.length - 1);
		newFile.close();
	}
}
