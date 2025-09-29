package com.terrabase.enterprise.api;

import com.terrabase.enterprise.api.dto.*;
import java.util.List;

/**
 * 证书管理服务接口
 * 提供证书注册、导入、查询等功能
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
public interface CertificateService {
    
    /**
     * 获取服务名称
     * @return 服务名称
     */
    String getServiceName();
    
    /**
     * 获取服务版本
     * @return 服务版本
     */
    String getServiceVersion();
    
    /**
     * 获取服务类型（commercial 或 open）
     * @return 服务类型
     */
    String getServiceType();

    /**
     * 获取服务健康状态
     * @return 健康状态信息
     */
    String getHealthStatus();

    /**
     * 启动服务
     * 将服务的running状态设置为true
     */
    void start();

    /**
     * 停止服务
     * 将服务的running状态设置为false
     */
    void stop();

    /**
     * 检查服务是否正在运行
     * @return true如果服务正在运行
     */
    boolean isRunning();

    // ========== 证书管理相关接口 ==========
    
    /**
     * 证书注册接口
     * @param certificate 证书信息对象
     */
    void registerCertificate(RegisterCertificate certificate);
    
    /**
     * 导入/更新证书接口
     * @param certificate 证书详细信息对象
     */
    void importOrUpdateCertificate(CertificateDetail certificate);
    
    /**
     * 导入信任CA证书接口
     * @param caCertificate CA证书对象
     */
    void importTrustedCaCertificate(CaCertificate caCertificate);
    
    /**
     * 查询所有证书信息接口
     * @return 证书收集信息列表
     */
    List<CertCollectInfo> queryAllCertificates();
    
    /**
     * License信息查询接口
     * @return License信息对象
     */
    LicenseInfo queryLicenseInfo();
}
