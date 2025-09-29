package com.terrabase.enterprise.impl.commercial;

import com.terrabase.enterprise.api.LogManagementService;
import com.terrabase.enterprise.api.dto.*;
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
 * 商业版日志管理服务实现
 * 集成商业组件实现日志管理功能
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
@Service
public class CommercialLogManagementServiceImpl implements LogManagementService {
    
    private static final Logger logger = LoggerFactory.getLogger(CommercialLogManagementServiceImpl.class);

    private final AtomicBoolean running = new AtomicBoolean(false);
    
    private final RestTemplate restTemplate;
    
    private String omsBaseUrl;
    private int timeout;
    
    public CommercialLogManagementServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        // 设置默认值
        this.omsBaseUrl = "http://localhost:8081/api/";
        this.timeout = 30000;
    }
    
    public CommercialLogManagementServiceImpl(RestTemplate restTemplate, String omsBaseUrl, int timeout) {
        this.restTemplate = restTemplate;
        this.omsBaseUrl = omsBaseUrl;
        this.timeout = timeout;
    }
    
    @Override
    public String getServiceName() {
        return "Commercial Log Management Service";
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
        
        return String.format("商业版日志管理服务健康状态: 正常 (版本: %s, 类型: %s)", 
                getServiceVersion(), getServiceType());
    }

    @Override
    public void start() {
        if (running.compareAndSet(false, true)) {
            logger.info("商业版日志管理服务已启动");
        } else {
            logger.warn("商业版日志管理服务已经在运行中");
        }
    }

    @Override
    public void stop() {
        if (running.compareAndSet(true, false)) {
            logger.info("商业版日志管理服务已停止");
        } else {
            logger.warn("商业版日志管理服务已经停止");
        }
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    // ========== 日志与监控相关接口实现 ==========

    @Override
    public void registerOperateLogI18N(LogI18NS logI18NS) {
        if (!running.get()) {
            logger.warn("服务未运行，无法执行操作日志国际化信息注册操作");
            return;
        }

        if (logI18NS == null || logI18NS.getLogI18NList() == null) {
            logger.warn("操作日志国际化信息对象不能为空");
            return;
        }

        try {
            logger.info("商业版执行操作日志国际化信息注册，国际化信息数量: {}", logI18NS.getLogI18NList().size());
            
            // 构建请求参数
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("logI18NList", logI18NS.getLogI18NList());
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // 构建操作日志国际化信息注册接口URL
            String registerLogI18NUrl = omsBaseUrl + "/framework/v1/log/operateLogs/actions/register/internation/internal";
            
            // 调用REST接口进行操作日志国际化信息注册
            ResponseEntity<Map> response = restTemplate.postForEntity(registerLogI18NUrl, requestEntity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                String status = (String) responseBody.get("status");
                
                if ("success".equals(status)) {
                    logger.info("商业版REST接口操作日志国际化信息注册成功，国际化信息数量: {}", logI18NS.getLogI18NList().size());
                    for (LogI18NS.LogI18NInfo logI18NInfo : logI18NS.getLogI18NList()) {
                        logger.info("商业版操作日志国际化信息注册成功 - 操作代码: {}, 操作名称: {}, 默认语言: {}",
                                logI18NInfo.getOperationCode(), logI18NInfo.getOperationName(), logI18NInfo.getDefaultLanguage());
                    }
                } else {
                    String errorMsg = (String) responseBody.get("message");
                    logger.error("商业版REST接口操作日志国际化信息注册失败: {}", errorMsg);
                }
            } else {
                logger.error("商业版REST接口操作日志国际化信息注册失败，HTTP状态码: {}", response.getStatusCode());
            }

        } catch (RestClientException e) {
            logger.error("商业版REST接口操作日志国际化信息注册调用异常", e);
        } catch (Exception e) {
            logger.error("商业版操作日志国际化信息注册失败: {}", logI18NS, e);
        }
    }

    @Override
    public void reportOperateLog(Logs logs) {
        if (!running.get()) {
            logger.warn("服务未运行，无法执行操作日志上报操作");
            return;
        }

        if (logs == null || logs.getLogList() == null) {
            logger.warn("操作日志对象不能为空");
            return;
        }

        try {
            logger.info("商业版执行操作日志上报，日志数量: {}", logs.getLogList().size());
            
            // 构建请求参数
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("logList", logs.getLogList());
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // 构建操作日志上报接口URL
            String reportLogUrl = omsBaseUrl + "/framework/v1/log/operateLogs/actions/register/internal";
            
            // 调用REST接口进行操作日志上报
            ResponseEntity<Map> response = restTemplate.postForEntity(reportLogUrl, requestEntity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                String status = (String) responseBody.get("status");
                
                if ("success".equals(status)) {
                    logger.info("商业版REST接口操作日志上报成功，日志数量: {}", logs.getLogList().size());
                    for (Logs.LogInfo logInfo : logs.getLogList()) {
                        logger.info("商业版操作日志上报成功 - 日志ID: {}, 用户ID: {}, 操作类型: {}, 操作描述: {}",
                                logInfo.getLogId(), logInfo.getUserId(), logInfo.getOperationType(), logInfo.getOperationDescription());
                    }
                } else {
                    String errorMsg = (String) responseBody.get("message");
                    logger.error("商业版REST接口操作日志上报失败: {}", errorMsg);
                }
            } else {
                logger.error("商业版REST接口操作日志上报失败，HTTP状态码: {}", response.getStatusCode());
            }

        } catch (RestClientException e) {
            logger.error("商业版REST接口操作日志上报调用异常", e);
        } catch (Exception e) {
            logger.error("商业版操作日志上报失败: {}", logs, e);
        }
    }
}
