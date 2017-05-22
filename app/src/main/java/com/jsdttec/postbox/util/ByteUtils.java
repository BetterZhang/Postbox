package com.jsdttec.postbox.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * @description Byte工具集
 * @author H.CAAHN
 * @createtime 2012-8-5 下午11:58:21
 */
public final class ByteUtils {
    /** 日志对象 */
//    private static final Logger logger = LoggerFactory.getLogger(ByteUtils.class);

    public static byte[] p() {
        return null;
    }
    
    /**
     * 将16进制的字符串转换成16进制的数组，追求最高速度，不兼容空白字符的情况
     * @param str
     * @return
     */
    public static byte[] parse16(String str) {
        // 如果传入的字符串不为空且包含除空白字符外的其他字符，则进行转换，反之则返回null
        if (str != null && str.length() > 1) {
            int count = 0;
            char[] chars = str.toCharArray();
            byte[] bytes = new byte[chars.length / 2];
            for (int i = 0, s = chars.length - 1; i < s;) {
                int a1 = Character.digit(chars[i], 16);
                int a2 = Character.digit(chars[i + 1], 16);
                if (a1 != -1 && a2 != -1) {
                    bytes[count++] = (byte) ((a1 << 4) + a2);
                }
                else {
                    throw new NumberFormatException(str + "包含\"" + (a1 == -1 ? chars[i] : chars[i + 1]) + "\"不能转换为数值");
                }
                
                i += 2;
            }
            return bytes;
        }
        return null;
    }
    
    /**
     * 将16进制的字符串转换成16进制的数组，返回的数组具有指定长度
     * @param str
     * @param length
     * @return
     */
    public static byte[] parse16(String str, int length) {
        // 如果传入的字符串不为空且包含除空白字符外的其他字符，则进行转换，反之则返回null
        if (str != null && str.length() > 1) {
            int count = 0;
            char[] chars = str.toCharArray();
            byte[] bytes = new byte[length > 0 ? length : chars.length / 2];
            for (int i = 0, s = chars.length - 1; i < s;) {
                if (Character.isWhitespace(chars[i])) {
                    i++;
                    continue;
                }
                else if (chars[i] != '0' || (chars[i + 1] != 'x' && chars[i + 1] != 'X')) {
                    int a1 = Character.digit(chars[i], 16);
                    int a2 = Character.digit(chars[i + 1], 16);
                    if (a1 != -1 && a2 != -1) {
                        bytes[count++] = (byte) ((a1 * 16) + a2);
                    }
                    else {
                        throw new NumberFormatException(str + "包含\"" + (a1 == -1 ? chars[i] : chars[i + 1])
                                + "\"不能转换为数值");
                    }
                }
                
                if (length > 0 && count >= length) {
                    break;
                }
                i += 2;
            }
            return Arrays.copyOf(bytes, count);
        }
        
        return null;
    }
    
    /**
     * 将指定的数据转换成byte数组
     * @param number
     * @return
     */
    public static byte[] parse16(long num, int length) {
        byte[] results = new byte[length];
        for (int i = length - 1; i >= 0; i--) {
            results[length - 1 - i] = (byte) (num >>> (i * 8) & 0xff);
        }
        
        return results;
    }
    
    /**
     * 对象序列化，即将Object对象转换成byte数组
     * @param obj
     * @return
     */
    public static byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
        }
        catch (IOException ex) {
//            logger.error("读写异常", ex);
        }
        finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (bos != null) {
                    bos.close();
                }
            }
            catch (Exception ex) {
                
            }
        }
        return bytes;
    }
    
    /**
     * 私有构造方法
     */
    private ByteUtils() {
        
    }
}
