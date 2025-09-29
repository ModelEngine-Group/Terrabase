package com.terrabase.enterprise.api.dto;

/**
 * 告警详细信息对象
 * 用于查询告警详情功能的数据传输对象
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
public class AlarmDetail {
    
    /**
     * 告警ID
     */
    private String alarmId;
    
    /**
     * 告警定义ID
     */
    private String eventDefineId;
    
    /**
     * 告警名称
     */
    private String alarmName;
    
    /**
     * 告警描述
     */
    private String alarmDescription;
    
    /**
     * 告警级别
     */
    private String alarmLevel;
    
    /**
     * 告警状态
     */
    private String alarmStatus;
    
    /**
     * 触发时间
     */
    private Long triggerTime;
    
    /**
     * 告警来源
     */
    private String alarmSource;
    
    /**
     * 告警详情
     */
    private String alarmDetails;
    
    /**
     * 告警数据
     */
    private String alarmData;
    
    /**
     * 处理时间
     */
    private Long handleTime;
    
    /**
     * 处理人
     */
    private String handler;
    
    /**
     * 处理备注
     */
    private String handleRemark;
    
    /**
     * 创建时间
     */
    private Long createTime;
    
    /**
     * 更新时间
     */
    private Long updateTime;
    
    public AlarmDetail() {}
    
    public AlarmDetail(String alarmId, String alarmName, String alarmLevel) {
        this.alarmId = alarmId;
        this.alarmName = alarmName;
        this.alarmLevel = alarmLevel;
        this.createTime = System.currentTimeMillis();
    }
    
    public String getAlarmId() {
        return alarmId;
    }
    
    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }
    
    public String getEventDefineId() {
        return eventDefineId;
    }
    
    public void setEventDefineId(String eventDefineId) {
        this.eventDefineId = eventDefineId;
    }
    
    public String getAlarmName() {
        return alarmName;
    }
    
    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }
    
    public String getAlarmDescription() {
        return alarmDescription;
    }
    
    public void setAlarmDescription(String alarmDescription) {
        this.alarmDescription = alarmDescription;
    }
    
    public String getAlarmLevel() {
        return alarmLevel;
    }
    
    public void setAlarmLevel(String alarmLevel) {
        this.alarmLevel = alarmLevel;
    }
    
    public String getAlarmStatus() {
        return alarmStatus;
    }
    
    public void setAlarmStatus(String alarmStatus) {
        this.alarmStatus = alarmStatus;
    }
    
    public Long getTriggerTime() {
        return triggerTime;
    }
    
    public void setTriggerTime(Long triggerTime) {
        this.triggerTime = triggerTime;
    }
    
    public String getAlarmSource() {
        return alarmSource;
    }
    
    public void setAlarmSource(String alarmSource) {
        this.alarmSource = alarmSource;
    }
    
    public String getAlarmDetails() {
        return alarmDetails;
    }
    
    public void setAlarmDetails(String alarmDetails) {
        this.alarmDetails = alarmDetails;
    }
    
    public String getAlarmData() {
        return alarmData;
    }
    
    public void setAlarmData(String alarmData) {
        this.alarmData = alarmData;
    }
    
    public Long getHandleTime() {
        return handleTime;
    }
    
    public void setHandleTime(Long handleTime) {
        this.handleTime = handleTime;
    }
    
    public String getHandler() {
        return handler;
    }
    
    public void setHandler(String handler) {
        this.handler = handler;
    }
    
    public String getHandleRemark() {
        return handleRemark;
    }
    
    public void setHandleRemark(String handleRemark) {
        this.handleRemark = handleRemark;
    }
    
    public Long getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    
    public Long getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
    
    @Override
    public String toString() {
        return "AlarmDetail{" +
                "alarmId='" + alarmId + '\'' +
                ", eventDefineId='" + eventDefineId + '\'' +
                ", alarmName='" + alarmName + '\'' +
                ", alarmDescription='" + alarmDescription + '\'' +
                ", alarmLevel='" + alarmLevel + '\'' +
                ", alarmStatus='" + alarmStatus + '\'' +
                ", triggerTime=" + triggerTime +
                ", alarmSource='" + alarmSource + '\'' +
                ", alarmDetails='" + alarmDetails + '\'' +
                ", alarmData='" + alarmData + '\'' +
                ", handleTime=" + handleTime +
                ", handler='" + handler + '\'' +
                ", handleRemark='" + handleRemark + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}




