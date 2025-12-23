package com.terrabase.enterprise.impl.commercial.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * FeignClient内部调用前置拦截器
 * 在请求中添加机器令牌，并支持重试机制
 */
@Slf4j
@Component
public class FeignInnerRequestAuthInterceptor implements RequestInterceptor {
    private static final String X_AUTH_TOKEN_NAME = "X-Auth-Token-Inner";
    
    @Autowired
    private TokenRetryConfig tokenRetryConfig;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = getTokenWithRetry();
        if (StringUtils.isBlank(token)) {
            log.warn("Failed to get machine token, using empty token for inner request");
            token = StringUtils.EMPTY;
        }
        requestTemplate.header(X_AUTH_TOKEN_NAME, token);
        
        if (tokenRetryConfig.isVerboseLogging()) {
            log.debug("Added token to request: {} -> {}", requestTemplate.url(), 
                     token.length() > 10 ? token.substring(0, 10) + "..." : token);
        }
    }
    
    /**
     * 获取 token，支持重试机制
     */
    private String getTokenWithRetry() {
        int retryCount = 0;
        String token = null;
        
        while (retryCount <= tokenRetryConfig.getMaxRetries()) {
            try {
                if (retryCount > 0) {
                    log.info("重试获取 token，第 {} 次尝试", retryCount);
                    if (tokenRetryConfig.isRefreshTokenBeforeRetry()) {
                        MachineTokenConfig.refreshMachineToken();
                    }
                    Thread.sleep(tokenRetryConfig.getRetryInterval());
                }
                
                token = MachineTokenConfig.getMachineToken();
                if (StringUtils.isNotBlank(token)) {
                    if (retryCount > 0) {
                        log.info("Token 获取成功，重试 {} 次后成功", retryCount);
                    }
                    break;
                }
                
                retryCount++;
            } catch (Exception e) {
                log.error("获取 token 时发生异常，第 {} 次尝试", retryCount, e);
                retryCount++;
            }
        }
        
        if (StringUtils.isBlank(token)) {
            log.error("经过 {} 次重试后仍无法获取 token", tokenRetryConfig.getMaxRetries());
        }
        
        return token;
    }
}
