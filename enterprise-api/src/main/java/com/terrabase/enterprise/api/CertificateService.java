package com.terrabase.enterprise.api;

import com.terrabase.enterprise.api.dto.*;
import com.terrabase.enterprise.api.response.ResultVo;
import java.util.List;

/**
 * 证书管理服务接口
 * 提供证书注册、导入、查询等功能
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
public interface CertificateService {

    // ========== 证书管理相关接口 ==========
    /**
     * 获取oms管理的所有证书信息接口
     * @return 证书收集信息结果对象
     */
    ResultVo<List<CertCollectInfo>> listCertificateServiceList();
    
    /**
     * License信息查询接口
     * @return License信息结果对象
     */
    ResultVo<LicenseInfo> getLicenseInfo();
}
