package com.terrabase.enterprise.api;

import com.terrabase.enterprise.api.dto.*;

/**
 * 日志管理服务接口
 * 提供操作日志的国际化注册和上报功能
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
public interface LogManagementService {
    
    /**
     * 获取服务名称
     * @return 服务名称
     */
    String getServiceName();
    
    /**
     * 获取服务版本
     * @return 服务版本
     */
    String getServiceVersion();
    
    /**
     * 获取服务类型（commercial 或 open）
     * @return 服务类型
     */
    String getServiceType();

    /**
     * 获取服务健康状态
     * @return 健康状态信息
     */
    String getHealthStatus();

    /**
     * 启动服务
     * 将服务的running状态设置为true
     */
    void start();

    /**
     * 停止服务
     * 将服务的running状态设置为false
     */
    void stop();

    /**
     * 检查服务是否正在运行
     * @return true如果服务正在运行
     */
    boolean isRunning();

    // ========== 日志与监控相关接口 ==========
    
    /**
     * 操作日志国际化信息注册接口
     * @param logI18NS 国际化日志信息对象
     */
    void registerOperateLogI18N(LogI18NS logI18NS);
    
    /**
     * 上报操作日志接口
     * @param logs 操作日志信息对象
     */
    void reportOperateLog(Logs logs);
}
