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
 * @author Yehong Pan
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
     * 数据加密接口
     */
    @PostMapping("/encrypt")
    public ResponseEntity<Map<String, Object>> encrypt(@RequestBody Map<String, String> request) {
        try {
            String plaintext = request.get("plaintext");
            String algorithmName = request.get("algorithm");
            String username = request.get("username");
            
            if (plaintext == null || plaintext.trim().isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "明文数据不能为空");
                return ResponseEntity.badRequest().body(error);
            }
            
            CryptoService cryptoService = jarLoadUtil.loadCryptoService();
            
            // 解析算法参数，如果未提供则使用默认AES
            CryptoAlgorithm algorithm = CryptoAlgorithm.fromString(algorithmName);
            String result = cryptoService.encrypt(plaintext, algorithm, username);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("ciphertext", result);
            // 不再返回服务类型
            response.put("algorithm", algorithm != null ? algorithm.getAlgorithm() : null);
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
            String username = request.get("username");
            
            if (ciphertext == null || ciphertext.trim().isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "密文数据不能为空");
                return ResponseEntity.badRequest().body(error);
            }
            
            CryptoService cryptoService = jarLoadUtil.loadCryptoService();
            
            // 解析算法参数，如果未提供则使用默认AES
            CryptoAlgorithm algorithm = CryptoAlgorithm.fromString(algorithmName);
            String result = cryptoService.decrypt(ciphertext, algorithm, username);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("result", result);
            // 不再返回服务类型
            response.put("algorithm", algorithm != null ? algorithm.getAlgorithm() : null);
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
    
    
}
