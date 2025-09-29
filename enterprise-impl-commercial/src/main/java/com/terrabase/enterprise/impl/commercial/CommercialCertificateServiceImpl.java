package com.terrabase.enterprise.impl.commercial;

import com.terrabase.enterprise.api.CertificateService;
import com.terrabase.enterprise.api.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 商业版证书管理服务实现
 * 集成商业组件实现证书管理功能
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
@Service
public class CommercialCertificateServiceImpl implements CertificateService {
    
    private static final Logger logger = LoggerFactory.getLogger(CommercialCertificateServiceImpl.class);

    private final AtomicBoolean running = new AtomicBoolean(false);
    
    private final RestTemplate restTemplate;
    
    private String omsBaseUrl;
    private int timeout;
    
    public CommercialCertificateServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        // 设置默认值
        this.omsBaseUrl = "http://localhost:8081/api/";
        this.timeout = 30000;
    }
    
    public CommercialCertificateServiceImpl(RestTemplate restTemplate, String omsBaseUrl, int timeout) {
        this.restTemplate = restTemplate;
        this.omsBaseUrl = omsBaseUrl;
        this.timeout = timeout;
    }
    
    @Override
    public String getServiceName() {
        return "Commercial Certificate Service";
    }
    
    @Override
    public String getServiceVersion() {
        return "1.0.0-commercial";
    }
    
    @Override
    public String getServiceType() {
        return "commercial";
    }
    
    @Override
    public String getHealthStatus() {
        if (!running.get()) {
            return "服务未运行";
        }
        
        return String.format("商业版证书管理服务健康状态: 正常 (版本: %s, 类型: %s)", 
                getServiceVersion(), getServiceType());
    }

    @Override
    public void start() {
        if (running.compareAndSet(false, true)) {
            logger.info("商业版证书管理服务已启动");
        } else {
            logger.warn("商业版证书管理服务已经在运行中");
        }
    }

    @Override
    public void stop() {
        if (running.compareAndSet(true, false)) {
            logger.info("商业版证书管理服务已停止");
        } else {
            logger.warn("商业版证书管理服务已经停止");
        }
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    // ========== 证书管理相关接口实现 ==========

    @Override
    public void registerCertificate(RegisterCertificate certificate) {
        if (!running.get()) {
            logger.warn("服务未运行，无法执行证书注册操作");
            return;
        }

        if (certificate == null) {
            logger.warn("证书注册对象不能为空");
            return;
        }

        try {
            logger.info("商业版执行证书注册: {}", certificate);
            
            // 构建请求参数
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("certificateId", certificate.getCertificateId());
            requestBody.put("certificateName", certificate.getCertificateName());
            requestBody.put("certificateType", certificate.getCertificateType());
            requestBody.put("description", certificate.getCertificateDescription());
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // 构建证书注册接口URL
            String registerCertificateUrl = omsBaseUrl + "/framework/v1/certificate/action/register/type/internal";
            
            // 调用REST接口进行证书注册
            ResponseEntity<Map> response = restTemplate.postForEntity(registerCertificateUrl, requestEntity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                String status = (String) responseBody.get("status");
                
                if ("success".equals(status)) {
                    logger.info("商业版REST接口证书注册成功 - 证书ID: {}, 证书名称: {}, 证书类型: {}",
                            certificate.getCertificateId(), certificate.getCertificateName(), certificate.getCertificateType());
                } else {
                    String errorMsg = (String) responseBody.get("message");
                    logger.error("商业版REST接口证书注册失败: {}", errorMsg);
                }
            } else {
                logger.error("商业版REST接口证书注册失败，HTTP状态码: {}", response.getStatusCode());
            }

        } catch (RestClientException e) {
            logger.error("商业版REST接口证书注册调用异常", e);
        } catch (Exception e) {
            logger.error("商业版证书注册失败: {}", certificate, e);
        }
    }

    @Override
    public void importOrUpdateCertificate(CertificateDetail certificate) {
        if (!running.get()) {
            logger.warn("服务未运行，无法执行证书导入/更新操作");
            return;
        }

        if (certificate == null) {
            logger.warn("证书详细信息对象不能为空");
            return;
        }

        try {
            logger.info("商业版执行证书导入/更新，证书ID: {}", certificate.getCertificateId());
            
            // 构建请求参数
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("certificateId", certificate.getCertificateId());
            requestBody.put("certificateFormat", certificate.getCertificateFormat());
            requestBody.put("certificateContent", certificate.getCertificateContent());
            requestBody.put("privateKey", certificate.getPrivateKeyContent());
            requestBody.put("password", certificate.getPassword());
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // 构建证书导入/更新接口URL
            String importCertificateUrl = omsBaseUrl + "/framework/v1/certificate/action/import/om";
            
            // 调用REST接口进行证书导入/更新
            ResponseEntity<Map> response = restTemplate.postForEntity(importCertificateUrl, requestEntity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                String status = (String) responseBody.get("status");
                
                if ("success".equals(status)) {
                    logger.info("商业版REST接口证书导入/更新成功 - 证书ID: {}, 证书格式: {}",
                            certificate.getCertificateId(), certificate.getCertificateFormat());
                } else {
                    String errorMsg = (String) responseBody.get("message");
                    logger.error("商业版REST接口证书导入/更新失败: {}", errorMsg);
                }
            } else {
                logger.error("商业版REST接口证书导入/更新失败，HTTP状态码: {}", response.getStatusCode());
            }

        } catch (RestClientException e) {
            logger.error("商业版REST接口证书导入/更新调用异常", e);
        } catch (Exception e) {
            logger.error("商业版证书导入/更新失败: {}", certificate, e);
        }
    }

    @Override
    public void importTrustedCaCertificate(CaCertificate caCertificate) {
        if (!running.get()) {
            logger.warn("服务未运行，无法执行CA证书导入操作");
            return;
        }

        if (caCertificate == null) {
            logger.warn("CA证书对象不能为空");
            return;
        }

        try {
            logger.info("商业版执行CA证书导入: {}", caCertificate);
            
            // 构建请求参数
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("caCertificateId", caCertificate.getCaCertificateId());
            requestBody.put("caCertificateName", caCertificate.getCaCertificateName());
            requestBody.put("issuer", caCertificate.getIssuer());
            requestBody.put("certificateContent", caCertificate.getCaCertificateContent());
            requestBody.put("validFrom", caCertificate.getValidFrom());
            requestBody.put("validTo", caCertificate.getValidTo());
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // 构建CA证书导入接口URL
            String importCaCertificateUrl = omsBaseUrl + "/framework/v1/certificate/action/import";
            
            // 调用REST接口进行CA证书导入
            ResponseEntity<Map> response = restTemplate.postForEntity(importCaCertificateUrl, requestEntity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                String status = (String) responseBody.get("status");
                
                if ("success".equals(status)) {
                    logger.info("商业版REST接口CA证书导入成功 - CA证书ID: {}, CA证书名称: {}, 颁发者: {}",
                            caCertificate.getCaCertificateId(), caCertificate.getCaCertificateName(), caCertificate.getIssuer());
                } else {
                    String errorMsg = (String) responseBody.get("message");
                    logger.error("商业版REST接口CA证书导入失败: {}", errorMsg);
                }
            } else {
                logger.error("商业版REST接口CA证书导入失败，HTTP状态码: {}", response.getStatusCode());
            }

        } catch (RestClientException e) {
            logger.error("商业版REST接口CA证书导入调用异常", e);
        } catch (Exception e) {
            logger.error("商业版CA证书导入失败: {}", caCertificate, e);
        }
    }

    @Override
    public List<CertCollectInfo> queryAllCertificates() {
        if (!running.get()) {
            logger.warn("服务未运行，无法执行证书信息查询操作");
            return null;
        }

        try {
            logger.info("商业版执行证书信息查询");
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            
            // 构建证书信息查询接口URL
            String queryCertificatesUrl = omsBaseUrl + "/framework/v1/certificate/action/cert/collect";
            
            // 调用REST接口进行证书信息查询
            ResponseEntity<Map> response = restTemplate.exchange(queryCertificatesUrl, HttpMethod.GET, requestEntity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                String status = (String) responseBody.get("status");
                
                if ("success".equals(status)) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> certificateList = (List<Map<String, Object>>) responseBody.get("certificateList");
                    
                    List<CertCollectInfo> certificates = new java.util.ArrayList<>();
                    if (certificateList != null) {
                        for (Map<String, Object> certMap : certificateList) {
                            CertCollectInfo cert = new CertCollectInfo(
                                (String) certMap.get("certificateId"),
                                (String) certMap.get("certificateName"),
                                (String) certMap.get("certificateType")
                            );
                            cert.setCertificateStatus((String) certMap.get("certificateStatus"));
                            cert.setValidFrom((Long) certMap.get("validFrom"));
                            cert.setValidTo((Long) certMap.get("validTo"));
                            cert.setIssuer((String) certMap.get("issuer"));
                            cert.setSubject((String) certMap.get("subject"));
                            certificates.add(cert);
                        }
                    }
                    
                    logger.info("商业版REST接口证书信息查询成功，查询到证书数量: {}", certificates.size());
                    return certificates;
                } else {
                    String errorMsg = (String) responseBody.get("message");
                    logger.error("商业版REST接口证书信息查询失败: {}", errorMsg);
                    return null;
                }
            } else {
                logger.error("商业版REST接口证书信息查询失败，HTTP状态码: {}", response.getStatusCode());
                return null;
            }

        } catch (RestClientException e) {
            logger.error("商业版REST接口证书信息查询调用异常", e);
            return null;
        } catch (Exception e) {
            logger.error("商业版证书信息查询失败", e);
            return null;
        }
    }

    @Override
    public LicenseInfo queryLicenseInfo() {
        if (!running.get()) {
            logger.warn("服务未运行，无法执行License信息查询操作");
            return null;
        }

        try {
            logger.info("商业版执行License信息查询");
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            
            // 构建License信息查询接口URL
            String queryLicenseUrl = omsBaseUrl + "/framework/v1/license/info";
            
            // 调用REST接口进行License信息查询
            ResponseEntity<Map> response = restTemplate.exchange(queryLicenseUrl, HttpMethod.GET, requestEntity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                String status = (String) responseBody.get("status");
                
                if ("success".equals(status)) {
                    LicenseInfo licenseInfo = new LicenseInfo(
                        (String) responseBody.get("licenseId"),
                        (String) responseBody.get("licenseType")
                    );
                    licenseInfo.setValidFrom((Long) responseBody.get("validFrom"));
                    licenseInfo.setValidTo((Long) responseBody.get("validTo"));
                    licenseInfo.setFeatureModules((String[]) responseBody.get("featureModules"));
                    licenseInfo.setUserLimit((Integer) responseBody.get("userLimit"));
                    licenseInfo.setConcurrentLimit((Integer) responseBody.get("concurrentLimit"));
                    licenseInfo.setStorageLimit((Long) responseBody.get("storageLimit"));
                    licenseInfo.setLicenseStatus((String) responseBody.get("licenseStatus"));
                    licenseInfo.setRemainingDays((Integer) responseBody.get("remainingDays"));
                    
                    logger.info("商业版REST接口License信息查询成功 - License ID: {}, License类型: {}, 剩余天数: {}",
                            licenseInfo.getLicenseId(), licenseInfo.getLicenseType(), licenseInfo.getRemainingDays());
                    
                    return licenseInfo;
                } else {
                    String errorMsg = (String) responseBody.get("message");
                    logger.error("商业版REST接口License信息查询失败: {}", errorMsg);
                    return null;
                }
            } else {
                logger.error("商业版REST接口License信息查询失败，HTTP状态码: {}", response.getStatusCode());
                return null;
            }

        } catch (RestClientException e) {
            logger.error("商业版REST接口License信息查询调用异常", e);
            return null;
        } catch (Exception e) {
            logger.error("商业版License信息查询失败", e);
            return null;
        }
    }
}
