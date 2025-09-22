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
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * KMC (Key Management Center) 加解密工具类
 * 基于开源算法实现数据加解密功能
 * 
 * @author Terrabase Team
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
     * 获取或创建密钥
     * @param keyId 密钥ID
     * @return 密钥对象
     */
    public static SecretKey getOrCreateKey(String keyId) {
        return keyCache.computeIfAbsent(keyId, KmcCryptoUtil::generateKey);
    }
    
    /**
     * 加密数据
     * @param plaintext 明文数据
     * @param keyId 密钥ID
     * @return 加密后的Base64编码字符串
     */
    public static String encrypt(String plaintext, String keyId) {
        if (plaintext == null || plaintext.isEmpty()) {
            throw new IllegalArgumentException("明文数据不能为空");
        }
        
        try {
            SecretKey key = getOrCreateKey(keyId);
            
            // 生成随机IV
            byte[] iv = new byte[GCM_IV_LENGTH];
            secureRandom.nextBytes(iv);
            
            // 创建GCM参数规范
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            
            // 初始化加密器
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);
            
            // 执行加密
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            
            // 将IV和密文组合
            byte[] encryptedData = new byte[GCM_IV_LENGTH + ciphertext.length];
            System.arraycopy(iv, 0, encryptedData, 0, GCM_IV_LENGTH);
            System.arraycopy(ciphertext, 0, encryptedData, GCM_IV_LENGTH, ciphertext.length);
            
            // 返回Base64编码的结果
            String result = Base64.encodeBase64String(encryptedData);
            logger.debug("数据加密成功，密钥ID: {}, 原文长度: {}, 密文长度: {}", 
                    keyId, plaintext.length(), result.length());
            
            return result;
            
        } catch (Exception e) {
            logger.error("数据加密失败，密钥ID: {}", keyId, e);
            throw new RuntimeException("数据加密失败", e);
        }
    }
    
    /**
     * 解密数据
     * @param ciphertext 密文数据（Base64编码）
     * @param keyId 密钥ID
     * @return 解密后的明文数据
     */
    public static String decrypt(String ciphertext, String keyId) {
        if (ciphertext == null || ciphertext.isEmpty()) {
            throw new IllegalArgumentException("密文数据不能为空");
        }
        
        try {
            SecretKey key = getOrCreateKey(keyId);
            
            // 解码Base64
            byte[] encryptedData = Base64.decodeBase64(ciphertext);
            
            // 检查数据长度
            if (encryptedData.length < GCM_IV_LENGTH) {
                throw new IllegalArgumentException("密文数据格式错误");
            }
            
            // 提取IV和密文
            byte[] iv = new byte[GCM_IV_LENGTH];
            byte[] cipherBytes = new byte[encryptedData.length - GCM_IV_LENGTH];
            System.arraycopy(encryptedData, 0, iv, 0, GCM_IV_LENGTH);
            System.arraycopy(encryptedData, GCM_IV_LENGTH, cipherBytes, 0, cipherBytes.length);
            
            // 创建GCM参数规范
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            
            // 初始化解密器
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);
            
            // 执行解密
            byte[] plaintext = cipher.doFinal(cipherBytes);
            String result = new String(plaintext, StandardCharsets.UTF_8);
            
            logger.debug("数据解密成功，密钥ID: {}, 密文长度: {}, 原文长度: {}", 
                    keyId, ciphertext.length(), result.length());
            
            return result;
            
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
     */
    private static String encryptSymmetric(String plaintext, String keyId, CryptoAlgorithm algorithm) throws Exception {
        SecretKey key = getOrCreateSymmetricKey(keyId, algorithm);
        
        Cipher cipher = Cipher.getInstance(algorithm.getTransformation());
        
        if (algorithm == CryptoAlgorithm.AES) {
            // AES使用GCM模式
            byte[] iv = new byte[12]; // GCM IV长度
            secureRandom.nextBytes(iv);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);
            
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            
            // 将IV和密文组合
            byte[] encryptedData = new byte[iv.length + ciphertext.length];
            System.arraycopy(iv, 0, encryptedData, 0, iv.length);
            System.arraycopy(ciphertext, 0, encryptedData, iv.length, ciphertext.length);
            
            return Base64.encodeBase64String(encryptedData);
        } else if (algorithm == CryptoAlgorithm.CHACHA20) {
            // ChaCha20使用特殊的IV处理
            byte[] iv = new byte[12]; // ChaCha20需要12字节的nonce
            secureRandom.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            
            // 将IV和密文组合
            byte[] encryptedData = new byte[iv.length + ciphertext.length];
            System.arraycopy(iv, 0, encryptedData, 0, iv.length);
            System.arraycopy(ciphertext, 0, encryptedData, iv.length, ciphertext.length);
            
            return Base64.encodeBase64String(encryptedData);
        } else {
            // 其他对称算法使用CBC模式
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] iv = cipher.getIV();
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            
            // 将IV和密文组合
            byte[] encryptedData = new byte[iv.length + ciphertext.length];
            System.arraycopy(iv, 0, encryptedData, 0, iv.length);
            System.arraycopy(ciphertext, 0, encryptedData, iv.length, ciphertext.length);
            
            return Base64.encodeBase64String(encryptedData);
        }
    }
    
    /**
     * 对称解密
     */
    private static String decryptSymmetric(String ciphertext, String keyId, CryptoAlgorithm algorithm) throws Exception {
        SecretKey key = getOrCreateSymmetricKey(keyId, algorithm);
        
        byte[] encryptedData = Base64.decodeBase64(ciphertext);
        Cipher cipher = Cipher.getInstance(algorithm.getTransformation());
        
        if (algorithm == CryptoAlgorithm.AES) {
            // AES使用GCM模式
            byte[] iv = new byte[12];
            byte[] cipherBytes = new byte[encryptedData.length - 12];
            System.arraycopy(encryptedData, 0, iv, 0, 12);
            System.arraycopy(encryptedData, 12, cipherBytes, 0, cipherBytes.length);
            
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);
            
            byte[] plaintext = cipher.doFinal(cipherBytes);
            return new String(plaintext, StandardCharsets.UTF_8);
        } else if (algorithm == CryptoAlgorithm.CHACHA20) {
            // ChaCha20使用特殊的IV处理
            byte[] iv = new byte[12]; // ChaCha20需要12字节的nonce
            byte[] cipherBytes = new byte[encryptedData.length - 12];
            System.arraycopy(encryptedData, 0, iv, 0, 12);
            System.arraycopy(encryptedData, 12, cipherBytes, 0, cipherBytes.length);
            
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            
            byte[] plaintext = cipher.doFinal(cipherBytes);
            return new String(plaintext, StandardCharsets.UTF_8);
        } else {
            // 其他对称算法使用CBC模式
            byte[] iv = new byte[cipher.getBlockSize()];
            byte[] cipherBytes = new byte[encryptedData.length - iv.length];
            System.arraycopy(encryptedData, 0, iv, 0, iv.length);
            System.arraycopy(encryptedData, iv.length, cipherBytes, 0, cipherBytes.length);
            
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            
            byte[] plaintext = cipher.doFinal(cipherBytes);
            return new String(plaintext, StandardCharsets.UTF_8);
        }
    }
    
    /**
     * 非对称加密（RSA）
     */
    private static String encryptAsymmetric(String plaintext, String keyId, CryptoAlgorithm algorithm) throws Exception {
        KeyPair keyPair = getOrCreateKeyPair(keyId, algorithm);
        PublicKey publicKey = keyPair.getPublic();
        
        Cipher cipher = Cipher.getInstance(algorithm.getTransformation());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        
        byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(ciphertext);
    }
    
    /**
     * 非对称解密（RSA）
     */
    private static String decryptAsymmetric(String ciphertext, String keyId, CryptoAlgorithm algorithm) throws Exception {
        KeyPair keyPair = getOrCreateKeyPair(keyId, algorithm);
        PrivateKey privateKey = keyPair.getPrivate();
        
        Cipher cipher = Cipher.getInstance(algorithm.getTransformation());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        
        byte[] encryptedData = Base64.decodeBase64(ciphertext);
        byte[] plaintext = cipher.doFinal(encryptedData);
        return new String(plaintext, StandardCharsets.UTF_8);
    }
    
    /**
     * 获取或创建对称密钥
     */
    private static SecretKey getOrCreateSymmetricKey(String keyId, CryptoAlgorithm algorithm) throws Exception {
        String cacheKey = keyId + "_" + algorithm.name();
        return keyCache.computeIfAbsent(cacheKey, k -> {
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm.getAlgorithm());
                keyGenerator.init(algorithm.getDefaultKeyLength());
                return keyGenerator.generateKey();
            } catch (Exception e) {
                throw new RuntimeException("生成对称密钥失败", e);
            }
        });
    }
    
    /**
     * 获取或创建密钥对
     */
    private static KeyPair getOrCreateKeyPair(String keyId, CryptoAlgorithm algorithm) throws Exception {
        String cacheKey = keyId + "_" + algorithm.name();
        return keyPairCache.computeIfAbsent(cacheKey, k -> {
            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm.getAlgorithm());
                keyPairGenerator.initialize(algorithm.getDefaultKeyLength());
                return keyPairGenerator.generateKeyPair();
            } catch (Exception e) {
                throw new RuntimeException("生成密钥对失败", e);
            }
        });
    }
    
    /**
     * 清空所有密钥缓存
     */
    public static void clearAllCaches() {
        int symmetricCount = keyCache.size();
        int asymmetricCount = keyPairCache.size();
        keyCache.clear();
        keyPairCache.clear();
        logger.info("已清空所有密钥缓存，对称密钥: {}, 非对称密钥: {}", symmetricCount, asymmetricCount);
    }
}
