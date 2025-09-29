package com.terrabase.business.controller;

import com.terrabase.business.util.JarLoadUtil;
import com.terrabase.enterprise.api.CryptoService;
import com.terrabase.enterprise.api.CryptoAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 加解密服务控制器
 * 负责提供数据加解密相关的REST API接口
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/enterprise/crypto")
@CrossOrigin(origins = "*")
public class CryptoController {
    
    private static final Logger logger = LoggerFactory.getLogger(CryptoController.class);
    
    @Autowired
    private JarLoadUtil jarLoadUtil;
    
    /**
     * 获取加解密服务信息
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getCryptoServiceInfo() {
        try {
            CryptoService cryptoService = jarLoadUtil.loadCryptoService();
            
            Map<String, Object> info = new HashMap<>();
            info.put("serviceName", cryptoService.getServiceName());
            info.put("serviceVersion", cryptoService.getServiceVersion());
            info.put("serviceType", cryptoService.getServiceType());
            info.put("healthStatus", cryptoService.getHealthStatus());
            
            return ResponseEntity.ok(info);
            
        } catch (Exception e) {
            logger.error("获取加解密服务信息失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "获取加解密服务信息失败: " + e.getMessage());
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
            
            CryptoService cryptoService = jarLoadUtil.loadCryptoService();
            
            // 解析算法参数，如果未提供则使用默认AES
            CryptoAlgorithm algorithm = CryptoAlgorithm.fromString(algorithmName);
            String result = cryptoService.encrypt(plaintext, algorithm);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("ciphertext", result);
            response.put("serviceType", cryptoService.getServiceType());
            response.put("algorithm", algorithm.getAlgorithm());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("数据加密失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("status", "failed");
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
            
            CryptoService cryptoService = jarLoadUtil.loadCryptoService();
            
            // 解析算法参数，如果未提供则使用默认AES
            CryptoAlgorithm algorithm = CryptoAlgorithm.fromString(algorithmName);
            String result = cryptoService.decrypt(ciphertext, algorithm);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("result", result);
            response.put("serviceType", cryptoService.getServiceType());
            response.put("algorithm", algorithm.getAlgorithm());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("数据解密失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("status", "failed");
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
    
    /**
     * 批量加密接口
     */
    @PostMapping("/encrypt/batch")
    public ResponseEntity<Map<String, Object>> batchEncrypt(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            java.util.List<String> plaintexts = (java.util.List<String>) request.get("plaintexts");
            String algorithmName = (String) request.get("algorithm");
            
            if (plaintexts == null || plaintexts.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "明文数据列表不能为空");
                return ResponseEntity.badRequest().body(error);
            }
            
            CryptoService cryptoService = jarLoadUtil.loadCryptoService();
            CryptoAlgorithm algorithm = CryptoAlgorithm.fromString(algorithmName);
            
            java.util.List<String> results = new java.util.ArrayList<>();
            for (String plaintext : plaintexts) {
                String encrypted = cryptoService.encrypt(plaintext, algorithm);
                results.add(encrypted);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("ciphertexts", results);
            response.put("serviceType", cryptoService.getServiceType());
            response.put("algorithm", algorithm.getAlgorithm());
            response.put("count", results.size());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("批量加密失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("status", "failed");
            error.put("error", "批量加密失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * 批量解密接口
     */
    @PostMapping("/decrypt/batch")
    public ResponseEntity<Map<String, Object>> batchDecrypt(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            java.util.List<String> ciphertexts = (java.util.List<String>) request.get("ciphertexts");
            String algorithmName = (String) request.get("algorithm");
            
            if (ciphertexts == null || ciphertexts.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "密文数据列表不能为空");
                return ResponseEntity.badRequest().body(error);
            }
            
            CryptoService cryptoService = jarLoadUtil.loadCryptoService();
            CryptoAlgorithm algorithm = CryptoAlgorithm.fromString(algorithmName);
            
            java.util.List<String> results = new java.util.ArrayList<>();
            for (String ciphertext : ciphertexts) {
                String decrypted = cryptoService.decrypt(ciphertext, algorithm);
                results.add(decrypted);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("results", results);
            response.put("serviceType", cryptoService.getServiceType());
            response.put("algorithm", algorithm.getAlgorithm());
            response.put("count", results.size());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("批量解密失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("status", "failed");
            error.put("error", "批量解密失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
