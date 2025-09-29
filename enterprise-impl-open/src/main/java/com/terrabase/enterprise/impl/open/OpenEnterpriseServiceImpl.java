package com.terrabase.enterprise.impl.open;

import com.terrabase.enterprise.api.EnterpriseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 开源版企业服务实现
 * 提供企业级服务的基本功能，子服务通过JarLoadUtil独立管理
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
@Service
public class OpenEnterpriseServiceImpl implements EnterpriseService {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenEnterpriseServiceImpl.class);

    private final AtomicBoolean running = new AtomicBoolean(false);
    
    @Override
    public String getServiceName() {
        return "Open Source Enterprise Service";
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
        
        return String.format("开源版企业服务健康状态: 正常 (版本: %s, 类型: %s)", 
                getServiceVersion(), getServiceType());
    }

    @Override
    public void start() {
        if (running.compareAndSet(false, true)) {
            logger.info("开源版企业服务已启动");
            // 注意：子服务现在通过JarLoadUtil独立管理，不再在此处启动
        } else {
            logger.warn("开源版企业服务已经在运行中");
        }
    }

    @Override
    public void stop() {
        if (running.compareAndSet(true, false)) {
            logger.info("开源版企业服务已停止");
            // 注意：子服务现在通过JarLoadUtil独立管理，不再在此处停止
        } else {
            logger.warn("开源版企业服务已经停止");
        }
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }
}