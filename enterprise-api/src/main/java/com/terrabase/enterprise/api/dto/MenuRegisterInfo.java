package com.terrabase.enterprise.api.dto;

import java.util.List;

/**
 * 菜单注册信息对象
 * 用于菜单注册功能的数据传输对象
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
public class MenuRegisterInfo {
    
    /**
     * 菜单列表
     */
    private List<MenuInfo> menuList;
    
    /**
     * 应用场景
     */
    private String applicationScenario;
    
    public MenuRegisterInfo() {}
    
    public MenuRegisterInfo(List<MenuInfo> menuList) {
        this.menuList = menuList;
    }
    
    public List<MenuInfo> getMenuList() {
        return menuList;
    }
    
    public void setMenuList(List<MenuInfo> menuList) {
        this.menuList = menuList;
    }
    
    public String getApplicationScenario() {
        return applicationScenario;
    }
    
    public void setApplicationScenario(String applicationScenario) {
        this.applicationScenario = applicationScenario;
    }
    
    @Override
    public String toString() {
        return "MenuRegisterInfo{" +
                "menuList=" + menuList +
                ", applicationScenario='" + applicationScenario + '\'' +
                '}';
    }
    
    /**
     * 单个菜单信息
     */
    public static class MenuInfo {
        
        /**
         * 菜单ID
         */
        private String menuId;
        
        /**
         * 菜单名称
         */
        private String menuName;
        
        /**
         * 菜单描述
         */
        private String menuDescription;
        
        /**
         * 父菜单ID
         */
        private String parentMenuId;
        
        /**
         * 菜单路径
         */
        private String menuPath;
        
        /**
         * 菜单图标
         */
        private String menuIcon;
        
        /**
         * 菜单排序
         */
        private Integer menuOrder;
        
        /**
         * 菜单类型
         */
        private String menuType;
        
        /**
         * 是否启用
         */
        private Boolean enabled;
        
        public MenuInfo() {}
        
        public MenuInfo(String menuId, String menuName, String menuPath) {
            this.menuId = menuId;
            this.menuName = menuName;
            this.menuPath = menuPath;
        }
        
        public String getMenuId() {
            return menuId;
        }
        
        public void setMenuId(String menuId) {
            this.menuId = menuId;
        }
        
        public String getMenuName() {
            return menuName;
        }
        
        public void setMenuName(String menuName) {
            this.menuName = menuName;
        }
        
        public String getMenuDescription() {
            return menuDescription;
        }
        
        public void setMenuDescription(String menuDescription) {
            this.menuDescription = menuDescription;
        }
        
        public String getParentMenuId() {
            return parentMenuId;
        }
        
        public void setParentMenuId(String parentMenuId) {
            this.parentMenuId = parentMenuId;
        }
        
        public String getMenuPath() {
            return menuPath;
        }
        
        public void setMenuPath(String menuPath) {
            this.menuPath = menuPath;
        }
        
        public String getMenuIcon() {
            return menuIcon;
        }
        
        public void setMenuIcon(String menuIcon) {
            this.menuIcon = menuIcon;
        }
        
        public Integer getMenuOrder() {
            return menuOrder;
        }
        
        public void setMenuOrder(Integer menuOrder) {
            this.menuOrder = menuOrder;
        }
        
        public String getMenuType() {
            return menuType;
        }
        
        public void setMenuType(String menuType) {
            this.menuType = menuType;
        }
        
        public Boolean getEnabled() {
            return enabled;
        }
        
        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }
        
        @Override
        public String toString() {
            return "MenuInfo{" +
                    "menuId='" + menuId + '\'' +
                    ", menuName='" + menuName + '\'' +
                    ", menuDescription='" + menuDescription + '\'' +
                    ", parentMenuId='" + parentMenuId + '\'' +
                    ", menuPath='" + menuPath + '\'' +
                    ", menuIcon='" + menuIcon + '\'' +
                    ", menuOrder=" + menuOrder +
                    ", menuType='" + menuType + '\'' +
                    ", enabled=" + enabled +
                    '}';
        }
    }
}




