package tests;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.URL;

public class DownloadFileTest {
	public static void main(String[] args) {
		try {
			URL u = new URL(
					"https://github.com/LucyBean/LucyGame/raw/master/LucyGame.jar");
			BufferedInputStream bis = new BufferedInputStream(u.openStream());
			FileOutputStream fout = new FileOutputStream("test.jar");
			
			final byte data[] = new byte[1024];
	        int count;
	        while ((count = bis.read(data, 0, 1024)) != -1) {
	            fout.write(data, 0, count);
	        }
	        
	        bis.close();
	        fout.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
