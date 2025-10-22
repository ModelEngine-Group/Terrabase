package com.terrabase.enterprise.impl.open;

import com.terrabase.enterprise.api.CertificateService;
import com.terrabase.enterprise.api.dto.*;
import com.terrabase.enterprise.api.response.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 开源版证书管理服务实现
 * 基于开源技术实现证书管理功能
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
@Service
public class OpenCertificateServiceImpl implements CertificateService {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenCertificateServiceImpl.class);

    // ========== 证书管理相关接口实现 ==========

    @Override
    public ResultVo<List<CertCollectInfo>> listCertificateServiceList() {
        try {
            logger.info("开源版执行证书信息查询");

            // 返回模拟数据
            List<CertCollectInfo> certificates = new java.util.ArrayList<>();
            
            // 创建数据使能证书
            CertCollectInfo cert1 = new CertCollectInfo();
            cert1.setProductName("数据使能平台");
            cert1.setIssueTime(System.currentTimeMillis() - 86400000L); // 1天前
            cert1.setExpirationTime(System.currentTimeMillis() + 86400000L * 365); // 1年后
            cert1.setIssuer("Terrabase CA");
            cert1.setSubject("CN=terrabase-data-enable");
            cert1.setSerialNumber("1234567890ABCDEF");
            cert1.setCertType("SSL");
            cert1.setStatus("有效");
            cert1.setAlertBeforeExpirationDays(30);
            cert1.setCertName("数据使能证书");
            cert1.setProductVersion("1.0.0");
            cert1.setPatchVersion("1.0.1");
            cert1.setDeviceEsn("DEVICE-001");
            certificates.add(cert1);
            
            logger.info("开源版证书信息查询成功，查询到证书数量: {}", certificates.size());
            return ResultVo.success(certificates);
            
        } catch (Exception e) {
            logger.error("开源版证书信息查询失败", e);
            return ResultVo.error("500", "证书信息查询失败: " + e.getMessage());
        }
    }
    
    @Override
    public ResultVo<LicenseInfo> getLicenseInfo() {
        try {
            logger.info("开源版执行License信息查询");

            // 返回模拟数据
            LicenseInfo licenseInfo = new LicenseInfo();
            
            // 设置许可证状态：2表示已经激活
            licenseInfo.setStatus("2");
            
            // 设置SBOM信息
            List<LicenseInfoEx> sboms = new java.util.ArrayList<>();
            
            // 添加数据使能模块信息
            LicenseInfoEx dataEnable = new LicenseInfoEx();
            dataEnable.setName("数据使能");
            dataEnable.setTotal("1000");
            dataEnable.setUnit("用户");
            sboms.add(dataEnable);
            
            licenseInfo.setSboms(sboms);
            
            logger.info("开源版License信息查询成功 - 状态: {}, SBOM模块数量: {}", 
                    licenseInfo.getStatus(), licenseInfo.getSboms().size());
            
            return ResultVo.success(licenseInfo);
            
        } catch (Exception e) {
            logger.error("开源版License信息查询失败", e);
            return ResultVo.error("500", "License信息查询失败: " + e.getMessage());
        }
    }
}
