package com.terrabase.enterprise.impl.commercial.config;

import com.alibaba.nacos.common.tls.TlsSystemConfig;
import com.terrabase.sdk.config.TerrabaseSDKConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 商业版Nacos配置管理类
 * 仅在商业版且启用Nacos时生效
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
@Configuration
@ConditionalOnProperty(
    name = "spring.cloud.nacos.discovery.enabled", 
    havingValue = "true", 
    matchIfMissing = false
)
@Import({NacosListener.class, LoadBalancerClientFactoryConfig.class})
public class CommercialNacosConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(CommercialNacosConfig.class);
    
    /**
     * 根据SDK配置初始化Nacos配置
     * 
     * @param sdkConfig SDK配置
     */
    public static void initializeFromSDKConfig(TerrabaseSDKConfig sdkConfig) {
        if (sdkConfig == null || !sdkConfig.isNacosDiscoveryEnabled()) {
            logger.info("Nacos服务发现未启用，跳过Nacos配置初始化");
            return;
        }
        
        logger.info("初始化商业版Nacos配置");
        logger.info("Nacos服务器地址: {}", sdkConfig.getNacosServerAddr());
        logger.info("Nacos用户名: {}", sdkConfig.getNacosUsername());
        logger.info("服务名称: {}", sdkConfig.getServiceName());
        logger.info("服务端口: {}", sdkConfig.getServicePort());
        
        // 设置系统属性，供Spring Cloud Nacos使用
        if (sdkConfig.getNacosServerAddr() != null) {
            System.setProperty("spring.cloud.nacos.discovery.server-addr", sdkConfig.getNacosServerAddr());
        }
        if (sdkConfig.getNacosUsername() != null) {
            System.setProperty("spring.cloud.nacos.discovery.username", sdkConfig.getNacosUsername());
        }
        if (sdkConfig.getNacosPassword() != null) {
            System.setProperty("spring.cloud.nacos.discovery.password", sdkConfig.getNacosPassword());
        }
        if (sdkConfig.getNacosPort() != null) {
            System.setProperty("spring.cloud.nacos.discovery.port", sdkConfig.getNacosPort());
        }
        if (sdkConfig.getNacosIp() != null) {
            System.setProperty("spring.cloud.nacos.discovery.ip", sdkConfig.getNacosIp());
        }
        if (sdkConfig.getServiceName() != null) {
            System.setProperty("spring.application.name", sdkConfig.getServiceName());
        }
        if (sdkConfig.getServicePort() != null) {
            System.setProperty("server.port", sdkConfig.getServicePort());
        }
        
        // 启用Nacos发现和注册
        System.setProperty("spring.cloud.nacos.discovery.enabled", "true");
        System.setProperty("spring.cloud.nacos.discovery.register-enabled", "true");
        
        // 配置SSL设置
        configureSSL(sdkConfig);
        
        logger.info("商业版Nacos配置初始化完成");
    }
    
    /**
     * 配置SSL设置
     * 
     * @param sdkConfig SDK配置
     */
    private static void configureSSL(TerrabaseSDKConfig sdkConfig) {
        if (sdkConfig.isTlsEnabled()) {
            logger.info("配置Nacos SSL设置");
            logger.info("TLS启用: {}", sdkConfig.isTlsEnabled());
            logger.info("客户端认证: {}", sdkConfig.isClientAuth());
            logger.info("信任证书路径: {}", sdkConfig.getTrustCertPath());
            
            // 设置TLS系统属性
            System.setProperty(TlsSystemConfig.TLS_ENABLE, String.valueOf(sdkConfig.isTlsEnabled()));
            System.setProperty(TlsSystemConfig.CLIENT_AUTH, String.valueOf(sdkConfig.isClientAuth()));
            if (sdkConfig.getTrustCertPath() != null) {
                System.setProperty(TlsSystemConfig.CLIENT_TRUST_CERT, sdkConfig.getTrustCertPath());
            }
            
            logger.info("Nacos SSL配置完成");
        } else {
            logger.info("Nacos SSL未启用");
        }
    }
    
    /**
     * 检查是否为商业版Nacos配置
     * 
     * @param sdkConfig SDK配置
     * @return 是否为商业版Nacos配置
     */
    public static boolean isCommercialNacosConfig(TerrabaseSDKConfig sdkConfig) {
        return sdkConfig != null && 
               sdkConfig.isNacosDiscoveryEnabled() && 
               sdkConfig.getNacosServerAddr() != null && 
               !sdkConfig.getNacosServerAddr().trim().isEmpty();
    }
}
