package com.bootvue.utils.encrypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * AES 对称加解密
 */
public class AesUtil {
    private String key;
    private String iv;

    /**
     * @param key 16位
     * @param iv  16位
     */
    public AesUtil(String key, String iv) {
        this.key = key;
        this.iv = iv;
    }

    // 加密
    public String encrypt(String origin) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv_ = new IvParameterSpec(iv.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv_);
        byte[] encrypted = cipher.doFinal(origin.getBytes(StandardCharsets.UTF_8));
        return new String(org.apache.commons.codec.binary.Base64.encodeBase64(encrypted)); // 此处使用BASE64做转码。
    }

    // 解密
    public String decrypt(String ciphertext) throws Exception {
        byte[] raw = key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv_ = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv_); // 根据密钥，对Cipher对象进行初始化，ENCRYPT_MODE表示加密模式
        byte[] original = cipher.doFinal(Base64.decodeBase64(ciphertext));
        return new String(original, StandardCharsets.UTF_8);
    }
}
