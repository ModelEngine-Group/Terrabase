package com.terrabase.enterprise.impl.commercial;

import com.terrabase.enterprise.api.CryptoService;
import com.terrabase.enterprise.api.CryptoAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 商业版加解密服务实现
 * 集成商业组件实现加解密功能
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
@Service
public class CommercialCryptoServiceImpl implements CryptoService {
    
    private static final Logger logger = LoggerFactory.getLogger(CommercialCryptoServiceImpl.class);

    private final AtomicBoolean running = new AtomicBoolean(false);
    
    private final RestTemplate restTemplate;
    
    private String omsBaseUrl;
    private int timeout;
    
    public CommercialCryptoServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        // 设置默认值
        this.omsBaseUrl = "http://localhost:8081/api/";
        this.timeout = 30000;
    }
    
    public CommercialCryptoServiceImpl(RestTemplate restTemplate, String omsBaseUrl, int timeout) {
        this.restTemplate = restTemplate;
        this.omsBaseUrl = omsBaseUrl;
        this.timeout = timeout;
    }
    
    @Override
    public String getServiceName() {
        return "Commercial Crypto Service";
    }
    
    @Override
    public String getServiceVersion() {
        return "1.0.0-commercial";
    }
    
    @Override
    public String getServiceType() {
        return "commercial";
    }
    
    @Override
    public String getHealthStatus() {
        if (!running.get()) {
            return "服务未运行";
        }
        
        return String.format("商业版加解密服务健康状态: 正常 (版本: %s, 类型: %s)", 
                getServiceVersion(), getServiceType());
    }

    @Override
    public void start() {
        if (running.compareAndSet(false, true)) {
            logger.info("商业版加解密服务已启动");
        } else {
            logger.warn("商业版加解密服务已经在运行中");
        }
    }

    @Override
    public void stop() {
        if (running.compareAndSet(true, false)) {
            logger.info("商业版加解密服务已停止");
        } else {
            logger.warn("商业版加解密服务已经停止");
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

        if (plaintext == null || plaintext.trim().isEmpty()) {
            return "明文数据不能为空";
        }

        if (algorithm == null) {
            algorithm = CryptoAlgorithm.AES; // 默认使用AES
        }

        try {
            logger.info("商业版执行数据加密，原文长度: {}, 算法: {}", plaintext.length(), algorithm.getAlgorithm());
            
            // 构建请求参数
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("plaintext", plaintext);
            requestBody.put("algorithm", algorithm.getAlgorithm());
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // 构建加密接口URL
            String encryptUrl = omsBaseUrl + "/enterprise/encrypt";
            
            // 调用REST接口进行加密
            ResponseEntity<Map> response = restTemplate.postForEntity(encryptUrl, requestEntity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                String ciphertext = (String) responseBody.get("ciphertext");
                String status = (String) responseBody.get("status");
                
                if ("success".equals(status) && ciphertext != null) {
                    logger.info("商业版REST接口加密成功，算法: {}", algorithm.getAlgorithm());
                    return ciphertext;
                } else {
                    String errorMsg = (String) responseBody.get("message");
                    logger.error("商业版REST接口加密失败: {}", errorMsg);
                    return "加密失败: " + (errorMsg != null ? errorMsg : "未知错误");
                }
            } else {
                logger.error("商业版REST接口加密失败，HTTP状态码: {}", response.getStatusCode());
                return "加密失败: HTTP请求失败，状态码: " + response.getStatusCode();
            }

        } catch (RestClientException e) {
            logger.error("商业版REST接口加密调用异常，算法: {}", algorithm, e);
            return "加密失败: REST接口调用异常 - " + e.getMessage();
        } catch (Exception e) {
            logger.error("商业版加密失败，算法: {}", algorithm, e);
            return "加密失败: " + e.getMessage();
        }
    }

    @Override
    public String decrypt(String ciphertext, CryptoAlgorithm algorithm) {
        if (!running.get()) {
            return "服务未运行，无法执行解密操作";
        }

        if (ciphertext == null || ciphertext.trim().isEmpty()) {
            return "密文数据不能为空";
        }

        if (algorithm == null) {
            algorithm = CryptoAlgorithm.AES; // 默认使用AES
        }

        try {
            logger.info("商业版执行数据解密，密文长度: {}, 算法: {}", ciphertext.length(), algorithm.getAlgorithm());
            
            // 构建请求参数
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("ciphertext", ciphertext);
            requestBody.put("algorithm", algorithm.getAlgorithm());
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // 构建解密接口URL
            String decryptUrl = omsBaseUrl + "/enterprise/decrypt";
            
            // 调用REST接口进行解密
            ResponseEntity<Map> response = restTemplate.postForEntity(decryptUrl, requestEntity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                String plaintext = (String) responseBody.get("plaintext");
                String status = (String) responseBody.get("status");
                
                if ("success".equals(status) && plaintext != null) {
                    logger.info("商业版REST接口解密成功，算法: {}", algorithm.getAlgorithm());
                    return plaintext;
                } else {
                    String errorMsg = (String) responseBody.get("message");
                    logger.error("商业版REST接口解密失败: {}", errorMsg);
                    return "解密失败: " + (errorMsg != null ? errorMsg : "未知错误");
                }
            } else {
                logger.error("商业版REST接口解密失败，HTTP状态码: {}", response.getStatusCode());
                return "解密失败: HTTP请求失败，状态码: " + response.getStatusCode();
            }

        } catch (RestClientException e) {
            logger.error("商业版REST接口解密调用异常，算法: {}", algorithm, e);
            return "解密失败: REST接口调用异常 - " + e.getMessage();
        } catch (Exception e) {
            logger.error("商业版解密失败，算法: {}", algorithm, e);
            return "解密失败: " + e.getMessage();
        }
    }
}
