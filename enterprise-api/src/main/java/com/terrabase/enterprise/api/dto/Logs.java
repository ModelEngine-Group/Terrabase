package com.terrabase.enterprise.api.dto;

import java.util.List;

/**
 * 操作日志信息对象
 * 用于上报操作日志功能的数据传输对象
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
public class Logs {
    
    /**
     * 日志列表
     */
    private List<LogInfo> logList;
    
    /**
     * 应用场景
     */
    private String applicationScenario;
    
    public Logs() {}
    
    public Logs(List<LogInfo> logList) {
        this.logList = logList;
    }
    
    public List<LogInfo> getLogList() {
        return logList;
    }
    
    public void setLogList(List<LogInfo> logList) {
        this.logList = logList;
    }
    
    public String getApplicationScenario() {
        return applicationScenario;
    }
    
    public void setApplicationScenario(String applicationScenario) {
        this.applicationScenario = applicationScenario;
    }
    
    @Override
    public String toString() {
        return "Logs{" +
                "logList=" + logList +
                ", applicationScenario='" + applicationScenario + '\'' +
                '}';
    }
    
    /**
     * 单个日志信息
     */
    public static class LogInfo {
        
        /**
         * 日志ID
         */
        private String logId;
        
        /**
         * 用户ID
         */
        private String userId;
        
        /**
         * 用户名
         */
        private String userName;
        
        /**
         * 操作类型
         */
        private String operationType;
        
        /**
         * 操作描述
         */
        private String operationDescription;
        
        /**
         * 操作时间
         */
        private Long operationTime;
        
        /**
         * 操作结果
         */
        private String operationResult;
        
        /**
         * IP地址
         */
        private String ipAddress;
        
        /**
         * 用户代理
         */
        private String userAgent;
        
        /**
         * 操作详情
         */
        private String operationDetails;
        
        public LogInfo() {}
        
        public LogInfo(String logId, String userId, String operationType, String operationDescription) {
            this.logId = logId;
            this.userId = userId;
            this.operationType = operationType;
            this.operationDescription = operationDescription;
            this.operationTime = System.currentTimeMillis();
        }
        
        public String getLogId() {
            return logId;
        }
        
        public void setLogId(String logId) {
            this.logId = logId;
        }
        
        public String getUserId() {
            return userId;
        }
        
        public void setUserId(String userId) {
            this.userId = userId;
        }
        
        public String getUserName() {
            return userName;
        }
        
        public void setUserName(String userName) {
            this.userName = userName;
        }
        
        public String getOperationType() {
            return operationType;
        }
        
        public void setOperationType(String operationType) {
            this.operationType = operationType;
        }
        
        public String getOperationDescription() {
            return operationDescription;
        }
        
        public void setOperationDescription(String operationDescription) {
            this.operationDescription = operationDescription;
        }
        
        public Long getOperationTime() {
            return operationTime;
        }
        
        public void setOperationTime(Long operationTime) {
            this.operationTime = operationTime;
        }
        
        public String getOperationResult() {
            return operationResult;
        }
        
        public void setOperationResult(String operationResult) {
            this.operationResult = operationResult;
        }
        
        public String getIpAddress() {
            return ipAddress;
        }
        
        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }
        
        public String getUserAgent() {
            return userAgent;
        }
        
        public void setUserAgent(String userAgent) {
            this.userAgent = userAgent;
        }
        
        public String getOperationDetails() {
            return operationDetails;
        }
        
        public void setOperationDetails(String operationDetails) {
            this.operationDetails = operationDetails;
        }
        
        @Override
        public String toString() {
            return "LogInfo{" +
                    "logId='" + logId + '\'' +
                    ", userId='" + userId + '\'' +
                    ", userName='" + userName + '\'' +
                    ", operationType='" + operationType + '\'' +
                    ", operationDescription='" + operationDescription + '\'' +
                    ", operationTime=" + operationTime +
                    ", operationResult='" + operationResult + '\'' +
                    ", ipAddress='" + ipAddress + '\'' +
                    ", userAgent='" + userAgent + '\'' +
                    ", operationDetails='" + operationDetails + '\'' +
                    '}';
        }
    }
}




