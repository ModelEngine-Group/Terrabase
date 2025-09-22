package com.terrabase.enterprise.impl.open;

import com.terrabase.enterprise.api.CryptoAlgorithm;
import com.terrabase.enterprise.impl.open.config.KmcConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 开源版企业服务KMC功能集成测试
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
@SpringBootTest(classes = TestConfiguration.class)
@TestPropertySource(properties = {
    "terrabase.kmc.enabled=true",
    "terrabase.kmc.default-key-id=test_default",
    "terrabase.kmc.enable-audit-log=true"
})
@DisplayName("开源版企业服务KMC功能集成测试")
public class OpenEnterpriseServiceImplKmcTest {
    
    private OpenEnterpriseServiceImpl service;
    private KmcConfig kmcConfig;
    
    @BeforeEach
    void setUp() {
        service = new OpenEnterpriseServiceImpl();
        kmcConfig = new KmcConfig();
        kmcConfig.setEnabled(true);
        kmcConfig.setDefaultKeyId("test_default");
        kmcConfig.setEnableAuditLog(true);
        
        // 使用反射设置私有字段
        try {
            var field = service.getClass().getDeclaredField("kmcConfig");
            field.setAccessible(true);
            field.set(service, kmcConfig);
            
            // 启动服务
            var runningField = service.getClass().getDeclaredField("running");
            runningField.setAccessible(true);
            var running = (java.util.concurrent.atomic.AtomicBoolean) runningField.get(service);
            running.set(true);
        } catch (Exception e) {
            throw new RuntimeException("设置KMC配置失败", e);
        }
    }
    
    @Test
    @DisplayName("测试服务初始化")
    void testServiceInitialization() {
        // 验证服务信息
        assertEquals("Open Source Enterprise Service", service.getServiceName());
        assertEquals("1.0.0-open", service.getServiceVersion());
        assertEquals("open", service.getServiceType());
        
        // 验证健康状态
        String healthStatus = service.getHealthStatus();
        assertTrue(healthStatus.contains("正常"));
        assertTrue(healthStatus.contains("开源版"));
    }
    
    @Test
    @DisplayName("测试KMC加密功能")
    void testKmcEncryption() {
        String plaintext = "测试KMC加密功能";
        
        // 执行加密（使用默认AES算法）
        String result = service.encrypt(plaintext, CryptoAlgorithm.AES);
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result.contains("开源版KMC加密成功"));
        assertTrue(result.contains("[AES]"));
        
