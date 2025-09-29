package com.terrabase.enterprise.impl.open;

import com.terrabase.enterprise.api.UserManagementService;
import com.terrabase.enterprise.api.dto.*;
import com.terrabase.enterprise.impl.open.config.KmcConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 开源版用户管理服务实现
 * 基于开源技术实现用户管理功能
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
@Service
public class OpenUserManagementServiceImpl implements UserManagementService {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenUserManagementServiceImpl.class);

    private final AtomicBoolean running = new AtomicBoolean(false);
    
    @Autowired
    private KmcConfig kmcConfig;
    
    @Override
    public String getServiceName() {
        return "Open Source User Management Service";
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
        
        return String.format("开源版用户管理服务健康状态: 正常 (版本: %s, 类型: %s)", 
                getServiceVersion(), getServiceType());
    }

    @Override
    public void start() {
        if (running.compareAndSet(false, true)) {
            logger.info("开源版用户管理服务已启动");
        } else {
            logger.warn("开源版用户管理服务已经在运行中");
        }
    }

    @Override
    public void stop() {
        if (running.compareAndSet(true, false)) {
            logger.info("开源版用户管理服务已停止");
        } else {
            logger.warn("开源版用户管理服务已经停止");
        }
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    // ========== 用户注册相关接口实现 ==========
    
    @Override
    public void registerRole(RoleRegister roleRegister) {
        if (!running.get()) {
            logger.warn("服务未运行，无法执行角色注册操作");
            return;
        }
        
        if (roleRegister == null) {
            logger.warn("角色注册对象不能为空");
            return;
        }
        
        try {
            logger.info("开源版执行角色注册: {}", roleRegister);

            logger.info("开源版角色注册成功 - 角色ID: {}, 角色名称: {}, 应用场景: {}", 
                    roleRegister.getRoleId(), roleRegister.getRoleName(), roleRegister.getApplicationScenario());
            
            // 记录审计日志
            if (kmcConfig.isEnableAuditLog()) {
                logger.info("角色注册审计 - 角色ID: {}, 角色名称: {}, 角色类型: {}", 
                        roleRegister.getRoleId(), roleRegister.getRoleName(), roleRegister.getRoleType());
            }
            
        } catch (Exception e) {
            logger.error("开源版角色注册失败: {}", roleRegister, e);
        }
    }
    
    @Override
    public void registerAuthority(AuthorityInfos authorityInfos) {
        if (!running.get()) {
            logger.warn("服务未运行，无法执行权限注册操作");
            return;
        }
        
        if (authorityInfos == null || authorityInfos.getAuthorityList() == null) {
            logger.warn("权限注册对象不能为空");
            return;
        }
        
        try {
            logger.info("开源版执行权限注册，权限数量: {}", authorityInfos.getAuthorityList().size());

            for (AuthorityInfos.AuthorityInfo authorityInfo : authorityInfos.getAuthorityList()) {
                logger.info("开源版权限注册成功 - 权限ID: {}, 权限名称: {}, 权限类型: {}", 
                        authorityInfo.getAuthorityId(), authorityInfo.getAuthorityName(), authorityInfo.getAuthorityType());
            }
            
            // 记录审计日志
            if (kmcConfig.isEnableAuditLog()) {
                logger.info("权限注册审计 - 权限数量: {}, 应用场景: {}", 
                        authorityInfos.getAuthorityList().size(), authorityInfos.getApplicationScenario());
            }
            
        } catch (Exception e) {
            logger.error("开源版权限注册失败: {}", authorityInfos, e);
        }
    }
    
    @Override
    public void registerMenu(MenuRegisterInfo menuRegisterInfo) {
        if (!running.get()) {
            logger.warn("服务未运行，无法执行菜单注册操作");
            return;
        }
        
        if (menuRegisterInfo == null || menuRegisterInfo.getMenuList() == null) {
            logger.warn("菜单注册对象不能为空");
            return;
        }
        
        try {
            logger.info("开源版执行菜单注册，菜单数量: {}", menuRegisterInfo.getMenuList().size());

            for (MenuRegisterInfo.MenuInfo menuInfo : menuRegisterInfo.getMenuList()) {
                logger.info("开源版菜单注册成功 - 菜单ID: {}, 菜单名称: {}, 菜单路径: {}", 
                        menuInfo.getMenuId(), menuInfo.getMenuName(), menuInfo.getMenuPath());
            }
            
            // 记录审计日志
            if (kmcConfig.isEnableAuditLog()) {
                logger.info("菜单注册审计 - 菜单数量: {}, 应用场景: {}", 
                        menuRegisterInfo.getMenuList().size(), menuRegisterInfo.getApplicationScenario());
            }
            
        } catch (Exception e) {
            logger.error("开源版菜单注册失败: {}", menuRegisterInfo, e);
        }
    }
    
    @Override
    public void registerMenuForbidden(ForbiddenBody forbiddenBody) {
        if (!running.get()) {
            logger.warn("服务未运行，无法执行菜单屏蔽注册操作");
            return;
        }
        
        if (forbiddenBody == null || forbiddenBody.getForbiddenMenuIds() == null) {
            logger.warn("菜单屏蔽对象不能为空");
            return;
        }
        
        try {
            logger.info("开源版执行菜单屏蔽注册，屏蔽菜单数量: {}", forbiddenBody.getForbiddenMenuIds().size());

            for (String menuId : forbiddenBody.getForbiddenMenuIds()) {
                logger.info("开源版菜单屏蔽注册成功 - 菜单ID: {}, 屏蔽原因: {}", menuId, forbiddenBody.getReason());
            }
            
            // 记录审计日志
            if (kmcConfig.isEnableAuditLog()) {
                logger.info("菜单屏蔽注册审计 - 屏蔽菜单数量: {}, 屏蔽原因: {}, 是否永久屏蔽: {}", 
                        forbiddenBody.getForbiddenMenuIds().size(), forbiddenBody.getReason(), forbiddenBody.getPermanent());
            }
            
        } catch (Exception e) {
            logger.error("开源版菜单屏蔽注册失败: {}", forbiddenBody, e);
        }
    }

    // ========== 时间管理相关接口实现 ==========
    
    @Override
    public void subscribeTimeConfigChange(Subscribe subscribe) {
        if (!running.get()) {
            logger.warn("服务未运行，无法执行时间配置变更事件订阅操作");
            return;
        }
        
        if (subscribe == null) {
            logger.warn("订阅信息对象不能为空");
            return;
        }
        
        try {
            logger.info("开源版执行时间配置变更事件订阅: {}", subscribe);

            logger.info("开源版时间配置变更事件订阅成功 - 订阅ID: {}, 服务名称: {}, 通知地址: {}", 
                    subscribe.getSubscribeId(), subscribe.getServiceName(), subscribe.getNotifyAddress());
            
        } catch (Exception e) {
            logger.error("开源版时间配置变更事件订阅失败: {}", subscribe, e);
        }
    }
}
