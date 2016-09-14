package com.unicorn.coordinate.utils;

import android.util.Base64;

import java.nio.charset.Charset;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    // 加密
    public static String encrypt(String sSrc, String sKey) throws Exception {
//        if (sKey == null) {
//            System.out.print("Key为空null");
//            return null;
//        }
//        // 判断Key是否为16位
//        if (sKey.length() != 16) {
//            System.out.print("Key长度不是16位");
//            return null;
//        }
        byte[] raw = sKey.getBytes();

        byte[] keyBytes = Arrays.copyOf(raw, 16);


        SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        //"算法/模式/补码方式"
        IvParameterSpec iv = new IvParameterSpec("1234567890123456".getBytes());
        //使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());

        return  new String(Base64.decode(encrypted, Base64.DEFAULT), Charset.defaultCharset());
//        return Base64.decode(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    public static String  test() throws Exception {
        String key = "123";
        String content = "AF09YY/LjW78wHa3yyaDEA==";
       return encrypt(content, key);
    }
}
