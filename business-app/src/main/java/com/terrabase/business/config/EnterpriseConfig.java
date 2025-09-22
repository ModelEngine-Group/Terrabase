package com.terrabase.business.config;

import com.terrabase.enterprise.impl.open.config.KmcConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 企业服务配置类
 * 管理企业服务相关的Bean配置
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
@Configuration
public class EnterpriseConfig {
    
    /**
     * 创建KmcConfig Bean，确保Spring容器中有这个配置
     * 使用@ConfigurationProperties自动绑定配置文件中的属性
     */
    @Bean
    @ConfigurationProperties(prefix = "terrabase.kmc")
    public KmcConfig kmcConfig() {
        return new KmcConfig();
    }
}
