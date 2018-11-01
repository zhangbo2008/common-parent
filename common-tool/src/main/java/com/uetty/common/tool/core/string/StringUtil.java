package com.uetty.common.tool.core.string;

public class StringUtil {

	/**
	 * 下划线命名转驼峰
	 */
	public static String underLineToCamelStyle (String str) {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c != '_') {
				continue;
			}
			str = str.substring(0, i) + str.substring(i + 1);
			if (i < str.length()) {
				str = str.substring(0, i) + str.substring(i, i + 1).toUpperCase()
						+ str.substring(i + 1);
			}
			i--;
		}
		return str;
	}
	
	/**
	 * 驼峰命名转下划线
	 * @param str
	 * @return
	 */
	public static String camelToUnderLineStyle (String str) {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c < 'A' || c > 'Z') {
				continue;
			}
			char u = (char) (c + 32);
			str = str.substring(0, i) + '_' + u + str.substring(i + 1);
			i++;
		}
		return str;
	}
}
