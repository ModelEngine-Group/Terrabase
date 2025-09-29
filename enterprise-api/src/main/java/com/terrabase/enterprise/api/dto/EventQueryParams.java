package com.terrabase.enterprise.api.dto;

/**
 * 告警查询参数对象
 * 用于分页查询告警功能的数据传输对象
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
public class EventQueryParams {
    
    /**
     * 页码
     */
    private Integer pageNum;
    
    /**
     * 每页大小
     */
    private Integer pageSize;
    
    /**
     * 告警级别
     */
    private String alarmLevel;
    
    /**
     * 告警状态
     */
    private String alarmStatus;
    
    /**
     * 告警类型
     */
    private String alarmType;
    
    /**
     * 开始时间
     */
    private Long startTime;
    
    /**
     * 结束时间
     */
    private Long endTime;
    
    /**
     * 告警来源
     */
    private String alarmSource;
    
    /**
     * 关键字搜索
     */
    private String keyword;
    
    public EventQueryParams() {}
    
    public EventQueryParams(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
    
    public Integer getPageNum() {
        return pageNum;
    }
    
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
    
    public Integer getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
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
    
    public String getAlarmType() {
        return alarmType;
    }
    
    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
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
    
    public String getAlarmSource() {
        return alarmSource;
    }
    
    public void setAlarmSource(String alarmSource) {
        this.alarmSource = alarmSource;
    }
    
    public String getKeyword() {
        return keyword;
    }
    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    @Override
    public String toString() {
        return "EventQueryParams{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", alarmLevel='" + alarmLevel + '\'' +
                ", alarmStatus='" + alarmStatus + '\'' +
                ", alarmType='" + alarmType + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", alarmSource='" + alarmSource + '\'' +
                ", keyword='" + keyword + '\'' +
                '}';
    }
}




