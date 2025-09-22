package com.terrabase.enterprise.impl.open;

import com.terrabase.enterprise.impl.open.config.KmcConfig;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 测试配置类
 * 为Spring Boot测试提供必要的配置
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
@SpringBootConfiguration
public class TestConfiguration {
    
    /**
     * KMC配置Bean
     */
    @Bean
    public KmcConfig kmcConfig() {
        KmcConfig config = new KmcConfig();
        config.setEnabled(true);
        config.setDefaultKeyId("test_default");
        config.setEnableAuditLog(true);
        return config;
    }
}
