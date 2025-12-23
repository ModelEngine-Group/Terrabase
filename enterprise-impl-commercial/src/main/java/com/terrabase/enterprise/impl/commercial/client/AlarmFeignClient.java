package com.terrabase.enterprise.impl.commercial.client;

import com.terrabase.enterprise.api.dto.EventInfo;
import com.terrabase.enterprise.api.dto.EventsCollection;
import com.terrabase.enterprise.api.request.GetEventsParams;
import com.terrabase.enterprise.api.request.RegisterEventDefineReq;
import com.terrabase.enterprise.api.response.ResultVo;
import com.terrabase.enterprise.impl.commercial.config.FeignInnerRequestAuthInterceptor;
import com.terrabase.enterprise.impl.commercial.config.HttpsFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
@FeignClient(value = "Monitor", path = "/monitor/v1", contextId = "eDataMate-alarm",
        configuration = {HttpsFeignClientConfig.class, FeignInnerRequestAuthInterceptor.class})
public interface AlarmFeignClient {
    /**
     * 批量注册权限
     *
     * @param req 注册请求
     * @return 注册结果
     */
    @PostMapping({"/events/defines"})
    ResultVo registerEventDefine(@RequestBody RegisterEventDefineReq req);

    /**
     * 上报告警
     *
     * @param alarmInfos 告警信息
     * @return 是否上报成功
     */
    @PostMapping({"/events/service/send-alarm/internal"})
    ResultVo<Boolean> sendEvents(@RequestBody List<EventInfo> alarmInfos);

    /**
     * 批量查询告警信息
     *
     * @param getEventsParams 查询请求
     * @return 查询结果
     */
    @GetMapping({"/events"})
    ResultVo<EventsCollection> getEventsByPage(@SpringQueryMap GetEventsParams getEventsParams);
}
