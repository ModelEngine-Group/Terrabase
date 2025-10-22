package com.terrabase.enterprise.impl.open;

import com.terrabase.enterprise.api.LogManagementService;
import com.terrabase.enterprise.api.dto.LogI18n;
import com.terrabase.enterprise.api.request.LogAttributeVo;
import com.terrabase.enterprise.api.response.ResultVo;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 开源版日志管理服务实现
 * 基于开源技术实现日志管理功能
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
@Service
public class OpenLogManagementServiceImpl implements LogManagementService {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenLogManagementServiceImpl.class);
    
    @Override
    public ResultVo<Integer> registerLogs(List<LogAttributeVo> logs) {
        if (logs == null || logs.isEmpty()) {
            logger.warn("审计日志对象不能为空");
            return ResultVo.error("400", "审计日志对象不能为空");
        }
        
        try {
            logger.info("开源版执行审计日志上报，日志数量: {}", logs.size());

            // 开源版不记录审计日志，只进行简单的日志输出
            for (LogAttributeVo log : logs) {
                logger.debug("开源版审计日志处理 - 序号: {}, 日志类型: {}, 用户名: {}, 操作: {}, 来源: {}, 终端: {}, 结果: {}", 
                        log.getSn(), log.getLogType(), log.getUsername(), log.getOperation(), 
                        log.getSource(), log.getTerminal(), log.getResult());
            }
            
            return ResultVo.success(logs.size());
            
        } catch (Exception e) {
            logger.error("开源版审计日志上报失败", e);
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
            logger.info("开源版执行审计日志国际化注册，国际化信息数量: {}", logI18ns.size());

            // 开源版不记录审计日志，只进行简单的日志输出
            for (LogI18n logI18n : logI18ns) {
                logger.debug("开源版审计日志国际化处理 - 代码: {}, 语言: {}, 内容: {}", 
                        logI18n.getCode(), logI18n.getLanguage(), logI18n.getContent());
            }
            
            return ResultVo.success(true);
            
        } catch (Exception e) {
            logger.error("开源版审计日志国际化注册失败", e);
            return ResultVo.error("500", "审计日志国际化注册失败: " + e.getMessage());
        }
    }
}



