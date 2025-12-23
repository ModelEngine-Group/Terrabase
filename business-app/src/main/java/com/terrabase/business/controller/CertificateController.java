package com.terrabase.business.controller;

import com.terrabase.business.util.JarLoadUtil;
import com.terrabase.enterprise.api.CertificateService;
import com.terrabase.enterprise.api.dto.CertCollectInfo;
import com.terrabase.enterprise.api.dto.LicenseInfo;
import com.terrabase.enterprise.api.response.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 证书管理服务控制器
 * 负责提供证书管理相关的REST API接口
 *
 * @author Yehong Pan
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/enterprise/certificate")
@CrossOrigin(origins = "*")
public class CertificateController {
    
    private static final Logger logger = LoggerFactory.getLogger(CertificateController.class);
    
    @Autowired
    private JarLoadUtil jarLoadUtil;
    
    /**
     * 获取证书列表
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getCertificateList() {
        try {
            CertificateService certService = jarLoadUtil.loadCertificateService();
            ResultVo<List<CertCollectInfo>> result = certService.listCertificateServiceList();
            
            Map<String, Object> response = new HashMap<>();
            if ("200".equals(result.getCode())) {
                response.put("status", "success");
                response.put("certificates", result.getData());
                response.put("total", result.getData() != null ? result.getData().size() : 0);
                response.put("timestamp", System.currentTimeMillis());
            } else {
                response.put("status", "failed");
                response.put("error", result.getMsg());
                response.put("code", result.getCode());
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("获取证书列表失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("status", "failed");
            error.put("error", "获取证书列表失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * 获取License信息
     */
    @GetMapping("/license")
    public ResponseEntity<Map<String, Object>> getLicenseInfo() {
        try {
            CertificateService certService = jarLoadUtil.loadCertificateService();
            ResultVo<LicenseInfo> result = certService.getLicenseInfo();
            
            Map<String, Object> response = new HashMap<>();
            if ("200".equals(result.getCode())) {
                response.put("status", "success");
                response.put("license", result.getData());
                response.put("timestamp", System.currentTimeMillis());
            } else {
                response.put("status", "failed");
                response.put("error", result.getMsg());
                response.put("code", result.getCode());
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("获取License信息失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("status", "failed");
            error.put("error", "获取License信息失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
}
