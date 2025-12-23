package com.terrabase.enterprise.impl.open;

import com.terrabase.enterprise.api.UserManagementService;
import com.terrabase.enterprise.api.dto.AuthorityInfo;
import com.terrabase.enterprise.api.dto.LoginUserDto;
import com.terrabase.enterprise.api.dto.ResourceGroup;
import com.terrabase.enterprise.api.request.RoleRegisterVo;
import com.terrabase.enterprise.api.response.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 开源版用户管理服务实现
 * 基于开源技术实现用户管理功能
 *
 * @author Yehong Pan
 * @version 1.0.0
 */
@Service
public class OpenUserManagementServiceImpl implements UserManagementService {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenUserManagementServiceImpl.class);

    // ========== 用户注册相关接口实现 ==========
    
    @Override
    public void batchRegisterRole(RoleRegisterVo roleRegister) {
        if (roleRegister == null) {
            logger.warn("角色注册对象不能为空");
            return;
        }
        
        try {
            logger.info("开源版执行批量角色注册");
            
            // 处理角色注册信息列表
            if (roleRegister.getRoleRegisterInfos() != null && !roleRegister.getRoleRegisterInfos().isEmpty()) {
                logger.info("开源版处理角色注册信息，数量: {}", roleRegister.getRoleRegisterInfos().size());
                
                for (com.terrabase.enterprise.api.dto.RoleRegisterInfo roleInfo : roleRegister.getRoleRegisterInfos()) {
                    logger.info("开源版角色注册成功 - 角色名: {}, 角色名代码: {}, 描述: {}, 可创建: {}, 支持登录类型: {}", 
                            roleInfo.getName(), 
                            roleInfo.getNameCode(), 
                            roleInfo.getDescription(),
                            roleInfo.isCreatable(),
                            roleInfo.getSupportLoginType());
                }
            }
            
            // 处理角色国际化信息列表
            if (roleRegister.getRoleI18nInfos() != null && !roleRegister.getRoleI18nInfos().isEmpty()) {
                logger.info("开源版处理角色国际化信息，数量: {}", roleRegister.getRoleI18nInfos().size());
                
                for (com.terrabase.enterprise.api.dto.RoleI18nInfo i18nInfo : roleRegister.getRoleI18nInfos()) {
                    logger.info("开源版角色国际化信息 - 角色名: {}, 代码: {}, 语言: {}, 内容: {}", 
                            i18nInfo.getName(), 
                            i18nInfo.getCode(), 
                            i18nInfo.getLanguage(),
                            i18nInfo.getContent());
                }
            }

        } catch (Exception e) {
            logger.error("开源版批量角色注册失败: {}", roleRegister, e);
        }
    }
    
    @Override
    public void registerPermission(List<AuthorityInfo> authorityInfos) {
        if (authorityInfos == null || authorityInfos.isEmpty()) {
            logger.warn("权限注册列表不能为空");
            return;
        }
        
        try {
            logger.info("开源版执行批量权限注册，权限数量: {}", authorityInfos.size());

            for (AuthorityInfo authorityInfo : authorityInfos) {
                logger.info("开源版权限注册成功 - 资源标识: {}, 描述: {}, 跳过检查: {}, 所需角色: {}", 
                        authorityInfo.getResourceKey(), 
                        authorityInfo.getDescription(), 
                        authorityInfo.isSkipCheck(),
                        authorityInfo.getRoles());
            }

        } catch (Exception e) {
            logger.error("开源版批量权限注册失败: {}", authorityInfos, e);
        }
    }
    
    @Override
    public List<ResourceGroup> getUserGroups(String userName) {
        if (userName == null || userName.trim().isEmpty()) {
            logger.warn("用户名不能为空");
            return new ArrayList<>();
        }
        
        try {
            logger.info("开源版获取用户资源组: {}", userName);
            
            // 开源版实现：返回默认的公共资源组
            List<ResourceGroup> groups = new ArrayList<>();
            groups.add(ResourceGroup.buildPublicGroup());
            
            logger.info("开源版获取用户资源组成功 - 用户: {}, 资源组数量: {}", userName, groups.size());
            
            return groups;
            
        } catch (Exception e) {
            logger.error("开源版获取用户资源组失败 - 用户: {}", userName, e);
            return new ArrayList<>();
        }
    }

    // ========== 用户认证相关接口实现 ==========
    
    @Override
    public ResultVo<List<String>> queryRolesByToken() {
        try {
            logger.info("开源版执行根据token查询角色名");
            
            // 开源版实现：返回模拟的角色数据
            List<String> roles = new java.util.ArrayList<>();
            roles.add("admin");
            roles.add("user");
            roles.add("operator");
            
            logger.info("开源版根据token查询角色名成功，角色数量: {}", roles.size());
            return ResultVo.success(roles);
            
        } catch (Exception e) {
            logger.error("开源版根据token查询角色名失败", e);
            return ResultVo.error("500", "角色查询失败: " + e.getMessage());
        }
    }
    
    @Override
    public ResultVo<List<LoginUserDto>> getCurrentUserInfo() {
        try {
            logger.info("开源版执行获取当前用户信息");
            
            // 开源版实现：返回模拟的用户信息
            List<LoginUserDto> users = new java.util.ArrayList<>();
            
            LoginUserDto user = new LoginUserDto();
            user.setUserName("admin");
            user.setUserId("admin_001");
            user.setRole("admin");
            
            // 设置资源组
            List<ResourceGroup> resourceGroups = new java.util.ArrayList<>();
            resourceGroups.add(ResourceGroup.buildPublicGroup());
            user.setResourceGroups(resourceGroups);
            users.add(user);
            
            logger.info("开源版获取当前用户信息成功，用户数量: {}", users.size());
            return ResultVo.success(users);
            
        } catch (Exception e) {
            logger.error("开源版获取当前用户信息失败", e);
            return ResultVo.error("500", "用户信息查询失败: " + e.getMessage());
        }
    }
}



