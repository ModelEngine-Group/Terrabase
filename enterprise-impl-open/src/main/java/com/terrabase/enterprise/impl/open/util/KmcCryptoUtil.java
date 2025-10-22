package com.terrabase.enterprise.impl.open.util;

import com.terrabase.enterprise.api.CryptoAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * KMC (Key Management Center) 加解密工具类
 * 基于开源算法实现数据加解密功能
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
public class KmcCryptoUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(KmcCryptoUtil.class);
    
    // 加密算法
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12; // 96位
    private static final int GCM_TAG_LENGTH = 16; // 128位
    private static final int KEY_LENGTH = 256; // 256位密钥
    
    // 密钥缓存
    private static final Map<String, SecretKey> keyCache = new ConcurrentHashMap<>();
    private static final Map<String, KeyPair> keyPairCache = new ConcurrentHashMap<>();
    
    // 随机数生成器
    private static final SecureRandom secureRandom = new SecureRandom();
    
    static {
        // 添加BouncyCastle安全提供者
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }
    
    /**
     * 生成AES密钥（不缓存）
     * @param keyId 密钥ID
     * @return 生成的密钥
     */
    public static SecretKey generateKey(String keyId) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(KEY_LENGTH);
            SecretKey key = keyGenerator.generateKey();
            logger.info("成功生成密钥: {}", keyId);
            return key;
        } catch (NoSuchAlgorithmException e) {
            logger.error("生成密钥失败", e);
            throw new RuntimeException("生成密钥失败", e);
        }
    }
    
    /**
     * 生成并缓存AES密钥
     * @param keyId 密钥ID
     * @return 生成的密钥
     */
    public static SecretKey generateAndCacheKey(String keyId) {
        SecretKey key = generateKey(keyId);
        keyCache.put(keyId, key);
        return key;
    }
    
    /**
     * 从字节数组创建密钥
     * @param keyBytes 密钥字节数组
     * @return SecretKey对象
     */
    public static SecretKey createKeyFromBytes(byte[] keyBytes) {
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }
    
    /**
     * 获取或生成密钥
     * @param keyId 密钥ID
     * @return 密钥对象
     */
    public static SecretKey getOrGenerateKey(String keyId) {
        SecretKey key = keyCache.get(keyId);
        if (key == null) {
            key = generateAndCacheKey(keyId);
        }
        return key;
    }
    
    /**
     * 使用指定密钥加密数据
     * @param plaintext 明文数据
     * @param keyId 密钥ID
     * @return 加密后的Base64编码字符串
     */
    public static String encrypt(String plaintext, String keyId) {
        if (plaintext == null || plaintext.isEmpty()) {
            throw new IllegalArgumentException("明文数据不能为空");
        }
        
        try {
            SecretKey key = getOrGenerateKey(keyId);
            
            // 生成随机IV
            byte[] iv = new byte[GCM_IV_LENGTH];
            secureRandom.nextBytes(iv);
            
            // 创建Cipher对象
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);
            
            // 加密数据
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            
            // 将IV和加密数据组合
            byte[] combined = new byte[GCM_IV_LENGTH + encryptedBytes.length];
            System.arraycopy(iv, 0, combined, 0, GCM_IV_LENGTH);
            System.arraycopy(encryptedBytes, 0, combined, GCM_IV_LENGTH, encryptedBytes.length);
            
            // 返回Base64编码的结果
            return Base64.encodeBase64String(combined);
            
        } catch (Exception e) {
            logger.error("数据加密失败，密钥ID: {}", keyId, e);
            throw new RuntimeException("数据加密失败", e);
        }
    }
    
    /**
     * 使用指定密钥解密数据
     * @param ciphertext 密文数据（Base64编码）
     * @param keyId 密钥ID
     * @return 解密后的明文数据
     */
    public static String decrypt(String ciphertext, String keyId) {
        if (ciphertext == null || ciphertext.isEmpty()) {
            throw new IllegalArgumentException("密文数据不能为空");
        }
        
        try {
            SecretKey key = getOrGenerateKey(keyId);
            
            // 解码Base64
            byte[] combined = Base64.decodeBase64(ciphertext);
            
            // 分离IV和加密数据
            byte[] iv = new byte[GCM_IV_LENGTH];
            byte[] encryptedBytes = new byte[combined.length - GCM_IV_LENGTH];
            System.arraycopy(combined, 0, iv, 0, GCM_IV_LENGTH);
            System.arraycopy(combined, GCM_IV_LENGTH, encryptedBytes, 0, encryptedBytes.length);
            
            // 创建Cipher对象
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);
            
            // 解密数据
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
            
        } catch (Exception e) {
            logger.error("数据解密失败，密钥ID: {}", keyId, e);
            throw new RuntimeException("数据解密失败", e);
        }
    }
    
    /**
     * 使用默认密钥加密
     * @param plaintext 明文数据
     * @return 加密后的Base64编码字符串
     */
    public static String encrypt(String plaintext) {
        return encrypt(plaintext, "default");
    }
    
    /**
     * 使用默认密钥解密
     * @param ciphertext 密文数据（Base64编码）
     * @return 解密后的明文数据
     */
    public static String decrypt(String ciphertext) {
        return decrypt(ciphertext, "default");
    }
    
    /**
     * 验证密钥是否存在
     * @param keyId 密钥ID
     * @return 密钥是否存在
     */
    public static boolean hasKey(String keyId) {
        return keyCache.containsKey(keyId);
    }
    
    /**
     * 删除密钥
     * @param keyId 密钥ID
     * @return 是否删除成功
     */
    public static boolean removeKey(String keyId) {
        SecretKey removed = keyCache.remove(keyId);
        if (removed != null) {
            logger.info("密钥已删除: {}", keyId);
            return true;
        }
        return false;
    }
    
    /**
     * 清空所有密钥
     */
    public static void clearAllKeys() {
        int count = keyCache.size();
        keyCache.clear();
        logger.info("已清空所有密钥，共删除 {} 个密钥", count);
    }
    
    /**
     * 获取密钥数量
     * @return 当前缓存的密钥数量
     */
    public static int getKeyCount() {
        return keyCache.size();
    }
    
    /**
     * 获取密钥信息
     * @param keyId 密钥ID
     * @return 密钥信息字符串
     */
    public static String getKeyInfo(String keyId) {
        SecretKey key = keyCache.get(keyId);
        if (key == null) {
            return "密钥不存在: " + keyId;
        }
        
        return String.format("密钥ID: %s, 算法: %s, 长度: %d bits", 
                keyId, key.getAlgorithm(), key.getEncoded().length * 8);
    }
    
    // ==================== 多算法支持方法 ====================
    
    /**
     * 使用指定算法加密数据
     * @param plaintext 明文数据
     * @param keyId 密钥ID
     * @param algorithm 加密算法
     * @return 加密后的Base64编码字符串
     */
    public static String encrypt(String plaintext, String keyId, CryptoAlgorithm algorithm) {
        if (plaintext == null || plaintext.isEmpty()) {
            throw new IllegalArgumentException("明文数据不能为空");
        }
        
        if (algorithm == null) {
            algorithm = CryptoAlgorithm.AES; // 默认使用AES
        }
        
        try {
            if (algorithm.isSymmetric()) {
                return encryptSymmetric(plaintext, keyId, algorithm);
            } else if (algorithm.isAsymmetric()) {
                return encryptAsymmetric(plaintext, keyId, algorithm);
            } else {
                throw new IllegalArgumentException("不支持的加密算法: " + algorithm);
            }
        } catch (Exception e) {
            logger.error("数据加密失败，密钥ID: {}, 算法: {}", keyId, algorithm, e);
            throw new RuntimeException("数据加密失败", e);
        }
    }
    
    /**
     * 使用指定算法解密数据
     * @param ciphertext 密文数据（Base64编码）
     * @param keyId 密钥ID
     * @param algorithm 解密算法
     * @return 解密后的明文数据
     */
    public static String decrypt(String ciphertext, String keyId, CryptoAlgorithm algorithm) {
        if (ciphertext == null || ciphertext.isEmpty()) {
            throw new IllegalArgumentException("密文数据不能为空");
        }
        
        if (algorithm == null) {
            algorithm = CryptoAlgorithm.AES; // 默认使用AES
        }
        
        try {
            if (algorithm.isSymmetric()) {
                return decryptSymmetric(ciphertext, keyId, algorithm);
            } else if (algorithm.isAsymmetric()) {
                return decryptAsymmetric(ciphertext, keyId, algorithm);
            } else {
                throw new IllegalArgumentException("不支持的解密算法: " + algorithm);
            }
        } catch (Exception e) {
            logger.error("数据解密失败，密钥ID: {}, 算法: {}", keyId, algorithm, e);
            throw new RuntimeException("数据解密失败", e);
        }
    }
    
    /**
     * 对称加密
     * @param plaintext 明文数据
     * @param keyId 密钥ID
     * @param algorithm 加密算法
     * @return 加密后的Base64编码字符串
     */
    private static String encryptSymmetric(String plaintext, String keyId, CryptoAlgorithm algorithm) {
        try {
            SecretKey key = getOrGenerateKey(keyId);
            
            // 根据算法选择不同的加密方式
            if (algorithm == CryptoAlgorithm.AES) {
                return encryptAES(plaintext, key);
            } else if (algorithm == CryptoAlgorithm.DES) {
                return encryptDES(plaintext, key);
            } else if (algorithm == CryptoAlgorithm.TRIPLE_DES) {
                return encryptTripleDES(plaintext, key);
            } else if (algorithm == CryptoAlgorithm.BLOWFISH) {
                return encryptBlowfish(plaintext, key);
            } else {
                // 默认使用AES
                return encryptAES(plaintext, key);
            }
        } catch (Exception e) {
            logger.error("对称加密失败，算法: {}", algorithm, e);
            throw new RuntimeException("对称加密失败", e);
        }
    }
    
    /**
     * 对称解密
     * @param ciphertext 密文数据
     * @param keyId 密钥ID
     * @param algorithm 解密算法
     * @return 解密后的明文数据
     */
    private static String decryptSymmetric(String ciphertext, String keyId, CryptoAlgorithm algorithm) {
        try {
            SecretKey key = getOrGenerateKey(keyId);
            
            // 根据算法选择不同的解密方式
            if (algorithm == CryptoAlgorithm.AES) {
                return decryptAES(ciphertext, key);
            } else if (algorithm == CryptoAlgorithm.DES) {
                return decryptDES(ciphertext, key);
            } else if (algorithm == CryptoAlgorithm.TRIPLE_DES) {
                return decryptTripleDES(ciphertext, key);
            } else if (algorithm == CryptoAlgorithm.BLOWFISH) {
                return decryptBlowfish(ciphertext, key);
            } else {
                // 默认使用AES
                return decryptAES(ciphertext, key);
            }
        } catch (Exception e) {
            logger.error("对称解密失败，算法: {}", algorithm, e);
            throw new RuntimeException("对称解密失败", e);
        }
    }
    
    /**
     * AES加密
     */
    private static String encryptAES(String plaintext, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, new byte[GCM_IV_LENGTH]);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);
        
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        byte[] iv = cipher.getIV();
        
        byte[] combined = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);
        
        return Base64.encodeBase64String(combined);
    }
    
    /**
     * AES解密
     */
    private static String decryptAES(String ciphertext, SecretKey key) throws Exception {
        byte[] combined = Base64.decodeBase64(ciphertext);
        byte[] iv = new byte[GCM_IV_LENGTH];
        byte[] encryptedBytes = new byte[combined.length - GCM_IV_LENGTH];
        
        System.arraycopy(combined, 0, iv, 0, GCM_IV_LENGTH);
        System.arraycopy(combined, GCM_IV_LENGTH, encryptedBytes, 0, encryptedBytes.length);
        
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);
        
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
    
    /**
     * DES加密
     */
    private static String encryptDES(String plaintext, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        byte[] iv = cipher.getIV();
        
        byte[] combined = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);
        
        return Base64.encodeBase64String(combined);
    }
    
    /**
     * DES解密
     */
    private static String decryptDES(String ciphertext, SecretKey key) throws Exception {
        byte[] combined = Base64.decodeBase64(ciphertext);
        byte[] iv = new byte[8];
        byte[] encryptedBytes = new byte[combined.length - 8];
        
        System.arraycopy(combined, 0, iv, 0, 8);
        System.arraycopy(combined, 8, encryptedBytes, 0, encryptedBytes.length);
        
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
    
    /**
     * 3DES加密
     */
    private static String encryptTripleDES(String plaintext, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        byte[] iv = cipher.getIV();
        
        byte[] combined = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);
        
        return Base64.encodeBase64String(combined);
    }
    
    /**
     * 3DES解密
     */
    private static String decryptTripleDES(String ciphertext, SecretKey key) throws Exception {
        byte[] combined = Base64.decodeBase64(ciphertext);
        byte[] iv = new byte[8];
        byte[] encryptedBytes = new byte[combined.length - 8];
        
        System.arraycopy(combined, 0, iv, 0, 8);
        System.arraycopy(combined, 8, encryptedBytes, 0, encryptedBytes.length);
        
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
    
    /**
     * Blowfish加密
     */
    private static String encryptBlowfish(String plaintext, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        byte[] iv = cipher.getIV();
        
        byte[] combined = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);
        
        return Base64.encodeBase64String(combined);
    }
    
    /**
     * Blowfish解密
     */
    private static String decryptBlowfish(String ciphertext, SecretKey key) throws Exception {
        byte[] combined = Base64.decodeBase64(ciphertext);
        byte[] iv = new byte[8];
        byte[] encryptedBytes = new byte[combined.length - 8];
        
        System.arraycopy(combined, 0, iv, 0, 8);
        System.arraycopy(combined, 8, encryptedBytes, 0, encryptedBytes.length);
        
        Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
    
    /**
     * 非对称加密
     */
    private static String encryptAsymmetric(String plaintext, String keyId, CryptoAlgorithm algorithm) {
        // 简化实现，实际项目中需要更复杂的非对称加密逻辑
        logger.warn("非对称加密暂未完全实现，使用对称加密替代");
        return encryptSymmetric(plaintext, keyId, CryptoAlgorithm.AES);
    }
    
    /**
     * 非对称解密
     */
    private static String decryptAsymmetric(String ciphertext, String keyId, CryptoAlgorithm algorithm) {
        // 简化实现，实际项目中需要更复杂的非对称解密逻辑
        logger.warn("非对称解密暂未完全实现，使用对称解密替代");
        return decryptSymmetric(ciphertext, keyId, CryptoAlgorithm.AES);
    }
}