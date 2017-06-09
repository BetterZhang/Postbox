package com.jsdttec.postbox.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESCoder {
    
//    private static final Logger log=LoggerFactory.getLogger(AESCoder.class);
    
    /**
     * 加密
     * hexStr和hexKey都须为16进制表示的字符串
     * 加密后返回16进制表示的字符串*/
    public static String ecbEnc(String hexStr, String hexKey){
        String rs=null;
        try {
            byte[] inBytes = HexUtil.hexToBytes(hexStr);            
            byte[] keyBytes = HexUtil.hexToBytes(hexKey);
            SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");// "算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(inBytes);            
            rs=HexUtil.bytesToHex(encrypted);
        } catch (Exception e) {
//            log.error("加密异常",e);
//            log.error("输入参数为hexStr：{},hexKey:{}",hexStr,hexKey);
        }
        return rs;
    }
    
    public static byte[] ecbEnc(byte[] input, byte[] key) {
    	 byte[] encrypted = null;
    	 SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
         Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES/ECB/NoPadding");
			  cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		      encrypted = cipher.doFinal(input);
		} catch (Exception e) {
//			log.error("加密异常",e);
		}
         return encrypted;
    }
    
    public static byte[] ecbDec(byte[] input,byte[] key){
    	byte[] decBytes = null;
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");// "算法/模式/补码方式"
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            decBytes = cipher.doFinal(input);
        } catch (Exception e) {
//            log.error("解密异常",e);
        }
        return decBytes;
    }
    
    /**
     * 解密
     * hexStr和hexKey都须为16进制
     * 加密后返回16进制的字符串*/
    public static String ecbDec(String hexStr,String hexKey){
        String rs=null;
        try {
            byte[] outBytes = HexUtil.hexToBytes(hexStr);
            byte[] keyBytes = HexUtil.hexToBytes(hexKey);
            SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");// "算法/模式/补码方式"
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] decBytes = cipher.doFinal(outBytes);
            rs=HexUtil.bytesToHex(decBytes);
        } catch (Exception e) {
//            log.error("解密异常",e);
//            log.error("输入参数为hexStr：{},hexKey:{}",hexStr,hexKey);
        }
        return rs;
    }
    
    public static void main(String[] args) {
    	String keystr = "32B043A0D66210377B2CA2A0B10DB974";
    	
		byte[] key =  HexUtil.hexToBytes(keystr);
		System.out.println(key.length);
		
//		byte[] plain_text = { (byte) 0x09, (byte) 0x01, (byte) 0x02, (byte) 0x07, (byte) 0x04, (byte) 0x07, (byte) 0x01, (byte) 0x06, (byte) 0x03, (byte) 0x00, (byte) 0x07, (byte) 0x01, (byte) 0x03, (byte) 0x03, (byte) 0x07, (byte) 0x02};
		byte[] plain_text = {0x07, 0x00, 0x09,0x01, 0x06, 0x02, 0x09, 0x05, 0x04, 0x04, 0x06, 0x07, 0x03, 0x04, 0x01, 0x09};
	
		byte[] encoded = ecbEnc(plain_text, key);
		
		System.out.println("plain_text:"+HexUtil.bytesToHex(plain_text));
		System.out.println("encrypted:"+HexUtil.bytesToHex(encoded));
		byte[] decoded = ecbDec(encoded, key);
		System.out.println("decoded:"+HexUtil.bytesToHex(decoded));
		
		String encodedStr = "59248B5CFA28F540229567DC33CA203A";
		byte[] hexToBytes = HexUtil.hexToBytes(encodedStr);
		byte[] decoded2 = ecbDec(hexToBytes, key);
		System.out.println("decoded2:"+HexUtil.bytesToHex(decoded2));

	}
}