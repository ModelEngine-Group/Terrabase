package com.terrabase.enterprise.api.dto;

import java.util.List;

/**
 * 权限信息对象
 * 用于权限注册功能的数据传输对象
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
public class AuthorityInfos {
    
    /**
     * 权限列表
     */
    private List<AuthorityInfo> authorityList;
    
    /**
     * 应用场景
     */
    private String applicationScenario;
    
    public AuthorityInfos() {}
    
    public AuthorityInfos(List<AuthorityInfo> authorityList) {
        this.authorityList = authorityList;
    }
    
    public List<AuthorityInfo> getAuthorityList() {
        return authorityList;
    }
    
    public void setAuthorityList(List<AuthorityInfo> authorityList) {
        this.authorityList = authorityList;
    }
    
    public String getApplicationScenario() {
        return applicationScenario;
    }
    
    public void setApplicationScenario(String applicationScenario) {
        this.applicationScenario = applicationScenario;
    }
    
    @Override
    public String toString() {
        return "AuthorityInfos{" +
                "authorityList=" + authorityList +
                ", applicationScenario='" + applicationScenario + '\'' +
                '}';
    }
    
    /**
     * 单个权限信息
     */
    public static class AuthorityInfo {
        
        /**
         * 权限ID
         */
        private String authorityId;
        
        /**
         * 权限名称
         */
        private String authorityName;
        
        /**
         * 权限描述
         */
        private String authorityDescription;
        
        /**
         * 权限类型
         */
        private String authorityType;
        
        /**
         * 资源路径
         */
        private String resourcePath;
        
        /**
         * 操作类型
         */
        private String operationType;
        
        public AuthorityInfo() {}
        
        public AuthorityInfo(String authorityId, String authorityName, String authorityDescription) {
            this.authorityId = authorityId;
            this.authorityName = authorityName;
            this.authorityDescription = authorityDescription;
        }
        
        public String getAuthorityId() {
            return authorityId;
        }
        
        public void setAuthorityId(String authorityId) {
            this.authorityId = authorityId;
        }
        
        public String getAuthorityName() {
            return authorityName;
        }
        
        public void setAuthorityName(String authorityName) {
            this.authorityName = authorityName;
        }
        
        public String getAuthorityDescription() {
            return authorityDescription;
        }
        
        public void setAuthorityDescription(String authorityDescription) {
            this.authorityDescription = authorityDescription;
        }
        
        public String getAuthorityType() {
            return authorityType;
        }
        
        public void setAuthorityType(String authorityType) {
            this.authorityType = authorityType;
        }
        
        public String getResourcePath() {
            return resourcePath;
        }
        
        public void setResourcePath(String resourcePath) {
            this.resourcePath = resourcePath;
        }
        
        public String getOperationType() {
            return operationType;
        }
        
        public void setOperationType(String operationType) {
            this.operationType = operationType;
        }
        
        @Override
        public String toString() {
            return "AuthorityInfo{" +
                    "authorityId='" + authorityId + '\'' +
                    ", authorityName='" + authorityName + '\'' +
                    ", authorityDescription='" + authorityDescription + '\'' +
                    ", authorityType='" + authorityType + '\'' +
                    ", resourcePath='" + resourcePath + '\'' +
                    ", operationType='" + operationType + '\'' +
                    '}';
        }
    }
}




