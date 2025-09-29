package com.terrabase.enterprise.api.dto;

import java.util.List;
import java.util.Map;

/**
 * 国际化日志信息对象
 * 用于操作日志国际化信息注册功能的数据传输对象
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
public class LogI18NS {
    
    /**
     * 日志国际化信息列表
     */
    private List<LogI18NInfo> logI18NList;
    
    /**
     * 应用场景
     */
    private String applicationScenario;
    
    public LogI18NS() {}
    
    public LogI18NS(List<LogI18NInfo> logI18NList) {
        this.logI18NList = logI18NList;
    }
    
    public List<LogI18NInfo> getLogI18NList() {
        return logI18NList;
    }
    
    public void setLogI18NList(List<LogI18NInfo> logI18NList) {
        this.logI18NList = logI18NList;
    }
    
    public String getApplicationScenario() {
        return applicationScenario;
    }
    
    public void setApplicationScenario(String applicationScenario) {
        this.applicationScenario = applicationScenario;
    }
    
    @Override
    public String toString() {
        return "LogI18NS{" +
                "logI18NList=" + logI18NList +
                ", applicationScenario='" + applicationScenario + '\'' +
                '}';
    }
    
    /**
     * 单个日志国际化信息
     */
    public static class LogI18NInfo {
        
        /**
         * 操作代码
         */
        private String operationCode;
        
        /**
         * 操作名称
         */
        private String operationName;
        
        /**
         * 国际化信息映射 (语言代码 -> 描述)
         */
        private Map<String, String> i18nMessages;
        
        /**
         * 默认语言
         */
        private String defaultLanguage;
        
        public LogI18NInfo() {}
        
        public LogI18NInfo(String operationCode, String operationName) {
            this.operationCode = operationCode;
            this.operationName = operationName;
        }
        
        public String getOperationCode() {
            return operationCode;
        }
        
        public void setOperationCode(String operationCode) {
            this.operationCode = operationCode;
        }
        
        public String getOperationName() {
            return operationName;
        }
        
        public void setOperationName(String operationName) {
            this.operationName = operationName;
        }
        
        public Map<String, String> getI18nMessages() {
            return i18nMessages;
        }
        
        public void setI18nMessages(Map<String, String> i18nMessages) {
            this.i18nMessages = i18nMessages;
        }
        
        public String getDefaultLanguage() {
            return defaultLanguage;
        }
        
        public void setDefaultLanguage(String defaultLanguage) {
            this.defaultLanguage = defaultLanguage;
        }
        
        @Override
        public String toString() {
            return "LogI18NInfo{" +
                    "operationCode='" + operationCode + '\'' +
                    ", operationName='" + operationName + '\'' +
                    ", i18nMessages=" + i18nMessages +
                    ", defaultLanguage='" + defaultLanguage + '\'' +
                    '}';
        }
    }
}




