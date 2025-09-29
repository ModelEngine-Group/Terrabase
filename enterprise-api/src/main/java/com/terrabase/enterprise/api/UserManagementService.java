package com.terrabase.enterprise.api;

import com.terrabase.enterprise.api.dto.*;

/**
 * 用户管理服务接口
 * 提供角色、权限、菜单等用户管理功能
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
public interface UserManagementService {
    
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

    // ========== 用户注册相关接口 ==========
    
    /**
     * 角色注册接口
     * @param roleRegister 角色注册对象
     */
    void registerRole(RoleRegister roleRegister);
    
    /**
     * 权限注册接口
     * @param authorityInfos 权限信息对象
     */
    void registerAuthority(AuthorityInfos authorityInfos);
    
    /**
     * 菜单注册接口
     * @param menuRegisterInfo 菜单注册信息对象
     */
    void registerMenu(MenuRegisterInfo menuRegisterInfo);
    
    /**
     * 菜单屏蔽注册接口
     * @param forbiddenBody 菜单屏蔽信息对象
     */
    void registerMenuForbidden(ForbiddenBody forbiddenBody);

    // ========== 时间管理相关接口 ==========
    
    /**
     * 时间配置变更事件订阅接口
     * @param subscribe 订阅信息对象
     */
    void subscribeTimeConfigChange(Subscribe subscribe);
}
