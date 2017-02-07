package com.unicorn.coordinate.utils;


import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class AESUtils {

//    public static String aesEncrypt(final String plaint, final String password, final String iv) throws Exception {
//
//        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//
//        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
//
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
//        cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(password), ivParameterSpec);
//        return Base64.encodeBase64String(cipher.doFinal(plaint.getBytes("utf-8")));
//    }

    public static String aesDecrypt(final String encryptStr, final String password, final String iv) throws Exception {

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");

        cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(password), ivParameterSpec);
//        byte[] encryptBytes = Base64.decodeBase64(encryptStr);


        byte[] encryptBytes = android.util.Base64.decode(encryptStr, android.util.Base64.DEFAULT);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);

        return new String(decryptBytes);
    }

//    private static byte[] decodeBase64(String dataToDecode) {
//        byte[] dataDecoded = Base64.decode(dataToDecode, Base64.DEFAULT);
//        return dataDecoded;
//    }

    public static String decrypt(final String encryptStr) {
        try {
            return aesDecrypt(encryptStr, "123", "1234567890123456");
        } catch (Exception e) {
            return "";
        }
    }

    public static String decrypt2(final String encryptStr) {
        try {
            return aesDecrypt(encryptStr, "124", "1234567890123456");
        } catch (Exception e) {
            return "";
        }
    }

    private static SecretKeySpec getSecretKeySpec(final String password) throws UnsupportedEncodingException {

        int keyLength = 128;
        byte[] keyBytes = new byte[keyLength / 8];
        Arrays.fill(keyBytes, (byte) 0x0);

        byte[] passwordBytes = password.getBytes("UTF-8");
        int length = passwordBytes.length < keyBytes.length ? passwordBytes.length : keyBytes.length;
        System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        return secretKeySpec;
    }
//
//    public static void main(String[] args) throws Exception {

//        String s = aesEncrypt("123", "1", "1234567890123456");
//        System.out.println(s);

//      String  s = aesDecrypt("P7wTbz62wE0Vf09M3Gpahw==", "123", "1234567890123456");
//        System.out.println(s);
//
////        s = aesDecrypt("AF09YY/LjW78wHa3yyaDEA==", "123", "1234567890123456");
//        System.out.println(s);
//        Log.e("result", s);
//    }


}