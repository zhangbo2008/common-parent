package com.uetty.common.tool.core.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	/**
	 * MD5加密
	 * @author Vincent_Field{e-mail:vincent_field@foxmail.com}
	 * @throws NoSuchAlgorithmException 
	 */
	public static String encode(String str) throws NoSuchAlgorithmException{
		MessageDigest instance = null;

		// 获取MD5算法对象
		instance = MessageDigest
				.getInstance("MD5");
		byte[] digest = instance.digest(str.getBytes());// 对字符串加密，返回字符数组

		StringBuffer sb = new StringBuffer();

		for (byte b : digest) {
			int j = b & 0xff;// 获取字节的低八位有效值
			String hexString = Integer.toHexString(j);// 十进制转16进制
			if (hexString.length() < 2) {// 每个字节两位字符
				hexString = "0" + hexString;
			}
			sb.append(hexString);
		}

		return sb.toString();
	}

	public static void main(String[] args) {
		try {
			System.out.println(encode("gwefsfwe"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}
