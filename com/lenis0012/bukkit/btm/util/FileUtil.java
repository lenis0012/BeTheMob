package src.com.lenis0012.bukkit.btm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
	
	public static void copy(InputStream input, File to) throws Exception {
		to.getParentFile().mkdirs();
		if(!to.exists())
			to.createNewFile();
		
		OutputStream output = new FileOutputStream(to);
		byte[] buffer = new byte[1024];
		int size = 0;
		while((size = input.read(buffer)) != -1) {
			output.write(buffer, 0, size);
		}
		
		output.close();
		input.close();
	}
}