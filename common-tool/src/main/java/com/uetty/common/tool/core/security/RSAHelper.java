package com.uetty.common.tool.core.security;

public class RSAHelper {

    /**
     * 获取公钥的key
     */
    public static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbweQtKvhTSZuGKaxkLoiGtXSA9b1aIzq2JdQ9sHx9DyQiSL/QYTo3KRj+I6J0F2tVkdHoTmq/9oRDX6TMare1o24gIe/swSBMYbOOeeRw3EmxF63pRtG9TwxFmxp9p65Hzvb0VOsyEeyd3PcBD0OCo4d2tMg16NYmhzROeTxYVQIDAQAB";
    
    /**
     * 获取私钥的key
     */
    public static final String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJvB5C0q+FNJm4YprGQuiIa1dID1vVojOrYl1D2wfH0PJCJIv9BhOjcpGP4jonQXa1WR0ehOar/2hENfpMxqt7WjbiAh7+zBIExhs4555HDcSbEXrelG0b1PDEWbGn2nrkfO9vRU6zIR7J3c9wEPQ4Kjh3a0yDXo1iaHNE55PFhVAgMBAAECgYAsfF5NQD6YmydeVDy2iEvaHa1ev9ELE5DLQVazH9sOZOqvGBQg0gQsiQJoY4kcmyu+zt0i+nKmzSPNkl2hPMGnHyqSRmLGqFnrQDnJH5EIGxEbzUvALU/919BF1tadHaiUwCoM6OlJFeTwcmtJvTpm4vOoKRPzbFdPTPFvFjkMKQJBAMlySDM5/Fy76hbWHVk44QRyeOg0Rlm/pDjr5CUv9CD6bu+jR89fEET1abuEhs0pmJAi/n4YEEZTj+VFf2G++SsCQQDF8B3VpAIhClCXxLkvaVgKe4Tn88a+kVR5JN8UUC7RmaMV4sWeb69Bi5dEmZA3RHJ+6To/jI2u1iFkg7hdpjR/AkB3Ea6tYLvS0Fu4LczhHOab61Gd5rAigkz+PCf49xQ0nfIOgROD9iu2ptxdMyM+hzSfFaAZRf3wo5mtKdv2GZxLAkEAmYWurjiCCjxk3AISHArZ5W9+WyXBvacc3MVTXP8AAUPnsR7tZgB77xuk3Ok6aRNmtraQnh+W+MdOqRZdFg3GYwJBAJFg5cqxqy4DwMfPnkK8Ppf7dRvpkImlAH+woyff2wEZuaO4XdNw+rwYB8hDR2APpmzrL1q02qGhRWMYoVuuguM=";
    
    
    public static String decryptByPrivateKey(String data, String charset)
            throws Exception {
    	byte[] decode = RSAUtils.decryptByPrivateKey(hexString2Bytes(data), privateKey);
    	if(charset != null){
    		return new String(decode, charset);
    	}else{
    		return new String(decode);
    	}
    }
    
    public static String decryptByPublicKey(String data, String charset) throws Exception{
    	byte[] decode = RSAUtils.decryptByPublicKey(hexString2Bytes(data), publicKey);
    	if(charset != null){
    		return new String(decode, charset);
    	}else{
    		return new String(decode);
    	}
    }
    
    public static String encryptByPrivateKey(String data, String charset) throws Exception{
    	byte[] encode;
    	if(charset != null){
    		encode = RSAUtils.encryptByPrivateKey(data.getBytes(charset), privateKey);
    	}else{
    		encode = RSAUtils.encryptByPrivateKey(data.getBytes(), privateKey);
    	}
    	return bytes2HexString(encode);
    }
    
    public static String encryptByPublicKey(String data, String charset) throws Exception{
    	byte[] encode;
    	if(charset != null){
    		encode = RSAUtils.encryptByPublicKey(data.getBytes(charset), publicKey);
    	}else{
    		encode = RSAUtils.encryptByPublicKey(data.getBytes(), publicKey);
    	}
    	return bytes2HexString(encode);
    }
    
    public static byte[] hexString2Bytes(String src) {  
        int l = src.length() / 2;  
        byte[] ret = new byte[l];  
        for (int i = 0; i < l; i++) {  
            ret[i] = (byte) Integer  
                    .valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();  
        }  
        return ret;  
    }  
 
 

	public static String bytes2HexString(byte[] b) {

		String ret = "";

		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex;
		}
		return ret;
	}
}
