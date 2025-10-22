package com.terrabase.enterprise.api;

/**
 * 企业级服务接口
 * 定义企业级功能的核心接口，组合各个子服务
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
public interface EnterpriseService {
    
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


}
