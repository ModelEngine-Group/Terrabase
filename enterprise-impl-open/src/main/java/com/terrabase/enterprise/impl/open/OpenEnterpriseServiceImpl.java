package com.terrabase.enterprise.impl.open;

import com.terrabase.enterprise.api.EnterpriseService;
import com.terrabase.enterprise.api.CryptoAlgorithm;
import com.terrabase.enterprise.impl.open.config.KmcConfig;
import com.terrabase.enterprise.impl.open.util.KmcCryptoUtil;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Map;

/**
 * 开源版企业服务实现
 * 基于开源数据使能技术实现
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
@Service
public class OpenEnterpriseServiceImpl implements EnterpriseService {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenEnterpriseServiceImpl.class);

    private final AtomicBoolean running = new AtomicBoolean(false);
    
    @Autowired
    private KmcConfig kmcConfig;
    
    @Override
    public String getServiceName() {
        return "Open Source Enterprise Service";
    }
    
    @Override
    public String getServiceVersion() {
        return "1.0.0-open";
    }
    
    @Override
    public String getServiceType() {
        return "open";
    }

    @Override
    public String getHealthStatus() {
        if (!running.get()) {
            return "服务未运行";
        }
        
        return String.format("开源版企业服务健康状态: 正常 (版本: %s, 类型: %s)", 
                getServiceVersion(), getServiceType());
    }

    @Override
    public void start() {
        if (running.compareAndSet(false, true)) {
            logger.info("开源版企业服务已启动");
        } else {
            logger.warn("开源版企业服务已经在运行中");
        }
    }

    @Override
    public void stop() {
        if (running.compareAndSet(true, false)) {
            logger.info("开源版企业服务已停止");
        } else {
            logger.warn("开源版企业服务已经停止");
        }
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public String encrypt(String plaintext, CryptoAlgorithm algorithm) {
        if (!running.get()) {
            return "服务未运行，无法执行加密操作";
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
            logger.info("开源版执行数据加密，原文长度: {}, 算法: {}", plaintext.length(), algorithm.getAlgorithm());
            
            // 使用KMC工具进行加密
            String ciphertext = KmcCryptoUtil.encrypt(plaintext, kmcConfig.getDefaultKeyId(), algorithm);
            
            // 记录审计日志
            if (kmcConfig.isEnableAuditLog()) {
                logger.info("KMC加密审计 - 密钥ID: {}, 算法: {}, 原文长度: {}, 密文长度: {}", 
                        kmcConfig.getDefaultKeyId(), algorithm.getAlgorithm(), plaintext.length(), ciphertext.length());
            }
            
            return String.format("开源版KMC加密成功 [%s]: %s", algorithm.getAlgorithm(), ciphertext);
            
        } catch (Exception e) {
            logger.error("开源版KMC加密失败，算法: {}", algorithm, e);
            return "KMC加密失败: " + e.getMessage();
        }
    }

    @Override
    public String decrypt(String ciphertext, CryptoAlgorithm algorithm) {
        if (!running.get()) {
            return "服务未运行，无法执行解密操作";
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
            logger.info("开源版执行数据解密，密文长度: {}, 算法: {}", ciphertext.length(), algorithm.getAlgorithm());
            
            // 使用KMC工具进行解密
            String plaintext = KmcCryptoUtil.decrypt(ciphertext, kmcConfig.getDefaultKeyId(), algorithm);
            
            // 记录审计日志
            if (kmcConfig.isEnableAuditLog()) {
                logger.info("KMC解密审计 - 密钥ID: {}, 算法: {}, 密文长度: {}, 原文长度: {}", 
                        kmcConfig.getDefaultKeyId(), algorithm.getAlgorithm(), ciphertext.length(), plaintext.length());
            }
            
            return String.format("开源版KMC解密成功 [%s]: %s", algorithm.getAlgorithm(), plaintext);
            
        } catch (Exception e) {
            logger.error("开源版KMC解密失败，算法: {}", algorithm, e);
            return "KMC解密失败: " + e.getMessage();
        }
    }
}