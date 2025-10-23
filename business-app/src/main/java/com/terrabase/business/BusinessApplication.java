package com.terrabase.business;

import com.alibaba.nacos.common.tls.TlsSystemConfig;
import com.terrabase.business.util.JarLoadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Terrabase 业务应用主启动类
 * 基于Spring Boot框架，支持动态加载企业服务实现
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
@SpringBootApplication
public class BusinessApplication {
//    static {
//        // configure nacos SSL
//        System.setProperty(TlsSystemConfig.TLS_ENABLE, "true");
//        System.setProperty(TlsSystemConfig.CLIENT_AUTH, "true");
//        System.setProperty(TlsSystemConfig.CLIENT_TRUST_CERT,
//                "/opt/huawei/fce/runtime/security/server_cert/nacos/nacos.crt");
//    }
    
    private static final Logger logger = LoggerFactory.getLogger(BusinessApplication.class);
    
    @Autowired
    private JarLoadUtil jarLoadUtil;

    
    public static void main(String[] args) {
        logger.info("正在启动 Terrabase 业务应用...");
        SpringApplication.run(BusinessApplication.class, args);
    }
    
    /**
     * 配置RestTemplate Bean
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    /**
     * 应用启动后执行初始化逻辑
     */
    @Bean
    public CommandLineRunner init() {
        return args -> {
            logger.info("=== Terrabase 业务应用初始化开始 ===");
            
            try {
                // 检测企业模式
                String enterpriseMode = jarLoadUtil.getEnterpriseMode();
                logger.info("企业模式: {}", enterpriseMode);
                
                if ("commercial".equals(enterpriseMode)) {
                    logger.info("商业版模式已启用");
                } else {
                    logger.info("开源版模式已启用");
                }
                
            } catch (Exception e) {
                logger.error("企业模式检测失败", e);
                // 不抛出异常，让应用继续启动
                logger.warn("应用将在默认模式下继续运行");
            }
            
            logger.info("=== Terrabase 业务应用初始化完成 ===");
        };
    }
}
