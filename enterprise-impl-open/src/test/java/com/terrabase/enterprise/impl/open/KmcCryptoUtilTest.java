package com.terrabase.enterprise.impl.open;

import com.terrabase.enterprise.impl.open.util.KmcCryptoUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * KMC加解密工具类测试
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
@DisplayName("KMC加解密工具类测试")
public class KmcCryptoUtilTest {
    
    private static final String TEST_KEY_ID = "test_key";
    private static final String TEST_PLAINTEXT = "Hello, Terrabase KMC!";
    
    @BeforeEach
    void setUp() {
        // 清空密钥缓存
        KmcCryptoUtil.clearAllKeys();
    }
    
    @Test
    @DisplayName("测试密钥生成")
    void testGenerateKey() {
        // 生成密钥
        var key = KmcCryptoUtil.generateAndCacheKey(TEST_KEY_ID);
        
        // 验证密钥不为空
        assertNotNull(key);
        
        // 验证密钥已缓存
        assertTrue(KmcCryptoUtil.hasKey(TEST_KEY_ID));
        
        // 验证密钥信息
        String keyInfo = KmcCryptoUtil.getKeyInfo(TEST_KEY_ID);
        assertTrue(keyInfo.contains(TEST_KEY_ID));
        assertTrue(keyInfo.contains("AES"));
    }
    
    @Test
    @DisplayName("测试基本加解密功能")
    void testBasicEncryptDecrypt() {
        // 生成密钥
        KmcCryptoUtil.generateAndCacheKey(TEST_KEY_ID);
        
        // 加密
        String ciphertext = KmcCryptoUtil.encrypt(TEST_PLAINTEXT, TEST_KEY_ID);
        
        // 验证加密结果
        assertNotNull(ciphertext);
        assertNotEquals(TEST_PLAINTEXT, ciphertext);
        assertTrue(ciphertext.length() > 0);
        
        // 解密
        String decryptedText = KmcCryptoUtil.decrypt(ciphertext, TEST_KEY_ID);
        
        // 验证解密结果
        assertNotNull(decryptedText);
        assertEquals(TEST_PLAINTEXT, decryptedText);
    }
    
    @Test
    @DisplayName("测试默认密钥加解密")
    void testDefaultKeyEncryptDecrypt() {
        // 使用默认密钥加密
        String ciphertext = KmcCryptoUtil.encrypt(TEST_PLAINTEXT);
        
        // 验证加密结果
        assertNotNull(ciphertext);
        assertNotEquals(TEST_PLAINTEXT, ciphertext);
        
        // 使用默认密钥解密
        String decryptedText = KmcCryptoUtil.decrypt(ciphertext);
        
        // 验证解密结果
        assertEquals(TEST_PLAINTEXT, decryptedText);
    }
    
    @Test
    @DisplayName("测试不同密钥的加解密")
    void testDifferentKeys() {
        String keyId1 = "key1";
        String keyId2 = "key2";
        
        // 生成两个不同的密钥
        KmcCryptoUtil.generateAndCacheKey(keyId1);
        KmcCryptoUtil.generateAndCacheKey(keyId2);
        
        // 使用第一个密钥加密
        String ciphertext1 = KmcCryptoUtil.encrypt(TEST_PLAINTEXT, keyId1);
        
        // 使用第二个密钥加密
        String ciphertext2 = KmcCryptoUtil.encrypt(TEST_PLAINTEXT, keyId2);
        
        // 验证不同密钥产生的密文不同
        assertNotEquals(ciphertext1, ciphertext2);
        
        // 验证只能用对应的密钥解密
        assertEquals(TEST_PLAINTEXT, KmcCryptoUtil.decrypt(ciphertext1, keyId1));
        assertEquals(TEST_PLAINTEXT, KmcCryptoUtil.decrypt(ciphertext2, keyId2));
        
        // 验证用错误的密钥解密会失败
        assertThrows(RuntimeException.class, () -> {
            KmcCryptoUtil.decrypt(ciphertext1, keyId2);
        });
    }
    
    @Test
    @DisplayName("测试空字符串和null值处理")
    void testEmptyAndNullValues() {
        KmcCryptoUtil.generateAndCacheKey(TEST_KEY_ID);
        
        // 测试空字符串
        assertThrows(IllegalArgumentException.class, () -> {
            KmcCryptoUtil.encrypt("", TEST_KEY_ID);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            KmcCryptoUtil.decrypt("", TEST_KEY_ID);
        });
        
        // 测试null值
        assertThrows(IllegalArgumentException.class, () -> {
            KmcCryptoUtil.encrypt(null, TEST_KEY_ID);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            KmcCryptoUtil.decrypt(null, TEST_KEY_ID);
        });
    }
    
