package com.terrabase.enterprise.impl.commercial.config;

import com.terrabase.enterprise.api.response.ResultVo;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Token 重试错误解码器
 * 检测 "invalid token" 错误并抛出特殊异常以触发重试机制
 */
@Slf4j
@Component
public class TokenRetryErrorDecoder implements ErrorDecoder {
    
    @Autowired
    private TokenRetryConfig tokenRetryConfig;
    
    private final ErrorDecoder defaultErrorDecoder = new Default();
    
    @Override
    public Exception decode(String methodKey, Response response) {
        // 如果重试机制未启用，使用默认错误解码器
        if (!tokenRetryConfig.isEnabled()) {
            return defaultErrorDecoder.decode(methodKey, response);
        }
        
        // 检查是否是 token 相关错误
        if (isTokenError(response)) {
            log.warn("检测到 token 错误，响应状态: {}, 方法: {}", response.status(), methodKey);
            return new TokenRetryException("Token 验证失败，需要重试", response.status());
        }
        
        // 其他错误使用默认处理
        return defaultErrorDecoder.decode(methodKey, response);
    }
    
    /**
     * 检查是否是 token 相关错误
     */
    private boolean isTokenError(Response response) {
        try {
            // 检查状态码
            if (response.status() == 401 || response.status() == 403) {
                return true;
            }
            
            // 检查响应体中的错误消息
            if (response.body() != null) {
                String body = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
                if (body != null && body.toLowerCase().contains(tokenRetryConfig.getInvalidTokenMessage().toLowerCase())) {
                    return true;
                }
                
                // 尝试解析 ResultVo 格式的响应
                try {
                    // 简单的字符串匹配，避免依赖 Jackson
                    if (body.contains("\"msg\"") && body.contains(tokenRetryConfig.getInvalidTokenMessage())) {
                        return true;
                    }
                } catch (Exception e) {
                    // 忽略解析错误
                }
            }
        } catch (IOException e) {
            log.warn("读取响应体失败", e);
        }
        
        return false;
    }
    
    /**
     * Token 重试异常
     */
    public static class TokenRetryException extends RuntimeException {
        private final int status;
        
        public TokenRetryException(String message, int status) {
            super(message);
            this.status = status;
        }
        
        public int getStatus() {
            return status;
        }
    }
}
