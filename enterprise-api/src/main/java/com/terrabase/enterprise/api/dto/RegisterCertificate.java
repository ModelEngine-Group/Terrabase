package com.terrabase.enterprise.api.dto;

/**
 * 证书注册对象
 * 用于证书注册功能的数据传输对象
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
public class RegisterCertificate {
    
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
     * 证书用途
     */
    private String certificateUsage;
    
    /**
     * 证书描述
     */
    private String certificateDescription;
    
    /**
     * 有效期（天数）
     */
    private Integer validityDays;
    
    public RegisterCertificate() {}
    
    public RegisterCertificate(String certificateId, String certificateName, String certificateType) {
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
    
    public String getCertificateUsage() {
        return certificateUsage;
    }
    
    public void setCertificateUsage(String certificateUsage) {
        this.certificateUsage = certificateUsage;
    }
    
    public String getCertificateDescription() {
        return certificateDescription;
    }
    
    public void setCertificateDescription(String certificateDescription) {
        this.certificateDescription = certificateDescription;
    }
    
    public Integer getValidityDays() {
        return validityDays;
    }
    
    public void setValidityDays(Integer validityDays) {
        this.validityDays = validityDays;
    }
    
    @Override
    public String toString() {
        return "RegisterCertificate{" +
                "certificateId='" + certificateId + '\'' +
                ", certificateName='" + certificateName + '\'' +
                ", certificateType='" + certificateType + '\'' +
                ", certificateUsage='" + certificateUsage + '\'' +
                ", certificateDescription='" + certificateDescription + '\'' +
                ", validityDays=" + validityDays +
                '}';
    }
}




