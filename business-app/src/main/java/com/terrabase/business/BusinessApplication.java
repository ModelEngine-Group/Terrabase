package com.terrabase.business;

import com.terrabase.business.util.JarLoadUtil;
import com.terrabase.enterprise.api.EnterpriseService;
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
 * @author Terrabase Team
 * @version 1.0.0
 */
@SpringBootApplication
public class BusinessApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(BusinessApplication.class);
    
    @Autowired
    private JarLoadUtil jarLoadUtil;

    // 动态加载企业服务 - 延迟初始化
    private EnterpriseService enterpriseService;
    
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
                // 延迟加载企业服务
                if (enterpriseService == null) {
                    logger.info("正在加载企业服务...");
                    enterpriseService = jarLoadUtil.loadEnterpriseService();
                }
                
                if (enterpriseService != null) {
                    logger.info("企业服务加载成功:");
                    logger.info("  服务名称: {}", enterpriseService.getServiceName());
                    logger.info("  服务版本: {}", enterpriseService.getServiceVersion());
                    logger.info("  服务类型: {}", enterpriseService.getServiceType());
                    
                    // 启动企业服务
                    logger.info("正在启动企业服务...");
                    enterpriseService.start();
                    
                    // 检查服务是否成功启动
                    if (enterpriseService.isRunning()) {
                        logger.info("企业服务启动成功");
                    } else {
                        logger.warn("企业服务启动失败");
                    }
                        
                    // 获取健康状态
                    String healthStatus = enterpriseService.getHealthStatus();
                    logger.info("企业服务健康状态: {}", healthStatus);
                } else {
                    logger.error("企业服务加载失败，应用可能无法正常工作");
                }
                
            } catch (Exception e) {
                logger.error("应用初始化过程中发生错误", e);
                // 不抛出异常，让应用继续启动
                logger.warn("应用将在没有企业服务的情况下继续运行");
            }
            
            logger.info("=== Terrabase 业务应用初始化完成 ===");
        };
    }
}
