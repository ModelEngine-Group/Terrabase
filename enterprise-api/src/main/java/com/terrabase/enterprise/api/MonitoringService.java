package com.terrabase.enterprise.api;

import com.terrabase.enterprise.api.dto.*;
import com.terrabase.enterprise.api.request.RegisterEventDefineReq;
import com.terrabase.enterprise.api.request.GetEventsParams;
import com.terrabase.enterprise.api.response.ResultVo;
import java.util.List;

/**
 * 监控告警服务接口
 * 提供告警定义注册、告警上报、告警查询等功能
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
public interface MonitoringService {
    
    /**
     * 批量注册告警定义接口
     * @param req 注册请求
     * @return 注册结果
     */
    ResultVo registerEventDefine(RegisterEventDefineReq req);
    
    /**
     * 上报告警接口
     * @param alarmInfos 告警信息列表
     * @return 是否上报成功
     */
    ResultVo<Boolean> sendEvents(List<EventInfo> alarmInfos);
    
    /**
     * 批量查询告警信息接口
     * @param getEventsParams 查询请求
     * @return 查询结果
     */
    ResultVo<EventsCollection> getEventsByPage(GetEventsParams getEventsParams);
}
