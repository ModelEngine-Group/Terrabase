package com.terrabase.enterprise.impl.commercial;

import com.terrabase.enterprise.api.LogService;
import com.terrabase.enterprise.api.dto.*;
import com.terrabase.enterprise.api.request.LogAttributeVo;
import com.terrabase.enterprise.api.response.ResultVo;
import com.terrabase.enterprise.impl.commercial.client.OperateLogFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商业版日志管理服务实现
 * 集成商业组件实现日志管理功能
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
@Service
public class CommercialLogServiceImpl implements LogService {
    
    private static final Logger logger = LoggerFactory.getLogger(CommercialLogServiceImpl.class);

    @Autowired
    private OperateLogFeignClient operateLogFeignClient;

    @Override
    public ResultVo<Integer> registerLogs(List<LogAttributeVo> logs) {
        if (logs == null || logs.isEmpty()) {
            logger.warn("审计日志对象不能为空");
            return ResultVo.error("400", "审计日志对象不能为空");
        }

        try {
            logger.info("商业版执行审计日志上报，日志数量: {}", logs.size());
            
            // 使用 Feign 客户端调用远程服务
            ResultVo<Integer> result = operateLogFeignClient.registerLogs(logs);
            
            if ("200".equals(result.getCode())) {
                logger.info("商业版审计日志上报成功，日志数量: {}", result.getData());
                return result;
            } else {
                logger.error("商业版审计日志上报失败: {}", result.getMsg());
                return ResultVo.error(result.getCode(), result.getMsg());
            }

        } catch (Exception e) {
            logger.error("商业版审计日志上报失败", e);
            return ResultVo.error("500", "审计日志上报失败: " + e.getMessage());
        }
    }

    @Override
    public ResultVo<Boolean> registryInternational(List<LogI18n> logI18ns) {
        if (logI18ns == null || logI18ns.isEmpty()) {
            logger.warn("审计日志国际化对象不能为空");
            return ResultVo.error("400", "审计日志国际化对象不能为空");
        }

        try {
            logger.info("商业版执行审计日志国际化注册，国际化信息数量: {}", logI18ns.size());
            
            // 使用 Feign 客户端调用远程服务
            ResultVo<Boolean> result = operateLogFeignClient.registryInternational(logI18ns);
            
            if ("200".equals(result.getCode())) {
                logger.info("商业版审计日志国际化注册成功: {}", result.getData());
                return result;
            } else {
                logger.error("商业版审计日志国际化注册失败: {}", result.getMsg());
                return ResultVo.error(result.getCode(), result.getMsg());
            }

        } catch (Exception e) {
            logger.error("商业版审计日志国际化注册失败", e);
            return ResultVo.error("500", "审计日志国际化注册失败: " + e.getMessage());
        }
    }
}



