package com.terrabase.business.controller;

import com.terrabase.business.util.JarLoadUtil;
import com.terrabase.enterprise.api.LogService;
import com.terrabase.enterprise.api.dto.LogI18n;
import com.terrabase.enterprise.api.request.LogAttributeVo;
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
 * 日志管理服务控制器
 * 负责提供日志管理相关的REST API接口
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/enterprise/log")
@CrossOrigin(origins = "*")
public class LogManagementController {
    
    private static final Logger logger = LoggerFactory.getLogger(LogManagementController.class);
    
    @Autowired
    private JarLoadUtil jarLoadUtil;
    
    /**
     * 上报审计日志
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerLogs(@RequestBody List<LogAttributeVo> logs) {
        try {
            if (logs == null || logs.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "日志列表不能为空");
                return ResponseEntity.badRequest().body(error);
            }
            
            LogService logService = jarLoadUtil.loadLogService();
            ResultVo<Integer> result = logService.registerLogs(logs);
            
            Map<String, Object> response = new HashMap<>();
            if ("200".equals(result.getCode())) {
                response.put("status", "success");
                response.put("message", "日志上报成功");
                response.put("logCount", result.getData());
                response.put("timestamp", System.currentTimeMillis());
            } else {
                response.put("status", "failed");
                response.put("error", result.getMsg());
                response.put("code", result.getCode());
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("上报审计日志失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("status", "failed");
            error.put("error", "上报审计日志失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * 注册审计日志国际化
     */
    @PostMapping("/i18n")
    public ResponseEntity<Map<String, Object>> registryInternational(@RequestBody List<LogI18n> logI18ns) {
        try {
            if (logI18ns == null || logI18ns.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "国际化信息列表不能为空");
                return ResponseEntity.badRequest().body(error);
            }
            
            LogService logService = jarLoadUtil.loadLogService();
            ResultVo<Boolean> result = logService.registryInternational(logI18ns);
            
            Map<String, Object> response = new HashMap<>();
            if ("200".equals(result.getCode())) {
                response.put("status", "success");
                response.put("message", "审计日志国际化注册成功");
                response.put("registered", result.getData());
                response.put("timestamp", System.currentTimeMillis());
            } else {
                response.put("status", "failed");
                response.put("error", result.getMsg());
                response.put("code", result.getCode());
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("注册审计日志国际化失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("status", "failed");
            error.put("error", "注册审计日志国际化失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
}