        // 提取密文部分进行验证
        String ciphertext = result.substring(result.indexOf(": ") + 2);
        assertNotNull(ciphertext);
        assertTrue(ciphertext.length() > 0);
    }
    
    @Test
    @DisplayName("测试KMC解密功能")
    void testKmcDecryption() {
        String plaintext = "测试KMC解密功能";
        
        // 先加密
        String encryptResult = service.encrypt(plaintext, CryptoAlgorithm.AES);
        assertTrue(encryptResult.contains("开源版KMC加密成功"));
        assertTrue(encryptResult.contains("[AES]"));
        
        // 提取密文
        String ciphertext = encryptResult.substring(encryptResult.indexOf(": ") + 2);
        
        // 执行解密
        String decryptResult = service.decrypt(ciphertext, CryptoAlgorithm.AES);
        
        // 验证结果
        assertNotNull(decryptResult);
        assertTrue(decryptResult.contains("开源版KMC解密成功"));
        assertTrue(decryptResult.contains("[AES]"));
        assertTrue(decryptResult.contains(plaintext));
    }
    
    @Test
    @DisplayName("测试完整的加解密流程")
    void testCompleteEncryptDecryptFlow() {
        String originalText = "完整的KMC加解密流程测试数据";
        
        // 加密
        String encryptResult = service.encrypt(originalText, CryptoAlgorithm.AES);
        assertTrue(encryptResult.contains("开源版KMC加密成功"));
        assertTrue(encryptResult.contains("[AES]"));
        
        // 提取密文
        String ciphertext = encryptResult.substring(encryptResult.indexOf(": ") + 2);
        
        // 解密
        String decryptResult = service.decrypt(ciphertext, CryptoAlgorithm.AES);
        assertTrue(decryptResult.contains("开源版KMC解密成功"));
        assertTrue(decryptResult.contains("[AES]"));
        
        // 验证原文和最终解密结果一致
        String finalDecryptedText = decryptResult.substring(decryptResult.indexOf(": ") + 2);
        assertEquals(originalText, finalDecryptedText);
    }
    
    @Test
    @DisplayName("测试空字符串处理")
    void testEmptyStringHandling() {
        // 测试空字符串加密
        String emptyResult = service.encrypt("", CryptoAlgorithm.AES);
        assertTrue(emptyResult.contains("明文数据不能为空"));
        
        // 测试空字符串解密
        String emptyDecryptResult = service.decrypt("", CryptoAlgorithm.AES);
        assertTrue(emptyDecryptResult.contains("密文数据不能为空"));
    }
    
    @Test
    @DisplayName("测试null值处理")
    void testNullValueHandling() {
        // 测试null值加密
        String nullResult = service.encrypt(null, CryptoAlgorithm.AES);
        assertTrue(nullResult.contains("明文数据不能为空"));
        
        // 测试null值解密
        String nullDecryptResult = service.decrypt(null, CryptoAlgorithm.AES);
        assertTrue(nullDecryptResult.contains("密文数据不能为空"));
    }
    
    @Test
    @DisplayName("测试KMC功能禁用时的行为")
    void testKmcDisabledBehavior() {
        // 禁用KMC功能
        kmcConfig.setEnabled(false);
        
        // 测试加密
        String encryptResult = service.encrypt("测试数据", CryptoAlgorithm.AES);
        assertTrue(encryptResult.contains("KMC功能未启用"));
        
        // 测试解密
        String decryptResult = service.decrypt("测试密文", CryptoAlgorithm.AES);
        assertTrue(decryptResult.contains("KMC功能未启用"));
    }
    
    @Test
    @DisplayName("测试服务未运行时的行为")
    void testServiceNotRunningBehavior() {
        // 创建一个未启动的服务实例
        OpenEnterpriseServiceImpl stoppedService = new OpenEnterpriseServiceImpl();
        try {
            var field = stoppedService.getClass().getDeclaredField("kmcConfig");
            field.setAccessible(true);
            field.set(stoppedService, kmcConfig);
        } catch (Exception e) {
            throw new RuntimeException("设置KMC配置失败", e);
        }
        
        // 测试加密
        String encryptResult = stoppedService.encrypt("测试数据", CryptoAlgorithm.AES);
        assertTrue(encryptResult.contains("服务未运行"));
        
        // 测试解密
        String decryptResult = stoppedService.decrypt("测试密文", CryptoAlgorithm.AES);
        assertTrue(decryptResult.contains("服务未运行"));
    }
    
    @Test
    @DisplayName("测试特殊字符加解密")
    void testSpecialCharactersEncryptDecrypt() {
        String specialText = "特殊字符测试: !@#$%^&*()_+-=[]{}|;':\",./<>?`~ 中文测试 1234567890";
        
        // 加密
        String encryptResult = service.encrypt(specialText, CryptoAlgorithm.AES);
        assertTrue(encryptResult.contains("开源版KMC加密成功"));
        assertTrue(encryptResult.contains("[AES]"));
        
        // 提取密文
        String ciphertext = encryptResult.substring(encryptResult.indexOf(": ") + 2);
        
        // 解密
        String decryptResult = service.decrypt(ciphertext, CryptoAlgorithm.AES);
        assertTrue(decryptResult.contains("开源版KMC解密成功"));
        assertTrue(decryptResult.contains("[AES]"));
        
        // 验证原文和最终解密结果一致
        String finalDecryptedText = decryptResult.substring(decryptResult.indexOf(": ") + 2);
        assertEquals(specialText, finalDecryptedText);
    }
    
    @Test
    @DisplayName("测试长文本加解密")
    void testLongTextEncryptDecrypt() {
        // 生成长文本
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longText.append("这是一个用于测试KMC加解密功能的长文本数据。");
        }
        String longPlaintext = longText.toString();
        
        // 加密
        String encryptResult = service.encrypt(longPlaintext, CryptoAlgorithm.AES);
        assertTrue(encryptResult.contains("开源版KMC加密成功"));
        assertTrue(encryptResult.contains("[AES]"));
        
        // 提取密文
        String ciphertext = encryptResult.substring(encryptResult.indexOf(": ") + 2);
        
        // 解密
        String decryptResult = service.decrypt(ciphertext, CryptoAlgorithm.AES);
        assertTrue(decryptResult.contains("开源版KMC解密成功"));
        assertTrue(decryptResult.contains("[AES]"));
        
        // 验证原文和最终解密结果一致
        String finalDecryptedText = decryptResult.substring(decryptResult.indexOf(": ") + 2);
        assertEquals(longPlaintext, finalDecryptedText);
    }
    
    @Test
    @DisplayName("测试多次加密同一明文")
    void testMultipleEncryptionOfSamePlaintext() {
        String plaintext = "多次加密测试数据";
        
        // 第一次加密
        String encryptResult1 = service.encrypt(plaintext, CryptoAlgorithm.AES);
        assertTrue(encryptResult1.contains("开源版KMC加密成功"));
        assertTrue(encryptResult1.contains("[AES]"));
        String ciphertext1 = encryptResult1.substring(encryptResult1.indexOf(": ") + 2);
        
        // 第二次加密
        String encryptResult2 = service.encrypt(plaintext, CryptoAlgorithm.AES);
        assertTrue(encryptResult2.contains("开源版KMC加密成功"));
        assertTrue(encryptResult2.contains("[AES]"));
        String ciphertext2 = encryptResult2.substring(encryptResult2.indexOf(": ") + 2);
        
        // 验证两次加密的结果不同（由于随机IV）
        assertNotEquals(ciphertext1, ciphertext2);
        
        // 验证两次加密都能正确解密
        String decryptResult1 = service.decrypt(ciphertext1, CryptoAlgorithm.AES);
        String decryptResult2 = service.decrypt(ciphertext2, CryptoAlgorithm.AES);
        
        assertTrue(decryptResult1.contains("开源版KMC解密成功"));
        assertTrue(decryptResult1.contains("[AES]"));
        assertTrue(decryptResult2.contains("开源版KMC解密成功"));
        assertTrue(decryptResult2.contains("[AES]"));
        
        String finalDecryptedText1 = decryptResult1.substring(decryptResult1.indexOf(": ") + 2);
        String finalDecryptedText2 = decryptResult2.substring(decryptResult2.indexOf(": ") + 2);
        
        assertEquals(plaintext, finalDecryptedText1);
        assertEquals(plaintext, finalDecryptedText2);
    }
    
    @Test
    @DisplayName("测试RSA加密算法")
    void testRsaEncryption() {
        String plaintext = "RSA加密测试数据";
        
        // 加密
        String encryptResult = service.encrypt(plaintext, CryptoAlgorithm.RSA);
        assertTrue(encryptResult.contains("开源版KMC加密成功"));
        assertTrue(encryptResult.contains("[RSA]"));
        
        // 提取密文
        String ciphertext = encryptResult.substring(encryptResult.indexOf(": ") + 2);
        
        // 解密
        String decryptResult = service.decrypt(ciphertext, CryptoAlgorithm.RSA);
        assertTrue(decryptResult.contains("开源版KMC解密成功"));
        assertTrue(decryptResult.contains("[RSA]"));
        
        // 验证原文和最终解密结果一致
        String finalDecryptedText = decryptResult.substring(decryptResult.indexOf(": ") + 2);
        assertEquals(plaintext, finalDecryptedText);
    }
    
    @Test
    @DisplayName("测试DES加密算法")
    void testDesEncryption() {
        String plaintext = "DES加密测试数据";
        
        // 加密
        String encryptResult = service.encrypt(plaintext, CryptoAlgorithm.DES);
        assertTrue(encryptResult.contains("开源版KMC加密成功"));
        assertTrue(encryptResult.contains("[DES]"));
        
        // 提取密文
        String ciphertext = encryptResult.substring(encryptResult.indexOf(": ") + 2);
        
        // 解密
        String decryptResult = service.decrypt(ciphertext, CryptoAlgorithm.DES);
        assertTrue(decryptResult.contains("开源版KMC解密成功"));
        assertTrue(decryptResult.contains("[DES]"));
        
        // 验证原文和最终解密结果一致
        String finalDecryptedText = decryptResult.substring(decryptResult.indexOf(": ") + 2);
        assertEquals(plaintext, finalDecryptedText);
    }
    
    @Test
    @DisplayName("测试3DES加密算法")
    void testTripleDesEncryption() {
        String plaintext = "3DES加密测试数据";
        
        // 加密
        String encryptResult = service.encrypt(plaintext, CryptoAlgorithm.TRIPLE_DES);
        assertTrue(encryptResult.contains("开源版KMC加密成功"));
        assertTrue(encryptResult.contains("[DESede]"));
        
        // 提取密文
        String ciphertext = encryptResult.substring(encryptResult.indexOf(": ") + 2);
        
        // 解密
        String decryptResult = service.decrypt(ciphertext, CryptoAlgorithm.TRIPLE_DES);
        assertTrue(decryptResult.contains("开源版KMC解密成功"));
        assertTrue(decryptResult.contains("[DESede]"));
        
        // 验证原文和最终解密结果一致
        String finalDecryptedText = decryptResult.substring(decryptResult.indexOf(": ") + 2);
        assertEquals(plaintext, finalDecryptedText);
    }
    
    @Test
    @DisplayName("测试Blowfish加密算法")
    void testBlowfishEncryption() {
        String plaintext = "Blowfish加密测试数据";
        
        // 加密
        String encryptResult = service.encrypt(plaintext, CryptoAlgorithm.BLOWFISH);
        assertTrue(encryptResult.contains("开源版KMC加密成功"));
        assertTrue(encryptResult.contains("[Blowfish]"));
        
        // 提取密文
        String ciphertext = encryptResult.substring(encryptResult.indexOf(": ") + 2);
        
        // 解密
        String decryptResult = service.decrypt(ciphertext, CryptoAlgorithm.BLOWFISH);
        assertTrue(decryptResult.contains("开源版KMC解密成功"));
        assertTrue(decryptResult.contains("[Blowfish]"));
        
        // 验证原文和最终解密结果一致
        String finalDecryptedText = decryptResult.substring(decryptResult.indexOf(": ") + 2);
        assertEquals(plaintext, finalDecryptedText);
    }
    
    @Test
    @DisplayName("测试ChaCha20加密算法")
    void testChaCha20Encryption() {
        String plaintext = "ChaCha20加密测试数据";
        
        // 加密
        String encryptResult = service.encrypt(plaintext, CryptoAlgorithm.CHACHA20);
        assertTrue(encryptResult.contains("开源版KMC加密成功"));
        assertTrue(encryptResult.contains("[ChaCha20]"));
        
        // 提取密文
        String ciphertext = encryptResult.substring(encryptResult.indexOf(": ") + 2);
        
        // 解密
        String decryptResult = service.decrypt(ciphertext, CryptoAlgorithm.CHACHA20);
        assertTrue(decryptResult.contains("开源版KMC解密成功"));
        assertTrue(decryptResult.contains("[ChaCha20]"));
        
        // 验证原文和最终解密结果一致
        String finalDecryptedText = decryptResult.substring(decryptResult.indexOf(": ") + 2);
        assertEquals(plaintext, finalDecryptedText);
    }
    
    @Test
    @DisplayName("测试null算法参数（应该使用默认AES）")
    void testNullAlgorithmParameter() {
        String plaintext = "测试null算法参数";
        
        // 加密（传递null算法参数）
        String encryptResult = service.encrypt(plaintext, null);
        assertTrue(encryptResult.contains("开源版KMC加密成功"));
        assertTrue(encryptResult.contains("[AES]")); // 应该使用默认AES
        
        // 提取密文
        String ciphertext = encryptResult.substring(encryptResult.indexOf(": ") + 2);
        
        // 解密（传递null算法参数）
        String decryptResult = service.decrypt(ciphertext, null);
        assertTrue(decryptResult.contains("开源版KMC解密成功"));
        assertTrue(decryptResult.contains("[AES]")); // 应该使用默认AES
        
        // 验证原文和最终解密结果一致
        String finalDecryptedText = decryptResult.substring(decryptResult.indexOf(": ") + 2);
        assertEquals(plaintext, finalDecryptedText);
    }
}
