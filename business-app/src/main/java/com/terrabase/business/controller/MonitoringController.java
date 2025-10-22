package com.terrabase.business.controller;

import com.terrabase.business.util.JarLoadUtil;
import com.terrabase.enterprise.api.MonitoringService;
import com.terrabase.enterprise.api.dto.EventInfo;
import com.terrabase.enterprise.api.dto.EventsCollection;
import com.terrabase.enterprise.api.request.GetEventsParams;
import com.terrabase.enterprise.api.request.RegisterEventDefineReq;
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
 * 监控告警服务控制器
 * 负责提供监控告警相关的REST API接口
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/enterprise/monitoring")
@CrossOrigin(origins = "*")
public class MonitoringController {
    
    private static final Logger logger = LoggerFactory.getLogger(MonitoringController.class);
    
    @Autowired
    private JarLoadUtil jarLoadUtil;
    
    /**
     * 注册事件定义
     */
    @PostMapping("/event-define")
    public ResponseEntity<Map<String, Object>> registerEventDefine(@RequestBody RegisterEventDefineReq request) {
        try {
            if (request == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "事件定义请求不能为空");
                return ResponseEntity.badRequest().body(error);
            }
            
            MonitoringService monitoringService = jarLoadUtil.loadMonitoringService();
            ResultVo result = monitoringService.registerEventDefine(request);
            
            Map<String, Object> response = new HashMap<>();
            if ("200".equals(result.getCode())) {
                response.put("status", "success");
                response.put("message", "事件定义注册成功");
                response.put("timestamp", System.currentTimeMillis());
            } else {
                response.put("status", "failed");
                response.put("error", result.getMsg());
                response.put("code", result.getCode());
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("注册事件定义失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("status", "failed");
            error.put("error", "注册事件定义失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * 上报事件
     */
    @PostMapping("/events")
    public ResponseEntity<Map<String, Object>> sendEvents(@RequestBody List<EventInfo> eventInfos) {
        try {
            if (eventInfos == null || eventInfos.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "事件信息列表不能为空");
                return ResponseEntity.badRequest().body(error);
            }
            
            MonitoringService monitoringService = jarLoadUtil.loadMonitoringService();
            ResultVo<Boolean> result = monitoringService.sendEvents(eventInfos);
            
            Map<String, Object> response = new HashMap<>();
            if ("200".equals(result.getCode())) {
                response.put("status", "success");
                response.put("message", "事件上报成功");
                response.put("success", result.getData());
                response.put("timestamp", System.currentTimeMillis());
            } else {
                response.put("status", "failed");
                response.put("error", result.getMsg());
                response.put("code", result.getCode());
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("上报事件失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("status", "failed");
            error.put("error", "上报事件失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * 分页查询事件
     */
    @PostMapping("/events/query")
    public ResponseEntity<Map<String, Object>> getEventsByPage(@RequestBody GetEventsParams getEventsParams) {
        try {
            if (getEventsParams == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "查询参数不能为空");
                return ResponseEntity.badRequest().body(error);
            }
            
            MonitoringService monitoringService = jarLoadUtil.loadMonitoringService();
            ResultVo<EventsCollection> result = monitoringService.getEventsByPage(getEventsParams);
            
            Map<String, Object> response = new HashMap<>();
            if ("200".equals(result.getCode())) {
                response.put("status", "success");
                response.put("events", result.getData());
                response.put("timestamp", System.currentTimeMillis());
            } else {
                response.put("status", "failed");
                response.put("error", result.getMsg());
                response.put("code", result.getCode());
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("查询事件失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("status", "failed");
            error.put("error", "查询事件失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
}
