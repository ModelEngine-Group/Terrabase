package com.terrabase.enterprise.impl.commercial;

import com.terrabase.enterprise.api.CertificateService;
import com.terrabase.enterprise.api.dto.*;
import com.terrabase.enterprise.api.response.ResultVo;
import com.terrabase.enterprise.impl.commercial.client.CertFeignClient;
import com.terrabase.enterprise.impl.commercial.client.LicenseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商业版证书管理服务实现
 * 集成商业组件实现证书管理功能
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
@Service
public class CommercialCertificateServiceImpl implements CertificateService {
    
    private static final Logger logger = LoggerFactory.getLogger(CommercialCertificateServiceImpl.class);

    @Autowired
    private LicenseClient licenseClient;

    @Autowired
    private CertFeignClient certFeignClient;

    // ========== 证书管理相关接口实现 ==========

    @Override
    public ResultVo<List<CertCollectInfo>> listCertificateServiceList() {
        try {
            logger.info("商业版执行证书信息查询");

            // 使用 Feign 客户端调用远程服务
            ResultVo<List<CertCollectInfo>> result = certFeignClient.listCertificateServiceList();

            if ("200".equals(result.getCode())) {
                logger.info("商业版Feign客户端证书信息查询成功，查询到证书数量: {}", 
                        result.getData() != null ? result.getData().size() : 0);
            } else {
                logger.error("商业版Feign客户端证书信息查询失败: {}", result.getMsg());
            }

            return result;

        } catch (Exception e) {
            logger.error("商业版证书信息查询失败", e);
            return ResultVo.error("500", "证书信息查询失败: " + e.getMessage());
        }
    }

    @Override
    public ResultVo<LicenseInfo> getLicenseInfo() {
        try {
            logger.info("商业版执行License信息查询");
            
            // 使用LicenseClient调用远程服务
            ResultVo<LicenseInfo> result = licenseClient.getLicenseInfo();
            
            if ("200".equals(result.getCode())) {
                logger.info("商业版License信息查询成功 - 状态: {}, SBOM模块数量: {}",
                        result.getData().getStatus(), 
                        result.getData().getSboms() != null ? result.getData().getSboms().size() : 0);
            } else {
                logger.error("商业版License信息查询失败: {}", result.getMsg());
            }
            
            return result;
            
        } catch (Exception e) {
            logger.error("商业版License信息查询失败", e);
            return ResultVo.error("500", "License信息查询失败: " + e.getMessage());
        }
    }
}
