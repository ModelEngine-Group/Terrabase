package com.terrabase.enterprise.api.dto;

/**
 * 告警定义对象
 * 用于注册告警定义功能的数据传输对象
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
public class EventDefine {
    
    /**
     * 告警定义ID
     */
    private String eventDefineId;
    
    /**
     * 告警名称
     */
    private String eventName;
    
    /**
     * 告警描述
     */
    private String eventDescription;
    
    /**
     * 告警类型
     */
    private String eventType;
    
    /**
     * 告警级别
     */
    private String eventLevel;
    
    /**
     * 告警分类
     */
    private String eventCategory;
    
    /**
     * 触发条件
     */
    private String triggerCondition;
    
    /**
     * 告警模板
     */
    private String eventTemplate;
    
    /**
     * 是否启用
     */
    private Boolean enabled;
    
    public EventDefine() {}
    
    public EventDefine(String eventDefineId, String eventName, String eventDescription) {
        this.eventDefineId = eventDefineId;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
    }
    
    public String getEventDefineId() {
        return eventDefineId;
    }
    
    public void setEventDefineId(String eventDefineId) {
        this.eventDefineId = eventDefineId;
    }
    
    public String getEventName() {
        return eventName;
    }
    
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    
    public String getEventDescription() {
        return eventDescription;
    }
    
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
    
    public String getEventType() {
        return eventType;
    }
    
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    
    public String getEventLevel() {
        return eventLevel;
    }
    
    public void setEventLevel(String eventLevel) {
        this.eventLevel = eventLevel;
    }
    
    public String getEventCategory() {
        return eventCategory;
    }
    
    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }
    
    public String getTriggerCondition() {
        return triggerCondition;
    }
    
    public void setTriggerCondition(String triggerCondition) {
        this.triggerCondition = triggerCondition;
    }
    
    public String getEventTemplate() {
        return eventTemplate;
    }
    
    public void setEventTemplate(String eventTemplate) {
        this.eventTemplate = eventTemplate;
    }
    
    public Boolean getEnabled() {
        return enabled;
    }
    
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
    @Override
    public String toString() {
        return "EventDefine{" +
                "eventDefineId='" + eventDefineId + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventDescription='" + eventDescription + '\'' +
                ", eventType='" + eventType + '\'' +
                ", eventLevel='" + eventLevel + '\'' +
                ", eventCategory='" + eventCategory + '\'' +
                ", triggerCondition='" + triggerCondition + '\'' +
                ", eventTemplate='" + eventTemplate + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}




