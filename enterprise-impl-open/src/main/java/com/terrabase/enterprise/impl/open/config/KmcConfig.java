package com.terrabase.enterprise.impl.open.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * KMC配置管理类
 * 管理密钥管理中心的配置信息
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
@Component
public class KmcConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(KmcConfig.class);
    
    // 默认配置
    private boolean enabled = true;
    private String defaultKeyId = "default";
    private boolean keyCaching = true;
    private int maxCacheSize = 100;
    private long keyExpirationTime = 24 * 60 * 60 * 1000; // 24小时
    
    // 自定义密钥配置
    private Map<String, String> customKeys = new ConcurrentHashMap<>();
    
    // 安全配置
    private boolean enableKeyRotation = false;
    private long keyRotationInterval = 7 * 24 * 60 * 60 * 1000; // 7天
    private boolean enableAuditLog = true;
    
    @PostConstruct
    public void init() {
        logger.info("初始化KMC配置...");
        logger.info("KMC启用状态: {}", enabled);
        logger.info("默认密钥ID: {}", defaultKeyId);
        logger.info("密钥缓存: {}", keyCaching);
        logger.info("最大缓存大小: {}", maxCacheSize);
        logger.info("密钥过期时间: {} ms", keyExpirationTime);
        logger.info("密钥轮换: {}", enableKeyRotation);
        logger.info("密钥轮换间隔: {} ms", keyRotationInterval);
        logger.info("审计日志: {}", enableAuditLog);
        logger.info("自定义密钥数量: {}", customKeys.size());
        
        if (customKeys.isEmpty()) {
            logger.info("未配置自定义密钥，将使用默认密钥");
        } else {
            logger.info("已配置自定义密钥: {}", customKeys.keySet());
        }
    }
    
    // Getter和Setter方法
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getDefaultKeyId() {
        return defaultKeyId;
    }
    
    public void setDefaultKeyId(String defaultKeyId) {
        this.defaultKeyId = defaultKeyId;
    }
    
    
    public boolean isKeyCaching() {
        return keyCaching;
    }
    
    public void setKeyCaching(boolean keyCaching) {
        this.keyCaching = keyCaching;
    }
    
    public int getMaxCacheSize() {
        return maxCacheSize;
    }
    
    public void setMaxCacheSize(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }
    
    public long getKeyExpirationTime() {
        return keyExpirationTime;
    }
    
    public void setKeyExpirationTime(long keyExpirationTime) {
        this.keyExpirationTime = keyExpirationTime;
    }
    
    public Map<String, String> getCustomKeys() {
        return customKeys;
    }
    
    public void setCustomKeys(Map<String, String> customKeys) {
        this.customKeys = customKeys;
    }
    
    public boolean isEnableKeyRotation() {
        return enableKeyRotation;
    }
    
    public void setEnableKeyRotation(boolean enableKeyRotation) {
        this.enableKeyRotation = enableKeyRotation;
    }
    
    public long getKeyRotationInterval() {
        return keyRotationInterval;
    }
    
    public void setKeyRotationInterval(long keyRotationInterval) {
        this.keyRotationInterval = keyRotationInterval;
    }
    
    public boolean isEnableAuditLog() {
        return enableAuditLog;
    }
    
    public void setEnableAuditLog(boolean enableAuditLog) {
        this.enableAuditLog = enableAuditLog;
    }
    
    /**
     * 添加自定义密钥
     * @param keyId 密钥ID
     * @param keyValue 密钥值（Base64编码）
     */
    public void addCustomKey(String keyId, String keyValue) {
        customKeys.put(keyId, keyValue);
        logger.info("添加自定义密钥: {}", keyId);
    }
    
    /**
     * 移除自定义密钥
     * @param keyId 密钥ID
     * @return 是否移除成功
     */
    public boolean removeCustomKey(String keyId) {
        String removed = customKeys.remove(keyId);
        if (removed != null) {
            logger.info("移除自定义密钥: {}", keyId);
            return true;
        }
        return false;
    }
    
    /**
     * 获取自定义密钥
     * @param keyId 密钥ID
     * @return 密钥值
     */
    public String getCustomKey(String keyId) {
        return customKeys.get(keyId);
    }
    
    /**
     * 检查是否为自定义密钥
     * @param keyId 密钥ID
     * @return 是否为自定义密钥
     */
    public boolean isCustomKey(String keyId) {
        return customKeys.containsKey(keyId);
    }
    
    /**
     * 获取配置摘要
     * @return 配置摘要字符串
     */
    public String getConfigSummary() {
        return String.format(
            "KMC配置摘要 - 启用: %s, 默认密钥: %s, 缓存: %s, 自定义密钥: %d",
            enabled, defaultKeyId, keyCaching, customKeys.size()
        );
    }
}
