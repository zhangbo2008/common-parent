package com.uetty.common.tool.core.string;

public class UnicodeEncoder {

	public static String encode(String str) {
		StringBuilder sb = new StringBuilder();
		char[] charArray = str.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			sb.append(charToUnicodeString(c));
		}
		return sb.toString();
	}
	
	private static String charToUnicodeString(char c) {
		if (c < 0x100) {
			if (c == '\\') {
				return "\\\\";
			} else {
				return c + "";
			}
		}
		String hex = Integer.toHexString(c);
		if (c >= 0x1000) {
			return "\\u" + hex;
		} else {
			return "\\u0" + hex;
		}
	}

	public static void main(String[] args) {
		String str = "34s\u901a\u7528\u6a21\u677f23测试水水水";
		// \u901a\u7528\u6a21\u677f
		System.out.println(str);// java读取的时候\u901a会自动转为'通'字
		
		System.out.println(encode(str));
	}
}
