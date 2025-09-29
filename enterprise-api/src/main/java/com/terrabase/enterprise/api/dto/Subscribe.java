package com.terrabase.enterprise.api.dto;

/**
 * 订阅信息对象
 * 用于时间配置变更事件订阅功能的数据传输对象
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
public class Subscribe {
    
    /**
     * 订阅ID
     */
    private String subscribeId;
    
    /**
     * 服务名称
     */
    private String serviceName;
    
    /**
     * 通知地址
     */
    private String notifyAddress;
    
    /**
     * 事件类型
     */
    private String eventType;
    
    /**
     * 订阅状态
     */
    private String status;
    
    /**
     * 创建时间
     */
    private Long createTime;
    
    public Subscribe() {}
    
    public Subscribe(String subscribeId, String serviceName, String notifyAddress) {
        this.subscribeId = subscribeId;
        this.serviceName = serviceName;
        this.notifyAddress = notifyAddress;
        this.createTime = System.currentTimeMillis();
    }
    
    public String getSubscribeId() {
        return subscribeId;
    }
    
    public void setSubscribeId(String subscribeId) {
        this.subscribeId = subscribeId;
    }
    
    public String getServiceName() {
        return serviceName;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public String getNotifyAddress() {
        return notifyAddress;
    }
    
    public void setNotifyAddress(String notifyAddress) {
        this.notifyAddress = notifyAddress;
    }
    
    public String getEventType() {
        return eventType;
    }
    
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Long getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    
    @Override
    public String toString() {
        return "Subscribe{" +
                "subscribeId='" + subscribeId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", notifyAddress='" + notifyAddress + '\'' +
                ", eventType='" + eventType + '\'' +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}




