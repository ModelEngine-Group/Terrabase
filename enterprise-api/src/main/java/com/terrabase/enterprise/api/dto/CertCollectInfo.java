package com.terrabase.enterprise.api.dto;

/**
 * 证书收集信息对象
 * 用于查询所有证书信息功能的数据传输对象
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
public class CertCollectInfo {
    
    /**
     * 证书ID
     */
    private String certificateId;
    
    /**
     * 证书名称
     */
    private String certificateName;
    
    /**
     * 证书类型
     */
    private String certificateType;
    
    /**
     * 证书状态
     */
    private String certificateStatus;
    
    /**
     * 有效期开始时间
     */
    private Long validFrom;
    
    /**
     * 有效期结束时间
     */
    private Long validTo;
    
    /**
     * 证书颁发者
     */
    private String issuer;
    
    /**
     * 证书主题
     */
    private String subject;
    
    /**
     * 证书用途
     */
    private String certificateUsage;
    
    /**
     * 创建时间
     */
    private Long createTime;
    
    /**
     * 更新时间
     */
    private Long updateTime;
    
    public CertCollectInfo() {}
    
    public CertCollectInfo(String certificateId, String certificateName, String certificateType) {
        this.certificateId = certificateId;
        this.certificateName = certificateName;
        this.certificateType = certificateType;
    }
    
    public String getCertificateId() {
        return certificateId;
    }
    
    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }
    
    public String getCertificateName() {
        return certificateName;
    }
    
    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }
    
    public String getCertificateType() {
        return certificateType;
    }
    
    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }
    
    public String getCertificateStatus() {
        return certificateStatus;
    }
    
    public void setCertificateStatus(String certificateStatus) {
        this.certificateStatus = certificateStatus;
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
    
    public String getIssuer() {
        return issuer;
    }
    
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getCertificateUsage() {
        return certificateUsage;
    }
    
    public void setCertificateUsage(String certificateUsage) {
        this.certificateUsage = certificateUsage;
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
        return "CertCollectInfo{" +
                "certificateId='" + certificateId + '\'' +
                ", certificateName='" + certificateName + '\'' +
                ", certificateType='" + certificateType + '\'' +
                ", certificateStatus='" + certificateStatus + '\'' +
                ", validFrom=" + validFrom +
                ", validTo=" + validTo +
                ", issuer='" + issuer + '\'' +
                ", subject='" + subject + '\'' +
                ", certificateUsage='" + certificateUsage + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}




