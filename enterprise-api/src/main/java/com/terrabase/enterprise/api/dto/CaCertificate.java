package com.terrabase.enterprise.api.dto;

/**
 * CA证书对象
 * 用于导入信任CA证书功能的数据传输对象
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
public class CaCertificate {
    
    /**
     * CA证书ID
     */
    private String caCertificateId;
    
    /**
     * CA证书名称
     */
    private String caCertificateName;
    
    /**
     * CA证书内容
     */
    private String caCertificateContent;
    
    /**
     * CA证书格式
     */
    private String caCertificateFormat;
    
    /**
     * 证书颁发者
     */
    private String issuer;
    
    /**
     * 证书主题
     */
    private String subject;
    
    /**
     * 有效期开始时间
     */
    private Long validFrom;
    
    /**
     * 有效期结束时间
     */
    private Long validTo;
    
    public CaCertificate() {}
    
    public CaCertificate(String caCertificateId, String caCertificateName, String caCertificateContent) {
        this.caCertificateId = caCertificateId;
        this.caCertificateName = caCertificateName;
        this.caCertificateContent = caCertificateContent;
    }
    
    public String getCaCertificateId() {
        return caCertificateId;
    }
    
    public void setCaCertificateId(String caCertificateId) {
        this.caCertificateId = caCertificateId;
    }
    
    public String getCaCertificateName() {
        return caCertificateName;
    }
    
    public void setCaCertificateName(String caCertificateName) {
        this.caCertificateName = caCertificateName;
    }
    
    public String getCaCertificateContent() {
        return caCertificateContent;
    }
    
    public void setCaCertificateContent(String caCertificateContent) {
        this.caCertificateContent = caCertificateContent;
    }
    
    public String getCaCertificateFormat() {
        return caCertificateFormat;
    }
    
    public void setCaCertificateFormat(String caCertificateFormat) {
        this.caCertificateFormat = caCertificateFormat;
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
    
    @Override
    public String toString() {
        return "CaCertificate{" +
                "caCertificateId='" + caCertificateId + '\'' +
                ", caCertificateName='" + caCertificateName + '\'' +
                ", caCertificateFormat='" + caCertificateFormat + '\'' +
                ", issuer='" + issuer + '\'' +
                ", subject='" + subject + '\'' +
                ", validFrom=" + validFrom +
                ", validTo=" + validTo +
                '}';
    }
}




