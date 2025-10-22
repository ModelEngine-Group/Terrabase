package com.terrabase.enterprise.impl.commercial;

import com.terrabase.enterprise.api.UserManagementService;
import com.terrabase.enterprise.api.dto.AuthorityInfo;
import com.terrabase.enterprise.api.dto.LoginUserDto;
import com.terrabase.enterprise.api.dto.ResourceGroup;
import com.terrabase.enterprise.api.request.RoleRegisterVo;
import com.terrabase.enterprise.api.response.ResultVo;
import com.terrabase.enterprise.impl.commercial.client.ManualAuthenticationClient;
import com.terrabase.enterprise.impl.commercial.client.OmsExtensionClient;
import com.terrabase.enterprise.impl.commercial.client.PermissionFeignClient;
import com.terrabase.enterprise.impl.commercial.client.RoleFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商业版用户管理服务实现
 * 集成商业组件实现用户管理功能
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
@Service
public class CommercialUserManagementServiceImpl implements UserManagementService {
    
    private static final Logger logger = LoggerFactory.getLogger(CommercialUserManagementServiceImpl.class);
    
    @Autowired
    private OmsExtensionClient omsExtensionClient;
    
    @Autowired
    private ManualAuthenticationClient manualAuthenticationClient;
    
    @Autowired
    private RoleFeignClient roleFeignClient;

    @Autowired
    private PermissionFeignClient permissionFeignClient;

    // ========== 用户注册相关接口实现 ==========

    @Override
    public void batchRegisterRole(RoleRegisterVo roleRegister) {
        if (roleRegister == null) {
            logger.warn("角色注册对象不能为空");
            return;
        }

        try {
            logger.info("商业版执行批量角色注册");
            
            // 使用 RoleFeignClient 调用远程服务进行批量角色注册
            ResultVo<String> result = roleFeignClient.batchRegisterRole(roleRegister);
            
            if ("200".equals(result.getCode())) {
                logger.info("商业版批量角色注册成功");
                
                // 记录角色注册信息
                if (roleRegister.getRoleRegisterInfos() != null && !roleRegister.getRoleRegisterInfos().isEmpty()) {
                    logger.info("商业版处理角色注册信息，数量: {}", roleRegister.getRoleRegisterInfos().size());
                    
                    for (com.terrabase.enterprise.api.dto.RoleRegisterInfo roleInfo : roleRegister.getRoleRegisterInfos()) {
                        logger.info("商业版角色注册成功 - 角色名: {}, 角色名代码: {}, 描述: {}, 可创建: {}, 支持登录类型: {}", 
                                roleInfo.getName(), 
                                roleInfo.getNameCode(), 
                                roleInfo.getDescription(),
                                roleInfo.isCreatable(),
                                roleInfo.getSupportLoginType());
                    }
                }
                
                // 记录角色国际化信息
                if (roleRegister.getRoleI18nInfos() != null && !roleRegister.getRoleI18nInfos().isEmpty()) {
                    logger.info("商业版处理角色国际化信息，数量: {}", roleRegister.getRoleI18nInfos().size());
                    
                    for (com.terrabase.enterprise.api.dto.RoleI18nInfo i18nInfo : roleRegister.getRoleI18nInfos()) {
                        logger.info("商业版角色国际化信息 - 角色名: {}, 代码: {}, 语言: {}, 内容: {}", 
                                i18nInfo.getName(), 
                                i18nInfo.getCode(), 
                                i18nInfo.getLanguage(),
                                i18nInfo.getContent());
                    }
                }
            } else {
                logger.error("商业版批量角色注册失败: {}", result.getMsg());
            }

        } catch (Exception e) {
            logger.error("商业版批量角色注册失败: {}", roleRegister, e);
        }
    }

    @Override
    public void registerPermission(List<AuthorityInfo> authorityInfos) {
        if (authorityInfos == null || authorityInfos.isEmpty()) {
            logger.warn("权限注册列表不能为空");
            return;
        }

        try {
            logger.info("商业版执行批量权限注册，权限数量: {}", authorityInfos.size());
            
            // 调用Feign客户端进行批量权限注册
            ResultVo<String> result = permissionFeignClient.registerPermission(authorityInfos);
            
            if ("200".equals(result.getCode())) {
                logger.info("商业版批量权限注册成功，权限数量: {}", authorityInfos.size());
                for (AuthorityInfo authorityInfo : authorityInfos) {
                    logger.info("商业版权限注册成功 - 资源标识: {}, 描述: {}, 跳过检查: {}, 所需角色: {}",
                            authorityInfo.getResourceKey(), 
                            authorityInfo.getDescription(), 
                            authorityInfo.isSkipCheck(),
                            authorityInfo.getRoles());
                }
            } else {
                logger.error("商业版批量权限注册失败: {}", result.getMsg());
            }
            
        } catch (Exception e) {
            logger.error("商业版批量权限注册失败: {}", authorityInfos, e);
        }
    }
    
    @Override
    public List<ResourceGroup> getUserGroups(String userName) {
        if (userName == null || userName.trim().isEmpty()) {
            logger.warn("用户名不能为空");
            return new ArrayList<>();
        }
        
        try {
            logger.info("商业版获取用户资源组: {}", userName);
            
            // 使用FeignClient调用OMS扩展服务获取用户资源组
            List<ResourceGroup> groups = omsExtensionClient.getUserGroups(userName);
            
            logger.info("商业版获取用户资源组成功 - 用户: {}, 资源组数量: {}", userName, groups.size());
            
            return groups;
            
        } catch (Exception e) {
            logger.error("商业版获取用户资源组失败 - 用户: {}", userName, e);
            // 发生异常时返回默认的公共资源组
            List<ResourceGroup> defaultGroups = new ArrayList<>();
            defaultGroups.add(ResourceGroup.buildPublicGroup());
            return defaultGroups;
        }
    }
    
    // ========== 用户认证相关接口实现 ==========
    
    @Override
    public ResultVo<List<String>> queryRolesByToken() {
        try {
            logger.info("商业版执行根据token查询角色名");
            
            // 使用 Feign 客户端调用远程服务
            ResultVo<List<String>> result = manualAuthenticationClient.queryRolesByToken();
            
            if ("200".equals(result.getCode())) {
                logger.info("商业版根据token查询角色名成功，角色数量: {}", result.getData().size());
                return result;
            } else {
                logger.error("商业版根据token查询角色名失败: {}", result.getMsg());
                return ResultVo.error(result.getCode(), result.getMsg());
            }
            
        } catch (Exception e) {
            logger.error("商业版根据token查询角色名失败", e);
            return ResultVo.error("500", "角色查询失败: " + e.getMessage());
        }
    }
    
    @Override
    public ResultVo<List<LoginUserDto>> getCurrentUserInfo() {
        try {
            logger.info("商业版执行获取当前用户信息");
            
            // 使用 Feign 客户端调用远程服务
            ResultVo<List<LoginUserDto>> result = manualAuthenticationClient.sessionCur();
            
            if ("200".equals(result.getCode())) {
                logger.info("商业版获取当前用户信息成功，用户数量: {}", result.getData().size());
                return result;
            } else {
                logger.error("商业版获取当前用户信息失败: {}", result.getMsg());
                return ResultVo.error(result.getCode(), result.getMsg());
            }
            
        } catch (Exception e) {
            logger.error("商业版获取当前用户信息失败", e);
            return ResultVo.error("500", "用户信息查询失败: " + e.getMessage());
        }
    }
}
