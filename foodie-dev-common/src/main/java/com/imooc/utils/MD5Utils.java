package com.imooc.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class MD5Utils {

    /**
     * @Title: MD5Utils.java
     * @Package com.imooc.utils
     * @Description: 对字符串进行md5加密
     */
    public static String getMD5Str(String strValue) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            String newstr = Base64.encodeBase64String(md5.digest(strValue.getBytes()));
            return newstr;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            log.error("字符串转MD5出错：{}", strValue);
        }
        return "";
    }

    public static void main(String[] args) {
        try {
            String md5 = getMD5Str("imooc");
            System.out.println(md5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
