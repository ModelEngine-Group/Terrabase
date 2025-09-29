package com.terrabase.enterprise.api.dto;

/**
 * 证书详细信息对象
 * 用于证书导入/更新功能的数据传输对象
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
public class CertificateDetail {
    
    /**
     * 证书ID
     */
    private String certificateId;
    
    /**
     * 证书内容
     */
    private String certificateContent;
    
    /**
     * 私钥内容
     */
    private String privateKeyContent;
    
    /**
     * 证书格式
     */
    private String certificateFormat;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 证书链
     */
    private String[] certificateChain;
    
    public CertificateDetail() {}
    
    public CertificateDetail(String certificateId, String certificateContent) {
        this.certificateId = certificateId;
        this.certificateContent = certificateContent;
    }
    
    public String getCertificateId() {
        return certificateId;
    }
    
    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }
    
    public String getCertificateContent() {
        return certificateContent;
    }
    
    public void setCertificateContent(String certificateContent) {
        this.certificateContent = certificateContent;
    }
    
    public String getPrivateKeyContent() {
        return privateKeyContent;
    }
    
    public void setPrivateKeyContent(String privateKeyContent) {
        this.privateKeyContent = privateKeyContent;
    }
    
    public String getCertificateFormat() {
        return certificateFormat;
    }
    
    public void setCertificateFormat(String certificateFormat) {
        this.certificateFormat = certificateFormat;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String[] getCertificateChain() {
        return certificateChain;
    }
    
    public void setCertificateChain(String[] certificateChain) {
        this.certificateChain = certificateChain;
    }
    
    @Override
    public String toString() {
        return "CertificateDetail{" +
                "certificateId='" + certificateId + '\'' +
                ", certificateFormat='" + certificateFormat + '\'' +
                ", password='" + (password != null ? "***" : null) + '\'' +
                ", certificateChain=" + (certificateChain != null ? certificateChain.length + " items" : null) +
                '}';
    }
}




