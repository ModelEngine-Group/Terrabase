package com.terrabase.enterprise.impl.commercial.client;

import com.terrabase.enterprise.api.dto.LogI18n;
import com.terrabase.enterprise.api.request.LogAttributeVo;
import com.terrabase.enterprise.api.response.ResultVo;
import com.terrabase.enterprise.impl.commercial.config.FeignInnerRequestAuthInterceptor;
import com.terrabase.enterprise.impl.commercial.config.HttpsFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Component
@FeignClient(value = "Framework", path = "/framework/v1/log/operateLogs", contextId = "eDataMate-operateLog",
        configuration = {HttpsFeignClientConfig.class, FeignInnerRequestAuthInterceptor.class})
public interface
OperateLogFeignClient {
    /**
     * 上报审计日志接口
     *
     * @param logs log对象列表
     * @return ResultVo<日志条数>
     */
    @PostMapping(value = "/actions/register/internal")
    ResultVo<Integer> registerLogs(List<LogAttributeVo> logs);

    /**
     * 注册审计日志国际化
     *
     * @param LogI18ns 国际化对象list
     * @return ResultVo<注册结果>
     */
    @PostMapping(value = "/actions/register/internation/internal")
    ResultVo<Boolean> registryInternational(List<LogI18n> LogI18ns);
}
