package com.terrabase.sdk;

import com.terrabase.enterprise.api.*;
import com.terrabase.enterprise.api.CryptoAlgorithm;
import com.terrabase.enterprise.api.dto.*;
import com.terrabase.enterprise.api.response.ResultVo;
import com.terrabase.sdk.config.TerrabaseSDKConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Terrabase SDK 主入口类
 * 提供统一的静态调用接口，支持所有企业服务功能
 * 
 * <h3>使用方式</h3>
 * <pre>
 * // 方式1：直接使用静态方法（最简洁）
 * String encrypted = TerrabaseSDK.cryptoService().encrypt("hello", CryptoAlgorithm.AES, "user1");
 * String decrypted = TerrabaseSDK.cryptoService().decrypt(encrypted, CryptoAlgorithm.AES, "user1");
 * 
 * // 方式2：通过getInstance()获取实例后调用
 * String encrypted = TerrabaseSDK.getInstance().crypto().encrypt("hello", CryptoAlgorithm.AES, "user1");
 * String decrypted = TerrabaseSDK.getInstance().crypto().decrypt(encrypted, CryptoAlgorithm.AES, "user1");
 * 
 * // 方式3：先初始化再调用
 * TerrabaseSDK sdk = TerrabaseSDK.init();
 * String encrypted = sdk.crypto().encrypt("hello", CryptoAlgorithm.AES, "user1");
 * ResultVo&lt;List&lt;LoginUserDto&gt;&gt; users = sdk.userManagement().getCurrentUserInfo();
 * 
 * // 日志服务
 * TerrabaseSDK.logService().log("INFO", "用户登录成功", "user1");
 * TerrabaseSDK.logService().log("ERROR", "系统异常", "user1");
 * 
 * // 用户管理服务
 * ResultVo&lt;List&lt;LoginUserDto&gt;&gt; users = TerrabaseSDK.userManagementService().getCurrentUserInfo();
 * 
 * // 证书管理服务
 * ResultVo&lt;CertificateDto&gt; cert = TerrabaseSDK.certificateService().generateCertificate(request);
 * ResultVo&lt;Boolean&gt; valid = TerrabaseSDK.certificateService().validateCertificate("cert123");
 * 
 * // 监控告警服务
 * ResultVo&lt;Boolean&gt; alert = TerrabaseSDK.monitoringService().sendAlert(alertRequest);
 * ResultVo&lt;List&lt;MetricDto&gt;&gt; metrics = TerrabaseSDK.monitoringService().getMetrics("cpu_usage", "2024-01-01", "2024-01-31");
 * 
 * // 获取企业模式信息
 * String mode = TerrabaseSDK.getInstance().getEnterpriseMode();
 * String info = TerrabaseSDK.getServiceInfo();
 * </pre>
 * 
 * <h3>配置初始化方式（高级用法）</h3>
 * <pre>
 * // 使用自定义配置初始化
 * TerrabaseSDKConfig config = new TerrabaseSDKConfig();
 * config.setJarPath("/path/to/enterprise/jars");
 * TerrabaseSDK.init(config);
 * </pre>
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
public class TerrabaseSDK {
    
    private static final Logger logger = LoggerFactory.getLogger(TerrabaseSDK.class);
    
    // SDK实例缓存
    private static volatile TerrabaseSDK instance;
    private static final Object lock = new Object();
    
    // 服务实例缓存
    private final Map<String, Object> serviceCache = new ConcurrentHashMap<>();
    
    // 服务加载器
    private final StandaloneJarLoadUtil jarLoadUtil;
    
    // SDK配置
    private final TerrabaseSDKConfig config;
    
    /**
     * 私有构造函数，防止外部实例化
     */
    private TerrabaseSDK() {
        this.config = TerrabaseSDKConfig.createDefault();
        this.jarLoadUtil = new StandaloneJarLoadUtil(config.getJarPath());
        logger.info("TerrabaseSDK 实例已创建");
    }
    
    /**
     * 带配置的私有构造函数
     * @param config SDK配置
     */
    private TerrabaseSDK(TerrabaseSDKConfig config) {
        this.config = config != null ? config : TerrabaseSDKConfig.createDefault();
        this.jarLoadUtil = new StandaloneJarLoadUtil(this.config.getJarPath());
        
        // 如果是商业版且启用了Nacos，则初始化Nacos配置
        initializeNacosIfNeeded();
        
        logger.info("TerrabaseSDK 实例已创建，配置: {}", this.config);
    }
    
