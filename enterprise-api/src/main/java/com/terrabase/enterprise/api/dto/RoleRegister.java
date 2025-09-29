package com.terrabase.enterprise.api.dto;

/**
 * 角色注册对象
 * 用于角色注册功能的数据传输对象
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
public class RoleRegister {
    
    /**
     * 角色ID
     */
    private String roleId;
    
    /**
     * 角色名称
     */
    private String roleName;
    
    /**
     * 角色描述
     */
    private String roleDescription;
    
    /**
     * 角色类型
     */
    private String roleType;
    
    /**
     * 权限列表
     */
    private String[] permissions;
    
    /**
     * 应用场景
     */
    private String applicationScenario;
    
    public RoleRegister() {}
    
    public RoleRegister(String roleId, String roleName, String roleDescription) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleDescription = roleDescription;
    }
    
    public String getRoleId() {
        return roleId;
    }
    
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
    
    public String getRoleName() {
        return roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public String getRoleDescription() {
        return roleDescription;
    }
    
    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }
    
    public String getRoleType() {
        return roleType;
    }
    
    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }
    
    public String[] getPermissions() {
        return permissions;
    }
    
    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }
    
    public String getApplicationScenario() {
        return applicationScenario;
    }
    
    public void setApplicationScenario(String applicationScenario) {
        this.applicationScenario = applicationScenario;
    }
    
    @Override
    public String toString() {
        return "RoleRegister{" +
                "roleId='" + roleId + '\'' +
                ", roleName='" + roleName + '\'' +
                ", roleDescription='" + roleDescription + '\'' +
                ", roleType='" + roleType + '\'' +
                ", applicationScenario='" + applicationScenario + '\'' +
                '}';
    }
}




