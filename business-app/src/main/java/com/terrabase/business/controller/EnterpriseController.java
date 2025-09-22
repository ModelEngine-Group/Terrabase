package com.terrabase.business.controller;

import com.terrabase.business.util.JarLoadUtil;
import com.terrabase.enterprise.api.EnterpriseService;
import com.terrabase.enterprise.api.CryptoAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 企业服务控制器
 * 提供REST API接口来演示动态加载的企业服务功能
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/enterprise")
@CrossOrigin(origins = "*")
public class EnterpriseController {
    
    private static final Logger logger = LoggerFactory.getLogger(EnterpriseController.class);
    
    @Autowired
    private JarLoadUtil jarLoadUtil;
    
    /**
     * 获取企业服务信息
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getServiceInfo() {
        try {
            EnterpriseService service = jarLoadUtil.loadEnterpriseService();
            
            Map<String, Object> info = new HashMap<>();
            info.put("serviceName", service.getServiceName());
            info.put("serviceVersion", service.getServiceVersion());
            info.put("serviceType", service.getServiceType());
            info.put("enterpriseMode", jarLoadUtil.getEnterpriseMode());
            info.put("healthStatus", service.getHealthStatus());
            
            return ResponseEntity.ok(info);
            
        } catch (Exception e) {
            logger.error("获取企业服务信息失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "获取企业服务信息失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * 获取健康状态
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealthStatus() {
        try {
            EnterpriseService service = jarLoadUtil.loadEnterpriseService();
            
            Map<String, Object> health = new HashMap<>();
            health.put("status", "UP");
            health.put("serviceName", service.getServiceName());
            health.put("serviceType", service.getServiceType());
            health.put("healthStatus", service.getHealthStatus());
            health.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(health);
            
        } catch (Exception e) {
            logger.error("获取健康状态失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("status", "DOWN");
            error.put("error", "获取健康状态失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * 重新加载企业服务
     */
    @PostMapping("/reload")
    public ResponseEntity<Map<String, Object>> reloadService() {
        try {
            logger.info("开始重新加载企业服务...");
            
            // 清理缓存
            jarLoadUtil.clearCache();
            
            // 重新加载服务
            EnterpriseService service = jarLoadUtil.loadEnterpriseService();
            service.start();
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "企业服务重新加载成功");
            response.put("serviceName", service.getServiceName());
            response.put("serviceType", service.getServiceType());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("重新加载企业服务失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "重新加载企业服务失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * 获取当前配置
     */
    @GetMapping("/config")
    public ResponseEntity<Map<String, Object>> getConfig() {
        try {
            Map<String, Object> config = new HashMap<>();
            config.put("enterpriseMode", jarLoadUtil.getEnterpriseMode());
            config.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(config);
            
        } catch (Exception e) {
            logger.error("获取配置信息失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "获取配置信息失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * 数据加密接口
     */
    @PostMapping("/encrypt")
    public ResponseEntity<Map<String, Object>> encrypt(@RequestBody Map<String, String> request) {
        try {
            String plaintext = request.get("plaintext");
            String algorithmName = request.get("algorithm");
            
            if (plaintext == null || plaintext.trim().isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "明文数据不能为空");
                return ResponseEntity.badRequest().body(error);
            }
            
            EnterpriseService service = jarLoadUtil.loadEnterpriseService();
            
            // 解析算法参数，如果未提供则使用默认AES
            CryptoAlgorithm algorithm = CryptoAlgorithm.fromString(algorithmName);
            String result = service.encrypt(plaintext, algorithm);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("result", result);
            response.put("serviceType", service.getServiceType());
            response.put("algorithm", algorithm.getAlgorithm());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("数据加密失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "数据加密失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * 数据解密接口
     */
    @PostMapping("/decrypt")
    public ResponseEntity<Map<String, Object>> decrypt(@RequestBody Map<String, String> request) {
        try {
            String ciphertext = request.get("ciphertext");
            String algorithmName = request.get("algorithm");
            
            if (ciphertext == null || ciphertext.trim().isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "密文数据不能为空");
                return ResponseEntity.badRequest().body(error);
            }
            
            EnterpriseService service = jarLoadUtil.loadEnterpriseService();
            
            // 解析算法参数，如果未提供则使用默认AES
            CryptoAlgorithm algorithm = CryptoAlgorithm.fromString(algorithmName);
            String result = service.decrypt(ciphertext, algorithm);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("result", result);
            response.put("serviceType", service.getServiceType());
            response.put("algorithm", algorithm.getAlgorithm());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("数据解密失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "数据解密失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * 获取支持的加密算法列表
     */
    @GetMapping("/algorithms")
    public ResponseEntity<Map<String, Object>> getSupportedAlgorithms() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("algorithms", CryptoAlgorithm.values());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("获取支持的加密算法列表失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "获取支持的加密算法列表失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
