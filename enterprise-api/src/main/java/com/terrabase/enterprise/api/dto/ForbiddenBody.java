package com.terrabase.enterprise.api.dto;

import java.util.List;

/**
 * 菜单屏蔽信息对象
 * 用于菜单屏蔽注册功能的数据传输对象
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
public class ForbiddenBody {
    
    /**
     * 屏蔽的菜单列表
     */
    private List<String> forbiddenMenuIds;
    
    /**
     * 屏蔽原因
     */
    private String reason;
    
    /**
     * 应用场景
     */
    private String applicationScenario;
    
    /**
     * 是否永久屏蔽
     */
    private Boolean permanent;
    
    /**
     * 屏蔽开始时间
     */
    private Long startTime;
    
    /**
     * 屏蔽结束时间
     */
    private Long endTime;
    
    public ForbiddenBody() {}
    
    public ForbiddenBody(List<String> forbiddenMenuIds, String reason) {
        this.forbiddenMenuIds = forbiddenMenuIds;
        this.reason = reason;
    }
    
    public List<String> getForbiddenMenuIds() {
        return forbiddenMenuIds;
    }
    
    public void setForbiddenMenuIds(List<String> forbiddenMenuIds) {
        this.forbiddenMenuIds = forbiddenMenuIds;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public String getApplicationScenario() {
        return applicationScenario;
    }
    
    public void setApplicationScenario(String applicationScenario) {
        this.applicationScenario = applicationScenario;
    }
    
    public Boolean getPermanent() {
        return permanent;
    }
    
    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }
    
    public Long getStartTime() {
        return startTime;
    }
    
    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }
    
    public Long getEndTime() {
        return endTime;
    }
    
    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
    
    @Override
    public String toString() {
        return "ForbiddenBody{" +
                "forbiddenMenuIds=" + forbiddenMenuIds +
                ", reason='" + reason + '\'' +
                ", applicationScenario='" + applicationScenario + '\'' +
                ", permanent=" + permanent +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}




