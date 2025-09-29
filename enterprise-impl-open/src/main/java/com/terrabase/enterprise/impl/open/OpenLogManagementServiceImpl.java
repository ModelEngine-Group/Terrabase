package com.terrabase.enterprise.impl.open;

import com.terrabase.enterprise.api.LogManagementService;
import com.terrabase.enterprise.api.dto.*;
import com.terrabase.enterprise.impl.open.config.KmcConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 开源版日志管理服务实现
 * 基于开源技术实现日志管理功能
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
@Service
public class OpenLogManagementServiceImpl implements LogManagementService {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenLogManagementServiceImpl.class);

    private final AtomicBoolean running = new AtomicBoolean(false);
    
    @Autowired
    private KmcConfig kmcConfig;
    
    @Override
    public String getServiceName() {
        return "Open Source Log Management Service";
    }
    
    @Override
    public String getServiceVersion() {
        return "1.0.0-open";
    }
    
    @Override
    public String getServiceType() {
        return "open";
    }

    @Override
    public String getHealthStatus() {
        if (!running.get()) {
            return "服务未运行";
        }
        
        return String.format("开源版日志管理服务健康状态: 正常 (版本: %s, 类型: %s)", 
                getServiceVersion(), getServiceType());
    }

    @Override
    public void start() {
        if (running.compareAndSet(false, true)) {
            logger.info("开源版日志管理服务已启动");
        } else {
            logger.warn("开源版日志管理服务已经在运行中");
        }
    }

    @Override
    public void stop() {
        if (running.compareAndSet(true, false)) {
            logger.info("开源版日志管理服务已停止");
        } else {
            logger.warn("开源版日志管理服务已经停止");
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
            logger.info("开源版执行操作日志国际化信息注册，国际化信息数量: {}", logI18NS.getLogI18NList().size());

            for (LogI18NS.LogI18NInfo logI18NInfo : logI18NS.getLogI18NList()) {
                logger.info("开源版操作日志国际化信息注册成功 - 操作代码: {}, 操作名称: {}, 默认语言: {}", 
                        logI18NInfo.getOperationCode(), logI18NInfo.getOperationName(), logI18NInfo.getDefaultLanguage());
            }
            
        } catch (Exception e) {
            logger.error("开源版操作日志国际化信息注册失败: {}", logI18NS, e);
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
            logger.info("开源版执行操作日志上报，日志数量: {}", logs.getLogList().size());

            for (Logs.LogInfo logInfo : logs.getLogList()) {
                logger.info("开源版操作日志上报成功 - 日志ID: {}, 用户ID: {}, 操作类型: {}, 操作描述: {}", 
                        logInfo.getLogId(), logInfo.getUserId(), logInfo.getOperationType(), logInfo.getOperationDescription());
            }
            
        } catch (Exception e) {
            logger.error("开源版操作日志上报失败: {}", logs, e);
        }
    }
}
