package com.wdy.autosubmit.function;

import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @Author WDY
 * @Date 2020-05-09 13:25
 * @Description TODO
 */
public class encryptAES {
    public String getPassword(String password,String pwdDefaultEncryptSalt) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        String randomStr64 = this.randomStr(64);
        String randomStr16 = this.randomStr(16);
        //加密后的密码
        String passwd = this.getAecString(randomStr64 + password, pwdDefaultEncryptSalt, randomStr16);
        return passwd;
    }
    //AES-128-CBC加密
    public String getAecString(String date,String key,String iv) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        //数据填充方式
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("utf-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(iv.getBytes("utf-8")));
        byte[] encrypted = cipher.doFinal(date.getBytes("utf-8"));
        return new BASE64Encoder().encode(encrypted).toString().replaceAll("[\\s*\t\n\r]", "");
    }
    //模拟前端CryptoJS加密
    public String randomStr(int num) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < num; i++) {
            stringBuilder.append(str.charAt(new Random().nextInt(62)));
        }
        return  stringBuilder.toString();
    }
}
