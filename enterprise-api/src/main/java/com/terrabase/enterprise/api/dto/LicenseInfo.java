package com.terrabase.enterprise.api.dto;

/**
 * License信息对象
 * 用于License信息查询功能的数据传输对象
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
public class LicenseInfo {
    
    /**
     * License ID
     */
    private String licenseId;
    
    /**
     * License类型
     */
    private String licenseType;
    
    /**
     * 有效期开始时间
     */
    private Long validFrom;
    
    /**
     * 有效期结束时间
     */
    private Long validTo;
    
    /**
     * 功能模块列表
     */
    private String[] featureModules;
    
    /**
     * 用户数量限制
     */
    private Integer userLimit;
    
    /**
     * 并发连接数限制
     */
    private Integer concurrentLimit;
    
    /**
     * 数据存储限制（GB）
     */
    private Long storageLimit;
    
    /**
     * License状态
     */
    private String licenseStatus;
    
    /**
     * 剩余天数
     */
    private Integer remainingDays;
    
    public LicenseInfo() {}
    
    public LicenseInfo(String licenseId, String licenseType) {
        this.licenseId = licenseId;
        this.licenseType = licenseType;
    }
    
    public String getLicenseId() {
        return licenseId;
    }
    
    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }
    
    public String getLicenseType() {
        return licenseType;
    }
    
    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }
    
    public Long getValidFrom() {
        return validFrom;
    }
    
    public void setValidFrom(Long validFrom) {
        this.validFrom = validFrom;
    }
    
    public Long getValidTo() {
        return validTo;
    }
    
    public void setValidTo(Long validTo) {
        this.validTo = validTo;
    }
    
    public String[] getFeatureModules() {
        return featureModules;
    }
    
    public void setFeatureModules(String[] featureModules) {
        this.featureModules = featureModules;
    }
    
    public Integer getUserLimit() {
        return userLimit;
    }
    
    public void setUserLimit(Integer userLimit) {
        this.userLimit = userLimit;
    }
    
    public Integer getConcurrentLimit() {
        return concurrentLimit;
    }
    
    public void setConcurrentLimit(Integer concurrentLimit) {
        this.concurrentLimit = concurrentLimit;
    }
    
    public Long getStorageLimit() {
        return storageLimit;
    }
    
    public void setStorageLimit(Long storageLimit) {
        this.storageLimit = storageLimit;
    }
    
    public String getLicenseStatus() {
        return licenseStatus;
    }
    
    public void setLicenseStatus(String licenseStatus) {
        this.licenseStatus = licenseStatus;
    }
    
    public Integer getRemainingDays() {
        return remainingDays;
    }
    
    public void setRemainingDays(Integer remainingDays) {
        this.remainingDays = remainingDays;
    }
    
    @Override
    public String toString() {
        return "LicenseInfo{" +
                "licenseId='" + licenseId + '\'' +
                ", licenseType='" + licenseType + '\'' +
                ", validFrom=" + validFrom +
                ", validTo=" + validTo +
                ", featureModules=" + (featureModules != null ? java.util.Arrays.toString(featureModules) : null) +
                ", userLimit=" + userLimit +
                ", concurrentLimit=" + concurrentLimit +
                ", storageLimit=" + storageLimit +
                ", licenseStatus='" + licenseStatus + '\'' +
                ", remainingDays=" + remainingDays +
                '}';
    }
}




