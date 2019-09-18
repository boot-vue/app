package com.bootvue.utils.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA 非对称加解密
 */
public class RsaUtil {
    /**
     * 生成公钥,私钥字符串
     *
     * @return
     * @throws Exception
     */
    public static Map<String, String> init() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = new SecureRandom();
        random.setSeed(System.currentTimeMillis());
        generator.initialize(2048, random);
        KeyPair keyPair = generator.genKeyPair();//密钥对
        byte[] pub_key = Base64.encodeBase64(keyPair.getPublic().getEncoded());
        byte[] pri_key = Base64.encodeBase64(keyPair.getPrivate().getEncoded());

        Map<String, String> map = new HashMap<>();
        map.put("public", new String(pub_key));
        map.put("private", new String(pri_key));
        return map;
    }

    /**
     * 公钥,私钥保存到文件中
     * 私钥: path/key_rsa
     * 公钥: path/key_rsa.pub
     *
     * @param path
     * @param map
     */
    public static void save(String path, Map<String, String> map) throws IOException {
        String pub_key = map.get("public");
        String pri_key = map.get("private");
        FileUtils.writeStringToFile(new File(path, "key_rsa"), pri_key, "UTF-8", false);//私钥
        FileUtils.writeStringToFile(new File(path, "key_rsa.pub"), pub_key, "UTF-8", false);
    }

    /**
     * 公钥加密
     *
     * @param origin        要加密的数据
     * @param publickeyPath 公钥文件的全路径
     * @return
     */
    public static byte[] encrypt(byte[] origin, String publickeyPath) throws Exception {
        //获取公钥字符串
        byte[] bytes = IOUtils.toByteArray(new FileInputStream(publickeyPath));
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(bytes));
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);
        //加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(origin);
    }

    /**
     * 私钥解密
     *
     * @param ciphertext    密文
     * @param publickeyPath 私钥文件全路径
     * @return
     */
    public static byte[] decrypt(byte[] ciphertext, String publickeyPath) throws Exception {
        //获取私钥字符串
        byte[] bytes = IOUtils.toByteArray(new FileInputStream(publickeyPath));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(bytes));
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(ciphertext);
    }
}
