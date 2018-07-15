package com.uetty.common.tool.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.UUID;

import com.uetty.common.tool.constant.Global;

public class FileTool {

	private static final String tmpFileDir = Global.TMP_FILE_DIR.getValue();

	/**
	 * 随机产生临时文件路径
	 * @param extName
	 * @return
	 */
	public static String randomFilePathByExtName(String extName) {
		File directory = new File(tmpFileDir);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		File f = null;
		int randomTimes = 0;
		do {
			if (randomTimes > 200) {
				throw new RuntimeException("after trying 200 times, an unused filename can not be found under the folder[" + tmpFileDir + "]");
			}
			randomTimes++;
			String fileName = tmpFileDir + File.separator + UUID.randomUUID().toString().substring(0, 8)
					+ System.currentTimeMillis() % 1000;
			if (extName != null && !"".equals(extName)) {
				fileName += "." + extName;
			}
			f = new File(fileName);
		} while (f.exists());
		return f.getAbsolutePath();
	}
	
	/**
	 * 输入流的数据输出到输出流
	 */
	public static void writeFromInputStream(OutputStream os, InputStream is) throws IOException {
		int len = -1;
		byte[] buffer = new byte[1024];
		try {
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			os.flush();
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}
	
	public static boolean isAbsolutePath (String path) {
		if (path.startsWith("/")) return true;
		if (isWinOS()) {// windows
			if (path.contains(":") || path.startsWith("\\")) {
				return true;
			}
		} else {// not windows, just unix compatible
			if (path.startsWith("~")) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否windows系统
	 */
	public static boolean isWinOS() {
		boolean isWinOS = false;
		try {
			String osName = System.getProperty("os.name").toLowerCase();
			String sharpOsName = osName.replaceAll("windows", "{windows}")
					.replaceAll("^win([^a-z])", "{windows}$1").replaceAll("([^a-z])win([^a-z])", "$1{windows}$2");
			isWinOS = sharpOsName.contains("{windows}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isWinOS;
	}
	
	public static InputStream openFileInJar(String jarPath, String filePath) throws IOException {
		if (!filePath.startsWith(File.separator)) {
			filePath = File.separator + filePath;
		}
		String urlPath = "jar:file:" + jarPath + "!" + filePath;
		URL url = new URL(urlPath);
		InputStream stream = url.openStream();
		return stream;
	}
}