    @Test
    @DisplayName("测试长文本加解密")
    void testLongTextEncryptDecrypt() {
        KmcCryptoUtil.generateAndCacheKey(TEST_KEY_ID);
        
        // 生成长文本
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longText.append("This is a long text for testing KMC encryption and decryption. ");
        }
        String longPlaintext = longText.toString();
        
        // 加密长文本
        String ciphertext = KmcCryptoUtil.encrypt(longPlaintext, TEST_KEY_ID);
        
        // 验证加密成功
        assertNotNull(ciphertext);
        assertNotEquals(longPlaintext, ciphertext);
        
        // 解密长文本
        String decryptedText = KmcCryptoUtil.decrypt(ciphertext, TEST_KEY_ID);
        
        // 验证解密成功
        assertEquals(longPlaintext, decryptedText);
    }
    
    @Test
    @DisplayName("测试特殊字符加解密")
    void testSpecialCharactersEncryptDecrypt() {
        KmcCryptoUtil.generateAndCacheKey(TEST_KEY_ID);
        
        // 包含特殊字符的文本
        String specialText = "特殊字符测试: !@#$%^&*()_+-=[]{}|;':\",./<>?`~ 中文测试 1234567890";
        
        // 加密
        String ciphertext = KmcCryptoUtil.encrypt(specialText, TEST_KEY_ID);
        
        // 验证加密成功
        assertNotNull(ciphertext);
        assertNotEquals(specialText, ciphertext);
        
        // 解密
        String decryptedText = KmcCryptoUtil.decrypt(ciphertext, TEST_KEY_ID);
        
        // 验证解密成功
        assertEquals(specialText, decryptedText);
    }
    
    @Test
    @DisplayName("测试密钥管理功能")
    void testKeyManagement() {
        String keyId1 = "key1";
        String keyId2 = "key2";
        
        // 初始状态应该没有密钥
        assertEquals(0, KmcCryptoUtil.getKeyCount());
        
        // 生成密钥
        KmcCryptoUtil.generateAndCacheKey(keyId1);
        KmcCryptoUtil.generateAndCacheKey(keyId2);
        
        // 验证密钥数量
        assertEquals(2, KmcCryptoUtil.getKeyCount());
        
        // 验证密钥存在
        assertTrue(KmcCryptoUtil.hasKey(keyId1));
        assertTrue(KmcCryptoUtil.hasKey(keyId2));
        
        // 删除一个密钥
        assertTrue(KmcCryptoUtil.removeKey(keyId1));
        assertFalse(KmcCryptoUtil.hasKey(keyId1));
        assertTrue(KmcCryptoUtil.hasKey(keyId2));
        assertEquals(1, KmcCryptoUtil.getKeyCount());
        
        // 清空所有密钥
        KmcCryptoUtil.clearAllKeys();
        assertEquals(0, KmcCryptoUtil.getKeyCount());
        assertFalse(KmcCryptoUtil.hasKey(keyId2));
    }
    
    @Test
    @DisplayName("测试无效密文解密")
    void testInvalidCiphertextDecrypt() {
        KmcCryptoUtil.generateAndCacheKey(TEST_KEY_ID);
        
        // 测试无效的Base64字符串
        assertThrows(RuntimeException.class, () -> {
            KmcCryptoUtil.decrypt("invalid_base64_string", TEST_KEY_ID);
        });
        
        // 测试太短的密文
        assertThrows(RuntimeException.class, () -> {
            KmcCryptoUtil.decrypt("short", TEST_KEY_ID);
        });
        
        // 测试不存在的密钥
        assertThrows(RuntimeException.class, () -> {
            KmcCryptoUtil.decrypt("some_ciphertext", "non_existent_key");
        });
    }
    
    @Test
    @DisplayName("测试多次加密同一明文")
    void testMultipleEncryptionOfSamePlaintext() {
        KmcCryptoUtil.generateAndCacheKey(TEST_KEY_ID);
        
        // 多次加密同一明文
        String ciphertext1 = KmcCryptoUtil.encrypt(TEST_PLAINTEXT, TEST_KEY_ID);
        String ciphertext2 = KmcCryptoUtil.encrypt(TEST_PLAINTEXT, TEST_KEY_ID);
        
        // 验证每次加密的结果都不同（由于随机IV）
        assertNotEquals(ciphertext1, ciphertext2);
        
        // 验证都能正确解密
        assertEquals(TEST_PLAINTEXT, KmcCryptoUtil.decrypt(ciphertext1, TEST_KEY_ID));
        assertEquals(TEST_PLAINTEXT, KmcCryptoUtil.decrypt(ciphertext2, TEST_KEY_ID));
    }
}
