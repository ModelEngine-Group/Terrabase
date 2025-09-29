package com.terrabase.enterprise.api;

import com.terrabase.enterprise.api.dto.*;
import java.util.List;

/**
 * 监控告警服务接口
 * 提供告警定义注册、告警上报、告警查询等功能
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
public interface MonitoringService {
    
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

    // ========== 告警系统相关接口 ==========
    
    /**
     * 注册告警定义接口
     * @param eventDefine 告警定义对象
     */
    void registerEventDefine(EventDefine eventDefine);
    
    /**
     * 告警上报接口
     * @param alarms 告警信息列表
     */
    void sendAlarmsBatch(List<AlarmInfo> alarms);
    
    /**
     * 分页查询告警接口
     * @param queryParams 查询参数
     * @return 告警信息分页结果
     */
    PageResult<EventInfo> getEventsByPage(EventQueryParams queryParams);
    
    /**
     * 根据ID查询告警详情接口
     * @param alarmId 告警ID
     * @return 告警详细信息
     */
    AlarmDetail getAlarmDetailById(String alarmId);
}