    /**
     * 初始化SDK
     * 此方法会检测并加载企业服务实现
     * 
     * @return SDK实例
     */
    public static TerrabaseSDK init() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new TerrabaseSDK();
                    logger.info("TerrabaseSDK 初始化完成");
                }
            }
        }
        return instance;
    }
    
    /**
     * 使用配置初始化SDK
     * 此方法会检测并加载企业服务实现
     * 
     * @param config SDK配置
     * @return SDK实例
     */
    public static TerrabaseSDK init(TerrabaseSDKConfig config) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new TerrabaseSDK(config);
                    logger.info("TerrabaseSDK 初始化完成，配置: {}", config);
                }
            }
        }
        return instance;
    }
    
    /**
     * 获取SDK实例（如果未初始化会自动初始化）
     * 
     * @return SDK实例
     */
    public static TerrabaseSDK getInstance() {
        if (instance == null) {
            return init();
        }
        return instance;
    }
    
    /**
     * 获取加解密服务
     * 
     * @return 加解密服务实例
     */
    public CryptoService crypto() {
        return getService("crypto_service", jarLoadUtil::loadCryptoService);
    }
    
    /**
     * 获取用户管理服务
     * 
     * @return 用户管理服务实例
     */
    public UserManagementService userManagement() {
        return getService("user_management_service", jarLoadUtil::loadUserManagementService);
    }
    
    /**
     * 获取日志服务
     * 
     * @return 日志服务实例
     */
    public LogService log() {
        return getService("log_service", jarLoadUtil::loadLogService);
    }
    
    /**
     * 获取证书管理服务
     * 
     * @return 证书管理服务实例
     */
    public CertificateService certificate() {
        return getService("certificate_service", jarLoadUtil::loadCertificateService);
    }
    
    /**
     * 获取监控告警服务
     * 
     * @return 监控告警服务实例
     */
    public MonitoringService monitoring() {
        return getService("monitoring_service", jarLoadUtil::loadMonitoringService);
    }
    
    
    // ==================== 静态方法（便捷调用） ====================
    
    /**
     * 获取加解密服务（静态方法）
     * 
     * @return 加解密服务实例
     */
    public static CryptoService cryptoService() {
        return getInstance().crypto();
    }
    
    /**
     * 获取用户管理服务（静态方法）
     * 
     * @return 用户管理服务实例
     */
    public static UserManagementService userManagementService() {
        return getInstance().userManagement();
    }
    
    /**
     * 获取日志服务（静态方法）
     * 
     * @return 日志服务实例
     */
    public static LogService logService() {
        return getInstance().log();
    }
    
    /**
     * 获取证书管理服务（静态方法）
     * 
     * @return 证书管理服务实例
     */
    public static CertificateService certificateService() {
        return getInstance().certificate();
    }
    
    /**
     * 获取监控告警服务（静态方法）
     * 
     * @return 监控告警服务实例
     */
    public static MonitoringService monitoringService() {
        return getInstance().monitoring();
    }
    
    
    /**
     * 获取服务信息（静态方法）
     * 返回包含企业模式等信息的字符串
     * 
     * @return 服务信息字符串
     */
    public static String getServiceInfo() {
        try {
            StringBuilder info = new StringBuilder();
            info.append("企业模式: ").append(getInstance().getEnterpriseMode()).append("\n");
            info.append("JAR路径: ").append(getInstance().getConfig().getJarPath()).append("\n");
            info.append("Nacos发现: ").append(getInstance().getConfig().isNacosDiscoveryEnabled() ? "启用" : "禁用");
            return info.toString();
        } catch (Exception e) {
            logger.error("获取服务信息失败", e);
            return "获取服务信息失败: " + e.getMessage();
        }
    }
    
    /**
     * 获取当前企业模式
     * 
     * @return 企业模式（commercial 或 open）
     */
    public String getEnterpriseMode() {
        return jarLoadUtil.getEnterpriseMode();
    }
    
    /**
     * 获取SDK配置
     * 
     * @return SDK配置
     */
    public TerrabaseSDKConfig getConfig() {
        return config;
    }
    
    /**
     * 清理服务缓存
     */
    public void clearCache() {
        serviceCache.clear();
        jarLoadUtil.clearCache();
        logger.info("SDK服务缓存已清理");
    }
    
    /**
     * 如果需要，初始化Nacos配置
     * 仅在商业版且启用Nacos时执行
     */
    private void initializeNacosIfNeeded() {
        try {
            // 检查是否为商业版
            String enterpriseMode = jarLoadUtil.getEnterpriseMode();
            if (!"commercial".equals(enterpriseMode)) {
                logger.debug("非商业版模式，跳过Nacos配置初始化");
                return;
            }
            
            // 检查是否启用了Nacos
            if (!config.isNacosDiscoveryEnabled()) {
                logger.debug("Nacos服务发现未启用，跳过Nacos配置初始化");
                return;
            }
            
            // 动态加载商业版Nacos配置类
            Class<?> nacosConfigClass = Class.forName("com.terrabase.enterprise.impl.commercial.config.CommercialNacosConfig");
            java.lang.reflect.Method initializeMethod = nacosConfigClass.getMethod("initializeFromSDKConfig", TerrabaseSDKConfig.class);
            initializeMethod.invoke(null, config);
            
            logger.info("商业版Nacos配置初始化完成");
            
        } catch (ClassNotFoundException e) {
            logger.debug("商业版Nacos配置类不存在，跳过Nacos配置初始化");
        } catch (Exception e) {
            logger.warn("初始化Nacos配置时发生异常，但不影响SDK正常使用", e);
        }
    }
    
    /**
     * 获取服务实例（带缓存）
     * 
     * @param serviceName 服务名称
     * @param loader 服务加载器
     * @return 服务实例
     */
    @SuppressWarnings("unchecked")
    private <T> T getService(String serviceName, ServiceLoader<T> loader) {
        try {
            // 先检查缓存
            T cachedService = (T) serviceCache.get(serviceName);
            if (cachedService != null) {
                return cachedService;
            }
            
            // 加载服务
            T service = loader.load();
            if (service != null) {
                serviceCache.put(serviceName, service);
                logger.debug("服务已加载并缓存: {}", serviceName);
            }
            
            return service;
            
        } catch (Exception e) {
            logger.error("加载服务失败: {}", serviceName, e);
            throw new RuntimeException("无法加载服务: " + serviceName, e);
        }
    }
    
    /**
     * 服务加载器接口
     */
    @FunctionalInterface
    private interface ServiceLoader<T> {
        T load() throws Exception;
    }
    
}
