package com.terrabase.business.controller;

import com.terrabase.business.util.JarLoadUtil;
import com.terrabase.enterprise.api.EnterpriseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 企业服务主控制器
 * 负责企业服务的基础功能：服务信息、健康状态、配置管理、服务重载等
 * 
 * @author Yehong Pan
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
     * 获取所有子服务信息概览
     */
    @GetMapping("/services/overview")
    public ResponseEntity<Map<String, Object>> getServicesOverview() {
        try {
            Map<String, Object> overview = new HashMap<>();
            overview.put("enterpriseMode", jarLoadUtil.getEnterpriseMode());
            overview.put("availableServices", new String[]{
                "crypto", "userManagement", "logManagement", 
                "certificate", "monitoring"
            });
            overview.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(overview);
            
        } catch (Exception e) {
            logger.error("获取服务概览失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "获取服务概览失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}