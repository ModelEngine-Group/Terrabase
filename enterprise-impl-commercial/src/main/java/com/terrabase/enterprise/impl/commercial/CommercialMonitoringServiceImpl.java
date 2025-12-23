package com.terrabase.enterprise.impl.commercial;

import com.terrabase.enterprise.api.MonitoringService;
import com.terrabase.enterprise.api.dto.*;
import com.terrabase.enterprise.api.request.RegisterEventDefineReq;
import com.terrabase.enterprise.api.request.GetEventsParams;
import com.terrabase.enterprise.api.response.ResultVo;
import com.terrabase.enterprise.impl.commercial.client.AlarmFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商业版监控告警服务实现
 * 集成商业组件实现监控告警功能
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
@Service
public class CommercialMonitoringServiceImpl implements MonitoringService {
    
    private static final Logger logger = LoggerFactory.getLogger(CommercialMonitoringServiceImpl.class);
    
    @Autowired
    private AlarmFeignClient alarmFeignClient;
    
    @Override
    public ResultVo registerEventDefine(RegisterEventDefineReq req) {
        if (req == null) {
            logger.warn("告警定义注册请求对象不能为空");
            return ResultVo.error("400", "告警定义注册请求对象不能为空");
        }
        
        try {
            logger.info("商业版执行告警定义注册，服务名称: {}, 告警定义数量: {}", 
                    req.getServiceName(), req.getEventDefines().size());
            
            // 调用Feign客户端进行告警定义注册
            ResultVo result = alarmFeignClient.registerEventDefine(req);
            
            if ("200".equals(result.getCode())) {
                logger.info("商业版告警定义注册成功，服务名称: {}", req.getServiceName());
            } else {
                logger.error("商业版告警定义注册失败: {}", result.getMsg());
            }
            
            return result;
            
        } catch (Exception e) {
            logger.error("商业版告警定义注册失败", e);
            return ResultVo.error("500", "告警定义注册失败: " + e.getMessage());
        }
    }
    
    @Override
    public ResultVo<Boolean> sendEvents(List<EventInfo> alarmInfos) {
        if (alarmInfos == null || alarmInfos.isEmpty()) {
            logger.warn("告警信息列表不能为空");
            return ResultVo.error("400", "告警信息列表不能为空");
        }
        
        try {
            logger.info("商业版执行告警上报，告警数量: {}", alarmInfos.size());
            
            // 调用Feign客户端进行告警上报
            ResultVo<Boolean> result = alarmFeignClient.sendEvents(alarmInfos);
            
            if ("200".equals(result.getCode())) {
                logger.info("商业版告警上报成功，告警数量: {}", alarmInfos.size());
            } else {
                logger.error("商业版告警上报失败: {}", result.getMsg());
            }
            
            return result;
            
        } catch (Exception e) {
            logger.error("商业版告警上报失败", e);
            return ResultVo.error("500", "告警上报失败: " + e.getMessage());
        }
    }
    
    @Override
    public ResultVo<EventsCollection> getEventsByPage(GetEventsParams getEventsParams) {
        if (getEventsParams == null) {
            logger.warn("告警查询参数对象不能为空");
            return ResultVo.error("400", "告警查询参数对象不能为空");
        }
        
        try {
            logger.info("商业版执行告警分页查询，页码: {}, 每页大小: {}", 
                    getEventsParams.getPageNum(), getEventsParams.getPageSize());
            
            // 调用Feign客户端进行告警分页查询
            ResultVo<EventsCollection> result = alarmFeignClient.getEventsByPage(getEventsParams);
            
            if ("200".equals(result.getCode())) {
                EventsCollection eventsCollection = result.getData();
                logger.info("商业版告警分页查询成功，查询到告警数量: {}, 总记录数: {}", 
                        eventsCollection.getEvents().size(), eventsCollection.getTotalCount());
            } else {
                logger.error("商业版告警分页查询失败: {}", result.getMsg());
            }
            
            return result;
            
        } catch (Exception e) {
            logger.error("商业版告警分页查询失败", e);
            return ResultVo.error("500", "告警分页查询失败: " + e.getMessage());
        }
    }
}
