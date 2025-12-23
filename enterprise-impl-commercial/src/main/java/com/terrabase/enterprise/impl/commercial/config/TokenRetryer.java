package com.terrabase.enterprise.impl.commercial.config;

import feign.RetryableException;
import feign.Retryer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Token 重试器
 * 专门处理 token 相关的重试逻辑
 */
@Slf4j
@Component
public class TokenRetryer implements Retryer {
    
    @Autowired
    private TokenRetryConfig tokenRetryConfig;
    
    private int attempt = 1;
    private final long period;
    private final long maxPeriod;
    private final int maxAttempts;
    
    public TokenRetryer() {
        this(1000L, 5000L, 3);
    }
    
    public TokenRetryer(long period, long maxPeriod, int maxAttempts) {
        this.period = period;
        this.maxPeriod = maxPeriod;
        this.maxAttempts = maxAttempts;
    }
    
    @Override
    public void continueOrPropagate(RetryableException e) {
        // 检查是否是 token 重试异常
        if (e.getCause() instanceof TokenRetryErrorDecoder.TokenRetryException) {
            TokenRetryErrorDecoder.TokenRetryException tokenException = 
                (TokenRetryErrorDecoder.TokenRetryException) e.getCause();
            
            if (attempt++ >= maxAttempts) {
                log.error("Token 重试次数已达上限 ({}), 放弃重试", maxAttempts);
                throw e;
            }
            
            log.warn("Token 验证失败，准备重试 (第 {} 次)", attempt - 1);
            
            // 刷新 token
            if (tokenRetryConfig.isRefreshTokenBeforeRetry()) {
                boolean refreshSuccess = MachineTokenConfig.refreshMachineToken();
                if (refreshSuccess) {
                    log.info("Token 刷新成功，准备重试请求");
                } else {
                    log.warn("Token 刷新失败，但仍将重试");
                }
            }
            
            // 计算重试间隔
            long sleepTime = period * attempt;
            if (sleepTime > maxPeriod) {
                sleepTime = maxPeriod;
            }
            
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
                throw e;
            }
            
            return;
        }
        
        // 其他类型的异常，使用默认重试逻辑
        if (attempt++ >= maxAttempts) {
            throw e;
        }
        
        long sleepTime = period * attempt;
        if (sleepTime > maxPeriod) {
            sleepTime = maxPeriod;
        }
        
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw e;
        }
    }
    
    @Override
    public Retryer clone() {
        return new TokenRetryer(period, maxPeriod, maxAttempts);
    }
}
