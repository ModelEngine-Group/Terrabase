package com.terrabase.enterprise.impl.open;

import com.terrabase.enterprise.api.CryptoService;
import com.terrabase.enterprise.api.CryptoAlgorithm;
import com.terrabase.enterprise.impl.open.config.KmcConfig;
import com.terrabase.enterprise.impl.open.util.KmcCryptoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 开源版加解密服务实现
 * 基于开源KMC技术实现
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
@Service
public class OpenCryptoServiceImpl implements CryptoService {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenCryptoServiceImpl.class);

    @Autowired(required = false)
    private KmcConfig kmcConfig;
    
    /**
     * 默认构造函数，用于非Spring环境下的实例化
     */
    public OpenCryptoServiceImpl() {
        // 在非Spring环境下，自动创建KmcConfig实例
        this.kmcConfig = createDefaultKmcConfig();
        if (this.kmcConfig != null) {
            logger.info("OpenCryptoServiceImpl 已在非Spring环境下成功初始化，KmcConfig自动创建完成");
        } else {
            logger.warn("OpenCryptoServiceImpl 初始化失败，KmcConfig创建失败");
        }
    }
    
    /**
     * 创建默认的KmcConfig实例
     * @return KmcConfig实例
     */
    private KmcConfig createDefaultKmcConfig() {
        try {
            KmcConfig config = new KmcConfig();
            // 设置默认配置
            config.setEnabled(true);
            config.setDefaultKeyId("sdk_default");
            config.setKeyCaching(true);
            config.setMaxCacheSize(100);
            config.setKeyExpirationTime(24 * 60 * 60 * 1000); // 24小时
            config.setEnableKeyRotation(false);
            config.setKeyRotationInterval(7 * 24 * 60 * 60 * 1000); // 7天
            config.setEnableAuditLog(true);
            
            // 手动调用初始化方法（非Spring环境）
            config.initialize();
            
            return config;
        } catch (Exception e) {
            logger.error("创建默认KmcConfig失败", e);
            return null;
        }
    }

    @Override
    public String encrypt(String plaintext, CryptoAlgorithm algorithm, String username) {
        
        if (kmcConfig == null) {
            logger.warn("KmcConfig未初始化，尝试重新创建");
            kmcConfig = createDefaultKmcConfig();
            if (kmcConfig == null) {
                return "KMC配置初始化失败，无法执行加密操作";
            }
        }
        
        if (!kmcConfig.isEnabled()) {
            return "KMC功能未启用，无法执行加密操作";
        }
        
        if (plaintext == null || plaintext.trim().isEmpty()) {
            return "明文数据不能为空";
        }
        
        if (algorithm == null) {
            algorithm = CryptoAlgorithm.AES; // 默认使用AES
        }
        
        try {
            logger.info("开源版执行数据加密，原文长度: {}, 算法: {}, 用户: {}", plaintext.length(), algorithm.getAlgorithm(), username);
            
            // 使用KMC工具进行加密
            String ciphertext = KmcCryptoUtil.encrypt(plaintext, kmcConfig.getDefaultKeyId(), algorithm);
            
            // 记录审计日志
            if (kmcConfig.isEnableAuditLog()) {
                logger.info("KMC加密审计 - 密钥ID: {}, 算法: {}, 原文长度: {}, 密文长度: {}", 
                        kmcConfig.getDefaultKeyId(), algorithm.getAlgorithm(), plaintext.length(), ciphertext.length());
            }
            
            return ciphertext;
            
        } catch (Exception e) {
            logger.error("开源版KMC加密失败，算法: {}", algorithm, e);
            return "KMC加密失败: " + e.getMessage();
        }
    }

    @Override
    public String decrypt(String ciphertext, CryptoAlgorithm algorithm, String username) {
        
        if (kmcConfig == null) {
            logger.warn("KmcConfig未初始化，尝试重新创建");
            kmcConfig = createDefaultKmcConfig();
            if (kmcConfig == null) {
                return "KMC配置初始化失败，无法执行解密操作";
            }
        }
        
        if (!kmcConfig.isEnabled()) {
            return "KMC功能未启用，无法执行解密操作";
        }
        
        if (ciphertext == null || ciphertext.trim().isEmpty()) {
            return "密文数据不能为空";
        }
        
        if (algorithm == null) {
            algorithm = CryptoAlgorithm.AES; // 默认使用AES
        }
        
        try {
            logger.info("开源版执行数据解密，密文长度: {}, 算法: {}, 用户: {}", ciphertext.length(), algorithm.getAlgorithm(), username);
            
            // 使用KMC工具进行解密
            String plaintext = KmcCryptoUtil.decrypt(ciphertext, kmcConfig.getDefaultKeyId(), algorithm);
            
            // 记录审计日志
            if (kmcConfig.isEnableAuditLog()) {
                logger.info("KMC解密审计 - 密钥ID: {}, 算法: {}, 密文长度: {}, 原文长度: {}", 
                        kmcConfig.getDefaultKeyId(), algorithm.getAlgorithm(), ciphertext.length(), plaintext.length());
            }
            
            return plaintext;
            
        } catch (Exception e) {
            logger.error("开源版KMC解密失败，算法: {}", algorithm, e);
            return "KMC解密失败: " + e.getMessage();
        }
    }
}
