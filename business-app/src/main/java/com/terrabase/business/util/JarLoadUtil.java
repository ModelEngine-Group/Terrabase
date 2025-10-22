package com.terrabase.business.util;

import com.terrabase.enterprise.api.*;
import com.terrabase.enterprise.impl.open.OpenEnterpriseServiceImpl;
import com.terrabase.enterprise.impl.open.config.KmcConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 企业服务加载工具类
 * 用于根据配置加载商业版（JAR包）或开源版（直接依赖）的企业服务实现
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
@Component
public class JarLoadUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(JarLoadUtil.class);
    
    @Value("${enterprise.jar.path:./lib}")
    private String jarPath;
    
    @Autowired
    private ApplicationContext applicationContext;
    
    // 缓存已加载的实例
    private final ConcurrentHashMap<String, Object> serviceInstances = new ConcurrentHashMap<>();
    
    /**
     * 加载加解密服务
     * @return 加解密服务实例
     */
    public CryptoService loadCryptoService() {
        return (CryptoService) loadService("crypto_service", 
            "com.terrabase.enterprise.impl.commercial.CommercialCryptoServiceImpl",
            "com.terrabase.enterprise.impl.open.OpenCryptoServiceImpl");
    }
    
    /**
     * 加载用户管理服务
     * @return 用户管理服务实例
     */
    public UserManagementService loadUserManagementService() {
        return (UserManagementService) loadService("user_management_service",
            "com.terrabase.enterprise.impl.commercial.CommercialUserManagementServiceImpl",
            "com.terrabase.enterprise.impl.open.OpenUserManagementServiceImpl");
    }
    
    /**
     * 加载日志管理服务
     * @return 日志管理服务实例
     */
    public LogManagementService loadLogManagementService() {
        return (LogManagementService) loadService("log_management_service",
            "com.terrabase.enterprise.impl.commercial.CommercialLogManagementServiceImpl",
            "com.terrabase.enterprise.impl.open.OpenLogManagementServiceImpl");
    }
    
    /**
     * 加载证书管理服务
     * @return 证书管理服务实例
     */
    public CertificateService loadCertificateService() {
        return (CertificateService) loadService("certificate_service",
            "com.terrabase.enterprise.impl.commercial.CommercialCertificateServiceImpl",
            "com.terrabase.enterprise.impl.open.OpenCertificateServiceImpl");
    }
    
    /**
     * 加载监控告警服务
     * @return 监控告警服务实例
     */
    public MonitoringService loadMonitoringService() {
        return (MonitoringService) loadService("monitoring_service",
            "com.terrabase.enterprise.impl.commercial.CommercialMonitoringServiceImpl",
            "com.terrabase.enterprise.impl.open.OpenMonitoringServiceImpl");
    }
    
    /**
     * 通用服务加载方法
     * @param cacheKey 缓存键
     * @param commercialClassName 商业版类名
     * @param openClassName 开源版类名
     * @return 服务实例
     */
    private Object loadService(String cacheKey, String commercialClassName, String openClassName) {
        try {
            // 先检查缓存
            Object cachedService = serviceInstances.get(cacheKey);
            if (cachedService != null) {
                logger.info("从缓存中获取服务实例: {}", cacheKey);
                return cachedService;
            }
            
            logger.info("开始检测并加载服务: {}", cacheKey);
            
            Object service;
            if (isCommercialJarAvailable()) {
                logger.info("检测到商业版JAR包，加载商业版服务: {}", commercialClassName);
                service = loadServiceFromJar(commercialClassName);
            } else {
                logger.info("未检测到商业版JAR包，使用开源版服务: {}", openClassName);
                service = loadServiceFromClasspath(openClassName);
            }
            
            // 如果服务加载失败，使用开源版作为降级方案
            if (service == null) {
                logger.warn("服务加载失败，使用开源版作为降级方案: {}", openClassName);
                service = loadServiceFromClasspath(openClassName);
            }
            
            // 如果开源版也加载失败，抛出异常
            if (service == null) {
                throw new RuntimeException("无法加载任何服务实现: " + cacheKey);
            }
            
            // 缓存服务实例
            serviceInstances.put(cacheKey, service);
            
            // 服务已加载完成，无需手动启动
            
            logger.info("服务加载成功: {}", cacheKey);
            
            return service;
            
        } catch (Exception e) {
            logger.error("加载服务失败: {}, 尝试使用开源版作为降级方案", cacheKey, e);
            try {
                // 最后的降级方案：直接实例化开源版服务
                Object fallbackService = loadServiceFromClasspath(openClassName);
                if (fallbackService != null) {
                    serviceInstances.put(cacheKey, fallbackService);
                    
                    // 降级服务已加载完成，无需手动启动
                    
                    logger.warn("使用开源版服务作为降级方案: {}", cacheKey);
                    return fallbackService;
                }
            } catch (Exception fallbackException) {
                logger.error("降级方案也失败了: {}", cacheKey, fallbackException);
            }
            throw new RuntimeException("无法加载任何服务实现: " + cacheKey, e);
        }
    }
    
    /**
     * 根据lib目录下是否存在JAR包动态加载企业服务实现
     * @return 企业服务实例，如果加载失败则返回开源版服务作为降级方案
     */
    public EnterpriseService loadEnterpriseService() {
        try {
            String cacheKey = "enterprise_service";
            
            // 先检查缓存
            EnterpriseService cachedService = (EnterpriseService) serviceInstances.get(cacheKey);
            if (cachedService != null) {
                logger.info("从缓存中获取企业服务实例");
                return cachedService;
            }
            
            logger.info("开始检测并加载企业服务实现");
            
            EnterpriseService service;
            if (isCommercialJarAvailable()) {
                logger.info("检测到商业版JAR包，加载商业版企业服务");
                service = loadCommercialService();
            } else {
                logger.info("未检测到商业版JAR包，使用开源版企业服务");
                service = loadOpenSourceService();
            }
            
            // 如果服务加载失败，使用开源版作为降级方案
            if (service == null) {
                logger.warn("企业服务加载失败，使用开源版作为降级方案");
                service = loadOpenSourceService();
            }
            
            // 如果开源版也加载失败，抛出异常
            if (service == null) {
                throw new RuntimeException("无法加载任何企业服务实现");
            }
            
            // 缓存服务实例
            serviceInstances.put(cacheKey, service);
            logger.info("企业服务加载成功: {}", service.getServiceName());
            
            return service;
            
        } catch (Exception e) {
            logger.error("加载企业服务失败，尝试使用开源版作为降级方案", e);
            try {
                // 最后的降级方案：直接实例化开源版服务
                OpenEnterpriseServiceImpl fallbackService = new OpenEnterpriseServiceImpl();
                
                // 从Spring容器中获取KmcConfig并手动注入
                try {
                    KmcConfig kmcConfig = applicationContext.getBean(KmcConfig.class);
                    if (kmcConfig != null) {
                        logger.info("降级方案：从Spring容器中获取KmcConfig并注入到开源版服务");
                        java.lang.reflect.Field kmcConfigField = OpenEnterpriseServiceImpl.class.getDeclaredField("kmcConfig");
                        kmcConfigField.setAccessible(true);
                        kmcConfigField.set(fallbackService, kmcConfig);
                    } else {
                        logger.warn("降级方案：Spring容器中没有找到KmcConfig Bean");
                    }
                } catch (Exception fallbackException) {
                    logger.warn("降级方案：注入KmcConfig失败: {}", fallbackException.getMessage());
                }
                
                serviceInstances.put("enterprise_service", fallbackService);
                
                // 降级企业服务已加载完成，无需手动启动
                logger.info("降级企业服务已加载完成");
                
                logger.warn("使用开源版企业服务作为降级方案");
                return fallbackService;
            } catch (Exception fallbackException) {
                logger.error("降级方案也失败了", fallbackException);
                throw new RuntimeException("无法加载任何企业服务实现，应用无法启动", fallbackException);
            }
        }
    }
    
    /**
     * 检测商业版JAR包是否可用
     * @return true如果JAR包存在且可加载
     */
    private boolean isCommercialJarAvailable() {
        try {
            String jarFileName = "enterprise-impl-commercial-1.0.0.jar";
            File jarFile = new File(jarPath, jarFileName);
            
            if (!jarFile.exists()) {
                logger.debug("商业版JAR文件不存在: {}", jarFile.getAbsolutePath());
                return false;
            }
            
            // 尝试加载JAR包中的类来验证JAR包是否有效
            URL jarUrl = jarFile.toURI().toURL();
            URLClassLoader classLoader = new URLClassLoader(new URL[]{jarUrl}, this.getClass().getClassLoader());
            
            // 尝试加载商业版实现类
            Class<?> clazz = classLoader.loadClass("com.terrabase.enterprise.impl.commercial.CommercialEnterpriseServiceImpl");
            if (clazz != null) {
                logger.debug("商业版JAR包验证成功: {}", jarFile.getAbsolutePath());
                return true;
            }
            
        } catch (Exception e) {
            logger.debug("商业版JAR包检测失败: {}", e.getMessage());
        }
        
        return false;
    }
    
    /**
     * 加载商业版服务
     * @return 商业版服务实例
     */
    private EnterpriseService loadCommercialService() throws Exception {
        logger.info("正在加载商业版企业服务...");
        
        // 尝试从JAR包加载
        EnterpriseService service = loadFromJar("com.terrabase.enterprise.impl.commercial.CommercialEnterpriseServiceImpl");
        
        if (service == null) {
            // 如果JAR包加载失败，尝试从类路径加载
            service = loadFromClasspath("com.terrabase.enterprise.impl.commercial.CommercialEnterpriseServiceImpl");
        }
        
        if (service == null) {
            throw new RuntimeException("无法加载商业版企业服务");
        }
        
        return service;
    }
    
    /**
     * 加载开源版服务
     * @return 开源版服务实例
     */
    private EnterpriseService loadOpenSourceService() throws Exception {
        logger.info("正在加载开源版企业服务...");
        
        // 直接实例化开源版实现（通过直接依赖）
        OpenEnterpriseServiceImpl service = new OpenEnterpriseServiceImpl();
        
        // 从Spring容器中获取KmcConfig并手动注入
        try {
            KmcConfig kmcConfig = applicationContext.getBean(KmcConfig.class);
            if (kmcConfig != null) {
                logger.info("从Spring容器中获取KmcConfig并注入到开源版服务");
                // 使用反射注入KmcConfig
                java.lang.reflect.Field kmcConfigField = OpenEnterpriseServiceImpl.class.getDeclaredField("kmcConfig");
                kmcConfigField.setAccessible(true);
                kmcConfigField.set(service, kmcConfig);
            } else {
                logger.warn("Spring容器中没有找到KmcConfig Bean");
            }
        } catch (Exception e) {
            logger.warn("注入KmcConfig失败: {}", e.getMessage());
        }
        
        if (service == null) {
            throw new RuntimeException("无法加载开源版企业服务");
        }
        
        return service;
    }
    
    /**
     * 从JAR包加载服务（通用方法）
     * @param className 类名
     * @return 服务实例
     */
    private Object loadServiceFromJar(String className) {
        try {
            String jarFileName = getJarFileName(className);
            File jarFile = new File(jarPath, jarFileName);
            
            if (!jarFile.exists()) {
                logger.warn("JAR文件不存在: {}", jarFile.getAbsolutePath());
                return null;
            }
            
            logger.info("从JAR文件加载: {}", jarFile.getAbsolutePath());
            
            URL jarUrl = jarFile.toURI().toURL();
            URLClassLoader classLoader = new URLClassLoader(new URL[]{jarUrl}, this.getClass().getClassLoader());
            
            Class<?> clazz = classLoader.loadClass(className);
            
            // 仅使用默认构造函数创建实例
            Constructor<?> defaultConstructor = clazz.getDeclaredConstructor();
            defaultConstructor.setAccessible(true);
            Object instance = defaultConstructor.newInstance();
            logger.info("使用默认构造函数创建商业版服务实例");
            
            return instance;
            
        } catch (Exception e) {
            logger.warn("从JAR包加载失败: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 从类路径加载服务（通用方法）
     * @param className 类名
     * @return 服务实例
     */
    private Object loadServiceFromClasspath(String className) {
        try {
            logger.info("从类路径加载: {}", className);
            
            Class<?> clazz = Class.forName(className);
            
            // 仅使用默认构造函数创建实例
            Constructor<?> defaultConstructor = clazz.getDeclaredConstructor();
            defaultConstructor.setAccessible(true);
            Object instance = defaultConstructor.newInstance();
            logger.info("使用默认构造函数创建服务实例");
            
            // 手动注入依赖
            injectDependencies(instance, className);
            
            return instance;
            
        } catch (Exception e) {
            logger.warn("从类路径加载失败: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 从JAR包加载服务
     * @param className 类名
     * @return 服务实例
     */
    private EnterpriseService loadFromJar(String className) {
        return (EnterpriseService) loadServiceFromJar(className);
    }
    
    /**
     * 从类路径加载服务
     * @param className 类名
     * @return 服务实例
     */
    private EnterpriseService loadFromClasspath(String className) {
        return (EnterpriseService) loadServiceFromClasspath(className);
    }
    
    /**
     * 手动注入依赖
     * @param instance 服务实例
     * @param className 类名
     */
    private void injectDependencies(Object instance, String className) {
        try {
            // 为 OpenCryptoServiceImpl 注入 KmcConfig
            if (className.contains("OpenCryptoServiceImpl")) {
                try {
                    KmcConfig kmcConfig = applicationContext.getBean(KmcConfig.class);
                    if (kmcConfig != null) {
                        java.lang.reflect.Field kmcConfigField = instance.getClass().getDeclaredField("kmcConfig");
                        kmcConfigField.setAccessible(true);
                        kmcConfigField.set(instance, kmcConfig);
                        logger.info("成功注入 KmcConfig 到 OpenCryptoServiceImpl");
                    } else {
                        logger.warn("Spring容器中没有找到 KmcConfig Bean");
                    }
                } catch (Exception e) {
                    logger.warn("注入 KmcConfig 失败: {}", e.getMessage());
                }
            }
            
            // 为其他服务注入依赖（如果需要）
            // 可以在这里添加其他服务的依赖注入逻辑
            
        } catch (Exception e) {
            logger.warn("依赖注入失败: {}", e.getMessage());
        }
    }
    
    /**
     * 根据类名获取JAR文件名
     * @param className 类名
     * @return JAR文件名
     */
    private String getJarFileName(String className) {
        if (className.contains("commercial")) {
            return "enterprise-impl-commercial-1.0.0.jar";
        } else if (className.contains("open")) {
            return "enterprise-impl-open-1.0.0.jar";
        }
        return "unknown.jar";
    }

    
    /**
     * 清理缓存
     */
    public void clearCache() {
        logger.info("清理服务缓存");
        serviceInstances.clear();
    }
    
    /**
     * 获取当前企业模式
     * @return 企业模式
     */
    public String getEnterpriseMode() {
        return isCommercialJarAvailable() ? "commercial" : "open";
    }
}
