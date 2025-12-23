package com.terrabase.enterprise.impl.commercial.config;

import org.springframework.context.annotation.Configuration;

/**
 * Token 重试配置
 * 用于配置 Feign 客户端在遇到 token 失效时的重试策略
 * 配置值直接硬编码在类中，无需外部配置文件
 */
@Configuration
public class TokenRetryConfig {
    
    /**
     * 是否启用 token 重试机制
     */
    private boolean enabled = true;
    
    /**
     * 最大重试次数
     */
    private int maxRetries = 2;
    
    /**
     * 重试间隔（毫秒）
     */
    private long retryInterval = 1000;
    
    /**
     * 是否在重试前刷新 token
     */
    private boolean refreshTokenBeforeRetry = true;
    
    /**
     * 无效 token 的错误消息关键词
     */
    private String invalidTokenMessage = "invalid token";
    
    /**
     * 是否启用详细日志
     */
    private boolean verboseLogging = true;
    
    // Getters and Setters
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public int getMaxRetries() {
        return maxRetries;
    }
    
    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }
    
    public long getRetryInterval() {
        return retryInterval;
    }
    
    public void setRetryInterval(long retryInterval) {
        this.retryInterval = retryInterval;
    }
    
    public boolean isRefreshTokenBeforeRetry() {
        return refreshTokenBeforeRetry;
    }
    
    public void setRefreshTokenBeforeRetry(boolean refreshTokenBeforeRetry) {
        this.refreshTokenBeforeRetry = refreshTokenBeforeRetry;
    }
    
    public String getInvalidTokenMessage() {
        return invalidTokenMessage;
    }
    
    public void setInvalidTokenMessage(String invalidTokenMessage) {
        this.invalidTokenMessage = invalidTokenMessage;
    }
    
    public boolean isVerboseLogging() {
        return verboseLogging;
    }
    
    public void setVerboseLogging(boolean verboseLogging) {
        this.verboseLogging = verboseLogging;
    }
}
