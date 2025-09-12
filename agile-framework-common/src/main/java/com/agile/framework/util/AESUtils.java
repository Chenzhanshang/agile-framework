package com.agile.framework.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * AES加解密的工具类
 *
 * @author chenzhanshang
 */
public final class AESUtils {

    /**
     * 密钥算法名称
     */
    private static final String ALGORITHM_NAME = "AES";

    /**
     * 密码转换的名称，算法/模式/补码方式
     */
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    /**
     * 生成密钥的基本字符
     */
    private static final String BASE_CHARACTER = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789";

    /**
     * 默认IV
     */
    public static final String IV = "1234567890123456";

    /**
     * 默认KEY
     */
    public static final String KEY = "1234567898765432";

    private AESUtils() {
    }

    /**
     * 生成随机密钥
     *
     * @return 随机密钥
     */
    public static String initKey() {
        return generateKeyOrIv();
    }

    /**
     * 生成初始向量
     *
     * @return 初始向量
     */
    public static String initIv() {
        return generateKeyOrIv();
    }

    /**
     * 生成随机密钥、初始向量
     */
    private static String generateKeyOrIv() {
        StringBuilder sBuilder = new StringBuilder();
        double r;
        for (int i = 0; i < 16; i++) {
            r = Math.random() * BASE_CHARACTER.length();
            sBuilder.append(BASE_CHARACTER.charAt((int) r));
        }
        return sBuilder.toString();
    }

    /**
     * 使用AES算法加密字符串
     *
     * @param data 需要加密的原文
     * @param key  密钥(16位字母、数字或符号)
     * @param iv   初始向量(16位字母、数字或符号)，使用CBC模式，需要一个向量iv，可增加加密算法的强度
     * @return 加密后进行Base64的密文
     * @throws Exception 加密失败
     */
    public static String encrypt(String data, String key, String iv) throws Exception {
        return Base64.getEncoder().encodeToString(encrypt(data.getBytes(StandardCharsets.UTF_8), key, iv));
    }

    /**
     * 使用AES算法加密数据
     *
     * @param data 需要加密的数据
     * @param key  密钥(16位字母、数字或符号)
     * @param iv   初始向量(16位字母、数字或符号)，使用CBC模式，需要一个向量iv，可增加加密算法的强度
     * @return 加密后的数据
     * @throws Exception 加密失败
     */
    public static byte[] encrypt(byte[] data, String key, String iv) throws Exception {
        return crypto(Cipher.ENCRYPT_MODE, data, key, iv);
    }

    /**
     * 使用AES算法解密字符串
     *
     * @param data 需要解密的密文
     * @param key  密钥(16位字母、数字或符号)
     * @param iv   初始向量(16位字母、数字或符号)
     * @return 解密后的明文
     * @throws Exception 解密失败
     */
    public static String decrypt(String data, String key, String iv) throws Exception {
        byte[] decrypted = decrypt(Base64.getDecoder().decode(data), key, iv);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    /**
     * 使用AES算法解密数据
     *
     * @param data 需要解密的数据
     * @param key  密钥(16位字母、数字或符号)
     * @param iv   初始向量(16位字母、数字或符号)
     * @return 解密后的数据
     * @throws Exception 解密失败
     */
    public static byte[] decrypt(byte[] data, String key, String iv) throws Exception {
        return crypto(Cipher.DECRYPT_MODE, data, key, iv);
    }

    /**
     * 加解密数据
     */
    private static byte[] crypto(int mode, byte[] content, String key, String iv) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM_NAME);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        cipher.init(mode, keySpec, ivParameterSpec);
        return cipher.doFinal(content);
    }

    /**
     * 使用AES算法加密字符串, 默认Key和Iv
     *
     * @param data 需要加密的原文
     * @return 加密后进行Base64的密文
     * @throws Exception 加密失败
     */
    public static String encrypt(String data) throws Exception {
        return Base64.getEncoder().encodeToString(encrypt(data.getBytes(StandardCharsets.UTF_8), KEY, IV));
    }

    /**
     * 使用AES算法解密字符串, 默认Key和Iv
     *
     * @param data 需要解密的密文
     * @return 解密后的明文
     * @throws Exception 解密失败
     */
    public static String decrypt(String data) throws Exception {
        byte[] decrypted = decrypt(Base64.getDecoder().decode(data), KEY, IV);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}
