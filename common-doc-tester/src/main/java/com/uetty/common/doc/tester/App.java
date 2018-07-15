package com.uetty.common.doc.tester;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * Hello world!
 *
 */
public class App {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException, URISyntaxException {
		
		URL url = new URL("jar:file:/home/vince/.m2/repository/com/uetty/common/common-tool/0.0.1-SNAPSHOT/common-tool-0.0.1-SNAPSHOT.jar!/pdf/fonts/");
//		System.out.println(url.toURI());
//		File f = new File(url.toURI());
//		
//		InputStream openStream = url.openStream();
//		System.out.println(openStream);
		
		JarFile jarFile = new JarFile("/home/vince/.m2/repository/com/uetty/common/common-tool/0.0.1-SNAPSHOT/common-tool-0.0.1-SNAPSHOT.jar");
		
		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry jarEntry = entries.nextElement();
			System.out.println(jarEntry.toString());
			if (jarEntry.toString().startsWith("pdf/fonts/")) {
				String path = jarEntry.toString();
				System.out.println("--------");
			}
		}
		
	}
}
