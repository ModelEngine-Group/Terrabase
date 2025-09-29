package com.terrabase.enterprise.impl.commercial;

import com.terrabase.enterprise.api.MonitoringService;
import com.terrabase.enterprise.api.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 商业版监控告警服务实现
 * 集成商业组件实现监控告警功能
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
@Service
public class CommercialMonitoringServiceImpl implements MonitoringService {
    
    private static final Logger logger = LoggerFactory.getLogger(CommercialMonitoringServiceImpl.class);

    private final AtomicBoolean running = new AtomicBoolean(false);
    
    private final RestTemplate restTemplate;
    
    private String omsBaseUrl;
    private int timeout;
    
    public CommercialMonitoringServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        // 设置默认值
        this.omsBaseUrl = "http://localhost:8081/api/";
        this.timeout = 30000;
    }
    
    public CommercialMonitoringServiceImpl(RestTemplate restTemplate, String omsBaseUrl, int timeout) {
        this.restTemplate = restTemplate;
        this.omsBaseUrl = omsBaseUrl;
        this.timeout = timeout;
    }
    
    @Override
    public String getServiceName() {
        return "Commercial Monitoring Service";
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
        
        return String.format("商业版监控告警服务健康状态: 正常 (版本: %s, 类型: %s)", 
                getServiceVersion(), getServiceType());
    }

    @Override
    public void start() {
        if (running.compareAndSet(false, true)) {
            logger.info("商业版监控告警服务已启动");
        } else {
            logger.warn("商业版监控告警服务已经在运行中");
        }
    }

    @Override
    public void stop() {
        if (running.compareAndSet(true, false)) {
            logger.info("商业版监控告警服务已停止");
        } else {
            logger.warn("商业版监控告警服务已经停止");
        }
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    // ========== 告警系统相关接口实现 ==========

    @Override
    public void registerEventDefine(EventDefine eventDefine) {
        if (!running.get()) {
            logger.warn("服务未运行，无法执行告警定义注册操作");
            return;
        }

        if (eventDefine == null) {
            logger.warn("告警定义对象不能为空");
            return;
        }

        try {
            logger.info("商业版执行告警定义注册: {}", eventDefine);
            
            // 构建请求参数
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("eventDefineId", eventDefine.getEventDefineId());
            requestBody.put("eventName", eventDefine.getEventName());
            requestBody.put("eventLevel", eventDefine.getEventLevel());
            requestBody.put("eventDescription", eventDefine.getEventDescription());
            requestBody.put("eventType", eventDefine.getEventType());
            requestBody.put("threshold", eventDefine.getTriggerCondition());
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // 构建告警定义注册接口URL
            String registerEventDefineUrl = omsBaseUrl + "/monitor/v1/events/defines";
            
            // 调用REST接口进行告警定义注册
            ResponseEntity<Map> response = restTemplate.postForEntity(registerEventDefineUrl, requestEntity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                String status = (String) responseBody.get("status");
                
                if ("success".equals(status)) {
                    logger.info("商业版REST接口告警定义注册成功 - 告警定义ID: {}, 告警名称: {}, 告警级别: {}",
                            eventDefine.getEventDefineId(), eventDefine.getEventName(), eventDefine.getEventLevel());
                } else {
                    String errorMsg = (String) responseBody.get("message");
                    logger.error("商业版REST接口告警定义注册失败: {}", errorMsg);
                }
            } else {
                logger.error("商业版REST接口告警定义注册失败，HTTP状态码: {}", response.getStatusCode());
            }

        } catch (RestClientException e) {
            logger.error("商业版REST接口告警定义注册调用异常", e);
        } catch (Exception e) {
            logger.error("商业版告警定义注册失败: {}", eventDefine, e);
        }
    }

    @Override
    public void sendAlarmsBatch(List<AlarmInfo> alarms) {
        if (!running.get()) {
            logger.warn("服务未运行，无法执行告警上报操作");
            return;
        }

        if (alarms == null || alarms.isEmpty()) {
            logger.warn("告警信息列表不能为空");
            return;
        }

        try {
            logger.info("商业版执行告警批量上报，告警数量: {}", alarms.size());
            
            // 构建请求参数
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("alarms", alarms);
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // 构建告警上报接口URL
            String sendAlarmsUrl = omsBaseUrl + "/monitor/v1/events/service/send-alarm/internal";
            
            // 调用REST接口进行告警上报
            ResponseEntity<Map> response = restTemplate.postForEntity(sendAlarmsUrl, requestEntity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                String status = (String) responseBody.get("status");
                
                if ("success".equals(status)) {
                    logger.info("商业版REST接口告警批量上报成功，告警数量: {}", alarms.size());
                    for (AlarmInfo alarmInfo : alarms) {
                        logger.info("商业版告警上报成功 - 告警ID: {}, 告警名称: {}, 告警级别: {}",
                                alarmInfo.getAlarmId(), alarmInfo.getAlarmName(), alarmInfo.getAlarmLevel());
                    }
                } else {
                    String errorMsg = (String) responseBody.get("message");
                    logger.error("商业版REST接口告警批量上报失败: {}", errorMsg);
                }
            } else {
                logger.error("商业版REST接口告警批量上报失败，HTTP状态码: {}", response.getStatusCode());
            }

        } catch (RestClientException e) {
            logger.error("商业版REST接口告警批量上报调用异常", e);
        } catch (Exception e) {
            logger.error("商业版告警批量上报失败", e);
        }
    }

    @Override
    public PageResult<EventInfo> getEventsByPage(EventQueryParams queryParams) {
        if (!running.get()) {
            logger.warn("服务未运行，无法执行告警查询操作");
            return null;
        }

        if (queryParams == null) {
            logger.warn("告警查询参数对象不能为空");
            return null;
        }

        try {
            logger.info("商业版执行告警分页查询，页码: {}, 每页大小: {}", queryParams.getPageNum(), queryParams.getPageSize());
            
            // 构建请求参数
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("pageNum", queryParams.getPageNum());
            requestBody.put("pageSize", queryParams.getPageSize());
            requestBody.put("eventLevel", queryParams.getAlarmLevel());
            requestBody.put("alarmStatus", queryParams.getAlarmStatus());
            requestBody.put("startTime", queryParams.getStartTime());
            requestBody.put("endTime", queryParams.getEndTime());
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // 构建告警分页查询接口URL
            String getEventsUrl = omsBaseUrl + "/monitor/v1/events";
            
            // 调用REST接口进行告警分页查询
            ResponseEntity<Map> response = restTemplate.postForEntity(getEventsUrl, requestEntity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                String status = (String) responseBody.get("status");
                
                if ("success".equals(status)) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> eventList = (List<Map<String, Object>>) responseBody.get("eventList");
                    Long total = (Long) responseBody.get("total");
                    
                    List<EventInfo> events = new java.util.ArrayList<>();
                    if (eventList != null) {
                        for (Map<String, Object> eventMap : eventList) {
                            EventInfo event = new EventInfo(
                                (String) eventMap.get("alarmId"),
                                (String) eventMap.get("alarmName"),
                                (String) eventMap.get("alarmLevel")
                            );
                            event.setEventDefineId((String) eventMap.get("eventDefineId"));
                            event.setAlarmDescription((String) eventMap.get("alarmDescription"));
                            event.setAlarmStatus((String) eventMap.get("alarmStatus"));
                            event.setTriggerTime((Long) eventMap.get("triggerTime"));
                            event.setAlarmSource((String) eventMap.get("alarmSource"));
                            event.setHandleTime((Long) eventMap.get("handleTime"));
                            event.setHandler((String) eventMap.get("handler"));
                            event.setHandleRemark((String) eventMap.get("handleRemark"));
                            events.add(event);
                        }
                    }
                    
                    PageResult<EventInfo> result = new PageResult<>(events, total, queryParams.getPageNum(), queryParams.getPageSize());
                    
                    logger.info("商业版REST接口告警分页查询成功，查询到告警数量: {}, 总记录数: {}", events.size(), result.getTotal());
                    
                    return result;
                } else {
                    String errorMsg = (String) responseBody.get("message");
                    logger.error("商业版REST接口告警分页查询失败: {}", errorMsg);
                    return null;
                }
            } else {
                logger.error("商业版REST接口告警分页查询失败，HTTP状态码: {}", response.getStatusCode());
                return null;
            }

        } catch (RestClientException e) {
            logger.error("商业版REST接口告警分页查询调用异常", e);
            return null;
        } catch (Exception e) {
            logger.error("商业版告警分页查询失败", e);
            return null;
        }
    }

    @Override
    public AlarmDetail getAlarmDetailById(String alarmId) {
        if (!running.get()) {
            logger.warn("服务未运行，无法执行告警详情查询操作");
            return null;
        }

        if (alarmId == null || alarmId.trim().isEmpty()) {
            logger.warn("告警ID不能为空");
            return null;
        }

        try {
            logger.info("商业版执行告警详情查询，告警ID: {}", alarmId);
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            
            // 构建告警详情查询接口URL
            String getAlarmDetailUrl = omsBaseUrl + "/monitor/v1/events/" + alarmId;
            
            // 调用REST接口进行告警详情查询
            ResponseEntity<Map> response = restTemplate.exchange(getAlarmDetailUrl, HttpMethod.GET, requestEntity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                String status = (String) responseBody.get("status");
                
                if ("success".equals(status)) {
                    Map<String, Object> alarmData = (Map<String, Object>) responseBody.get("alarmDetail");
                    
                    AlarmDetail alarmDetail = new AlarmDetail(
                        (String) alarmData.get("alarmId"),
                        (String) alarmData.get("alarmName"),
                        (String) alarmData.get("alarmLevel")
                    );
                    alarmDetail.setEventDefineId((String) alarmData.get("eventDefineId"));
                    alarmDetail.setAlarmDescription((String) alarmData.get("alarmDescription"));
                    alarmDetail.setAlarmStatus((String) alarmData.get("alarmStatus"));
                    alarmDetail.setTriggerTime((Long) alarmData.get("triggerTime"));
                    alarmDetail.setAlarmSource((String) alarmData.get("alarmSource"));
                    alarmDetail.setAlarmDetails((String) alarmData.get("alarmDetails"));
                    alarmDetail.setAlarmData((String) alarmData.get("alarmData"));
                    alarmDetail.setCreateTime((Long) alarmData.get("createTime"));
                    alarmDetail.setUpdateTime((Long) alarmData.get("updateTime"));
                    
                    logger.info("商业版REST接口告警详情查询成功 - 告警ID: {}, 告警名称: {}, 告警级别: {}",
                            alarmDetail.getAlarmId(), alarmDetail.getAlarmName(), alarmDetail.getAlarmLevel());
                    
                    return alarmDetail;
                } else {
                    String errorMsg = (String) responseBody.get("message");
                    logger.error("商业版REST接口告警详情查询失败: {}", errorMsg);
                    return null;
                }
            } else {
                logger.error("商业版REST接口告警详情查询失败，HTTP状态码: {}", response.getStatusCode());
                return null;
            }

        } catch (RestClientException e) {
            logger.error("商业版REST接口告警详情查询调用异常，告警ID: {}", alarmId, e);
            return null;
        } catch (Exception e) {
            logger.error("商业版告警详情查询失败，告警ID: {}", alarmId, e);
            return null;
        }
    }
}
