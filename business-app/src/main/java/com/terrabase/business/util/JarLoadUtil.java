package com.terrabase.business.util;

import com.terrabase.enterprise.api.*;
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
     * 加载证书管理服务
     * @return 证书管理服务实例
     */
    public CertificateService loadCertificateService() {
        return (CertificateService) loadService("certificate_service",
            "com.terrabase.enterprise.impl.commercial.CommercialCertificateServiceImpl",
            "com.terrabase.enterprise.impl.open.OpenCertificateServiceImpl");
    }
    
    /**
     * 加载日志服务
     * @return 日志服务实例
     */
    public LogService loadLogService() {
        return (LogService) loadService("log_service",
            "com.terrabase.enterprise.impl.commercial.CommercialLogServiceImpl",
            "com.terrabase.enterprise.impl.open.OpenLogServiceImpl");
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
            Class<?> clazz = classLoader.loadClass("com.terrabase.enterprise.impl.commercial.CommercialCryptoServiceImpl");
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
            
            
            return instance;
            
        } catch (Exception e) {
            logger.warn("从类路径加载失败: {}", e.getMessage());
            return null;
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
