package com.jsdttec.postbox.util;

import android.text.TextUtils;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description String字符串工具集
 * @author H.CAAHN
 * @createtime 2012-8-5 下午11:55:31
 */
public final class Strings {
    public final static char[] digits = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C',
            'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z', '_', '-', '~', '!', '#', '$', '%', '^', '&', '*', '(', ')', '=', '+',
            '/', '{', '}', '[', ']', '|', '\\', ':', ';', '"', '\"', '<', '>', ',', '.', '?'};

    /**
     * 按指定byte长度截断字符串，对于非ASCII格式的字符将不会被截断 仅支持UTF-8格式
     * @param str
     * @param size
     * @return
     */
    public static String substringUTF8(String str, int size) {
        try {
            byte[] bs = null;
            
            if (size <= 0) {
                return "";
            } else if (str == null || (bs = str.getBytes("utf-8")).length <= size) {
                return str;
            }
            
            int index = 0;
            if ((bs[size] >> 7) == 0x0 || ((bs[size] >> 6) & 0x03) == 0x03) {
                index = size;
            }
            else {
                for (int i = size - 1; i >= 0; i--) {
                    if (((bs[i] >> 6) & 0x03) == 0x03) {
                        index = i;
                        break;
                    }
                }
            }
            return new String(Arrays.copyOf(bs, index));
        }
        catch (UnsupportedEncodingException e) {}
        return str;
    }
    
    /**
     * 通过使用指定的 charset 解码指定的 byte 数组，构造一个新的 String
     * @param bytes
     * @param charsetName
     * @return
     */
    public static String newString(byte[] bytes, String charsetName) {
        try {
            return new String(bytes, charsetName);
        }
        catch (UnsupportedEncodingException e) {
            return null;
        }
    }
    
    /**
     * 通过使用指定的字符集解码指定的 byte 子数组，构造一个新的 String
     * @param bytes
     * @param offset
     * @param length
     * @param charsetName
     * @return
     */
    public static String newString(byte[] bytes, int offset, int length, String charsetName) {
        try {
            return new String(bytes, offset, length, charsetName);
        }
        catch (UnsupportedEncodingException e) {
            return null;
        }
    }
    
    /**
     * 将普通的文本字符串转换成16进制字符串
     * @param str
     * @param splitStr
     * @return
     */
    public static String parse16(String str, String splitStr) {
        return parse16(str.getBytes(), null, splitStr, 0);
    }
    
    /**
     * 将byte数组转换成16进制字符串
     * @param data
     * @return
     */
    public static String parse16(byte[] data) {
        return parse16(data, null, null, -1);
    }
    
    /**
     * 将byte数组转换成16进制字符串
     * @param data
     * @param prefix - 前缀
     * @return
     */
    public static String parse16(byte[] data, String prefix) {
        return parse16(data, prefix, null, -1);
    }
    
    /**
     * 将byte数组转换成16进制字符串
     * @param data
     * @param prefix - 前缀
     * @param splitStr - 分隔符
     * @return
     */
    public static String parse16(byte[] data, String prefix, String splitStr) {
        return parse16(data, prefix, splitStr, -1);
    }
    
    /**
     * 将byte数组转换成16进制字符串，如数组{0x78, 0x2A, 0x5C}将转换成字符串"78 2A 5C"
     * @param data
     * @param prefix - 前缀
     * @param splitStr - 分隔符
     * @param lineNum - 一行显示条数
     * @return
     */
    public static String parse16(byte[] data, String prefix, String splitStr, int lineNum) {
        if (data == null || data.length == 0) {
            return null;
        }
        
        StringBuffer buf = new StringBuffer();
        for (int i = 0, n = data.length; i < n; ) {
            byte b = data[i];
            if (splitStr != null && i > 0) {
                buf.append(splitStr);
            }
            
            if (prefix != null) {
                buf.append(prefix);
            }
            
            buf.append(digits[(b >>> 4) & 0x0f]);
            buf.append(digits[b & 0x0f]);
            
            i++;
            if (lineNum > 0 && i % lineNum == 0) {
                buf.append("\r\n");
            }
        }
        
        return buf.toString();
    }
    
    /**
     * 将16进制文本字符串转换成普通字符串
     * @param hex
     * @return
     */
    public static String hex2String(String hex) {
        Pattern pattern = Pattern.compile("^0(x|X)");
        boolean find = pattern.matcher(hex).find();
        if (find) {
            hex = hex.substring(2);
        }
        hex = hex.replaceAll("\\s", "");
        
        char[] chs = hex.toCharArray();
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (char2byte(chs[i * 2]) << 4 | char2byte(chs[i * 2 + 1]));
        }
        return new String(bytes);
    }
    
    /**
     * 将long型转换成16进制表现形式
     * @param num
     * @param pix - 是否带0x前缀
     * @return
     */
    public static String parse16(long num, boolean pix) {
        return parse16(num, 16, pix);
    }
    
    /**
     * 将int型转换成16进制表现形式
     * @param num
     * @param pix - 是否带0x前缀
     * @return
     */
    public static String parse16(int num, boolean pix) {
        return parse16(num, 8, pix);
    }
    
    /**
     * 将short型转换成16进制表现形式
     * @param num
     * @param pix - 是否带0x前缀
     * @return
     */
    public static String parse16(short num, boolean pix) {
        return parse16(num, 4, pix);
    }
    
    /**
     * 将byte型转换成16进制表现形式
     * @param num
     * @param pix - 是否带0x前缀
     * @return
     */
    public static String parse16(byte num, boolean pix) {
        return parse16(num, 2, pix);
    }
    
    /**
     * 将数值转换成指定长度的16进制表现形式
     * @param num
     * @param index
     * @param pix - 是否带0x前缀
     * @return
     */
    public static String parse16(long num, int index, boolean pix) {
        char[] chs;
        if (pix) {
            chs = new char[index + 2];
            chs[0] = '0';
            chs[1] = 'x';
            for (int i = index - 1; i >= 0; i--) {
                int _t = (int) (num >>> (i * 4) & 0x0f);
                chs[index + 1 - i] = digits[_t];
            }
        }
        else {
            chs = new char[index];
            for (int i = index - 1; i >= 0; i--) {
                int _t = (int) (num >>> (i * 4) & 0x0f);
                chs[index - 1 - i] = digits[_t];
            }
        }
        return new String(chs);
    }
    
    /**
     * 删除特殊字符
     * @param str
     * @return
     */
    public static String deleteSpecialString(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        
        Pattern pattern = Pattern.compile("[\"\'<>]");
        Matcher matcher = pattern.matcher(str);
        return matcher.replaceAll("");
    }
    
    /**
     * 过滤特殊字符
     * @param str
     * @return
     */
    public static String replaceSpecialString(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        
        str = str.replaceAll("\"", "“");
        str = str.replaceAll("\'", "‘");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        return str;
    }
    
    public static void main(String[] args) {
        String aa = "a<aaa中(*&^&<%&(*)*)({\"[]\'\"\"{{]]]]'";
        System.out.println(deleteSpecialString(aa));
        System.out.println(replaceSpecialString(aa));
    }
    
    /**
     * 将16进制的char字符转换成byte
     * @param ch
     * @return
     */
    private static byte char2byte(char ch) {
        int result = "0123456789ABCDEF".indexOf(Character.toUpperCase(ch));
        if (result == -1) {
            throw new RuntimeException("不能将字符:  \"" + ch + "\"转换成16进制数");
        }
        
        return (byte) result;
    }
    
    /**
     * 私有构造方法
     */
    private Strings() {
        
    }
}
