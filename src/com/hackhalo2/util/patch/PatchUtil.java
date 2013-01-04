package com.hackhalo2.util.patch;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Joe Desbonnet, jdesbonnet@gmail.com
 * @maintainer Jacob "HACKhalo2" Litewski, hackhalotwo@gmail.com
 */
public class PatchUtil {
	/**
	 * Equiv of C library memcmp().
	 * 
	 * @param buf1
	 * @param bufoffset1
	 * @param buf2
	 * @param n
	 * @return
	 */
	public final static int memcmp(byte[] buf1, int bufoffset1, byte[] buf2, int bufoffset2) {

		int n = buf1.length - bufoffset1;

		if (n > (buf2.length-bufoffset2)) {
			n = buf2.length-bufoffset2;
		}
		
		for (int i = 0; i < n; i++) {
			if (buf1[i + bufoffset1] != buf2[i + bufoffset2])
				return buf1[i + bufoffset1] < buf2[i + bufoffset2] ? -1 : 1;
		}

		return 0;
	}

	public static final boolean readFromStream(InputStream in, byte[] buf, int offset, int len) throws IOException {

		int totalBytesRead = 0;
		int nbytes;

		while ( totalBytesRead < len) {
			nbytes = in.read(buf,offset+totalBytesRead,len-totalBytesRead);
			if (nbytes < 0) {
				System.err.println ("readFromStream(): returning prematurely. Read " + totalBytesRead + " bytes");
				return false;
			}
			totalBytesRead+=nbytes;
		}

		return true;
	}
}