package com.terrabase.enterprise.api;

import com.terrabase.enterprise.api.dto.AuthorityInfo;
import com.terrabase.enterprise.api.dto.LoginUserDto;
import com.terrabase.enterprise.api.dto.ResourceGroup;
import com.terrabase.enterprise.api.request.RoleRegisterVo;
import com.terrabase.enterprise.api.response.ResultVo;

import java.util.List;

/**
 * 用户管理服务接口
 * 提供角色、权限、菜单等用户管理功能
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
public interface UserManagementService {

    // ========== 用户注册相关接口 ==========
    
    /**
     * 批量角色注册接口
     * @param roleRegister 角色注册对象
     */
    void batchRegisterRole(RoleRegisterVo roleRegister);
    
    /**
     * 批量权限注册接口
     * @param authorityInfos 权限信息列表
     */
    void registerPermission(List<AuthorityInfo> authorityInfos);
    
    /**
     * 获取用户资源组列表
     * @param userName 用户名
     * @return 用户资源组列表
     */
    List<ResourceGroup> getUserGroups(String userName);

    // ========== 用户认证相关接口 ==========
    
    /**
     * 根据token查询角色名接口
     * @return 角色名列表结果对象
     */
    ResultVo<List<String>> queryRolesByToken();
    
    /**
     * 获取当前用户信息接口
     * @return 当前用户信息结果对象
     */
    ResultVo<List<LoginUserDto>> getCurrentUserInfo();
}
