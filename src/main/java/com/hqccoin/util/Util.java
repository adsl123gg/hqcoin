package com.hqccoin.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
	
	public static String hash256(String string){
		MessageDigest messageDigest = null;
		String encodeStr = null;
        try {
        	messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(string.getBytes("UTF-8"));
			encodeStr = byte2Hex(messageDigest.digest());
		} catch (UnsupportedEncodingException|NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        return encodeStr;
	}
	
	private static String byte2Hex(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i=0;i<bytes.length;i++){
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1){
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }


}
