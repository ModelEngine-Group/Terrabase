package com.terrabase.enterprise.api;

import com.terrabase.enterprise.api.dto.*;
import com.terrabase.enterprise.api.request.LogAttributeVo;
import com.terrabase.enterprise.api.response.ResultVo;

import java.util.List;

public interface LogManagementService {

    /**
     * 上报审计日志接口
     * @param logs 日志对象列表
     * @return ResultVo<日志条数>
     */
    ResultVo<Integer> registerLogs(List<LogAttributeVo> logs);
    
    /**
     * 注册审计日志国际化
     * @param logI18ns 国际化对象列表
     * @return ResultVo<注册结果>
     */
    ResultVo<Boolean> registryInternational(List<LogI18n> logI18ns);
}
