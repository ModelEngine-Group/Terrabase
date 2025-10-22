package com.terrabase.sdk.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Terrabase SDK 配置类
 * 支持外部配置注入，解决SDK以JAR包形式使用时配置隔离问题
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
public class TerrabaseSDKConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(TerrabaseSDKConfig.class);
    
    // ==================== Nacos 配置（仅商业版使用） ====================
    private String nacosServerAddr = "https://consulservice:18302";  // 默认Nacos服务器地址
    private String nacosUsername = "consul";  // 默认用户名
    private String nacosPassword;  // 密码通过脚本自动获取
    private String nacosPort = "8080";
    private String nacosIp = "terrabase";  // 默认服务注册IP为terrabase
    private boolean nacosSecure = true;
    private String nacosClusterName = "DEFAULT";
    private boolean nacosDiscoveryEnabled = false;  // 默认关闭，仅商业版启用
    private boolean nacosRegisterEnabled = false;   // 默认关闭，仅商业版启用
    
    // ==================== SSL 配置 ====================
    private boolean tlsEnabled = true;  // 默认启用TLS
    private boolean clientAuth = true;   // 默认启用客户端认证
    private String trustCertPath = "/opt/huawei/fce/runtime/security/server_cert/nacos/nacos.crt";  // 默认证书路径
    
    // ==================== 服务配置 ====================
    private String serviceName;
    private String servicePort = "8008";  // 默认服务端口为8008
    
    // ==================== JAR包路径配置 ====================
    private String jarPath = "./lib";
    
    // ==================== 认证配置 ====================
    private String machineToken;
    private String authToken;
    
    // ==================== 其他配置 ====================
    private Map<String, String> customProperties = new HashMap<>();
    
    /**
     * 默认构造函数
     */
    public TerrabaseSDKConfig() {
    }
    
    /**
     * 创建默认配置（开源版）
     * 开源版不启用Nacos服务注册和发现
     * @return 默认配置实例
     */
    public static TerrabaseSDKConfig createDefault() {
        TerrabaseSDKConfig config = new TerrabaseSDKConfig();
        // 开源版默认不启用Nacos
        config.setNacosDiscoveryEnabled(false);
        config.setNacosRegisterEnabled(false);
        return config;
    }
    
    /**
     * 创建商业版配置
     * 商业版启用Nacos服务注册和发现
     * @param nacosServerAddr Nacos服务器地址
     * @param nacosUsername Nacos用户名
     * @param nacosPassword Nacos密码
     * @return 商业版配置实例
     */
    public static TerrabaseSDKConfig createCommercial(String nacosServerAddr, String nacosUsername, String nacosPassword) {
        TerrabaseSDKConfig config = new TerrabaseSDKConfig();
        config.setNacosServerAddr(nacosServerAddr);
        config.setNacosUsername(nacosUsername);
        config.setNacosPassword(nacosPassword);
        // 商业版启用Nacos
        config.setNacosDiscoveryEnabled(true);
        config.setNacosRegisterEnabled(true);
        return config;
    }
    
    /**
     * 创建带默认值的商业版配置
     * 使用默认的Nacos服务器地址和用户名，密码通过脚本自动获取
     * @return 商业版配置实例
     */
    public static TerrabaseSDKConfig createCommercialWithDefaults() {
        TerrabaseSDKConfig config = new TerrabaseSDKConfig();
        // 使用默认值，密码通过脚本自动获取（带重试机制）
        config.setNacosPassword(getPasswordWithFallback());
        // 商业版启用Nacos
        config.setNacosDiscoveryEnabled(true);
        config.setNacosRegisterEnabled(true);
        return config;
    }
    
    // ==================== Getter/Setter 方法 ====================
    
    public String getNacosServerAddr() {
        return nacosServerAddr;
    }
    
    public void setNacosServerAddr(String nacosServerAddr) {
        this.nacosServerAddr = nacosServerAddr;
    }
    
    public String getNacosUsername() {
        return nacosUsername;
    }
    
    public void setNacosUsername(String nacosUsername) {
        this.nacosUsername = nacosUsername;
    }
    
    public String getNacosPassword() {
        return nacosPassword;
    }
    
    public void setNacosPassword(String nacosPassword) {
        this.nacosPassword = nacosPassword;
    }
    
    public String getNacosPort() {
        return nacosPort;
    }
    
    public void setNacosPort(String nacosPort) {
        this.nacosPort = nacosPort;
    }
    
    public String getNacosIp() {
        return nacosIp;
    }
    
    public void setNacosIp(String nacosIp) {
        this.nacosIp = nacosIp;
    }
    
    public boolean isNacosSecure() {
        return nacosSecure;
    }
    
    public void setNacosSecure(boolean nacosSecure) {
        this.nacosSecure = nacosSecure;
    }
    
    public String getNacosClusterName() {
        return nacosClusterName;
    }
    
    public void setNacosClusterName(String nacosClusterName) {
        this.nacosClusterName = nacosClusterName;
    }
    
    public boolean isNacosDiscoveryEnabled() {
        return nacosDiscoveryEnabled;
    }
    
    public void setNacosDiscoveryEnabled(boolean nacosDiscoveryEnabled) {
        this.nacosDiscoveryEnabled = nacosDiscoveryEnabled;
    }
    
    public boolean isNacosRegisterEnabled() {
        return nacosRegisterEnabled;
    }
    
    public void setNacosRegisterEnabled(boolean nacosRegisterEnabled) {
        this.nacosRegisterEnabled = nacosRegisterEnabled;
    }
    
    public boolean isTlsEnabled() {
        return tlsEnabled;
    }
    
    public void setTlsEnabled(boolean tlsEnabled) {
        this.tlsEnabled = tlsEnabled;
    }
    
    public boolean isClientAuth() {
        return clientAuth;
    }
    
    public void setClientAuth(boolean clientAuth) {
        this.clientAuth = clientAuth;
    }
    
    public String getTrustCertPath() {
        return trustCertPath;
    }
    
    public void setTrustCertPath(String trustCertPath) {
        this.trustCertPath = trustCertPath;
    }
    
    public String getServiceName() {
        return serviceName;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public String getServicePort() {
        return servicePort;
    }
    
    public void setServicePort(String servicePort) {
        this.servicePort = servicePort;
    }
    
    public String getJarPath() {
        return jarPath;
    }
    
    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }
    
    public String getMachineToken() {
        return machineToken;
    }
    
    public void setMachineToken(String machineToken) {
        this.machineToken = machineToken;
    }
    
    public String getAuthToken() {
        return authToken;
    }
    
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    
    public Map<String, String> getCustomProperties() {
        return customProperties;
    }
    
    public void setCustomProperties(Map<String, String> customProperties) {
        this.customProperties = customProperties;
    }
    
    /**
     * 添加自定义属性
     * @param key 属性键
     * @param value 属性值
     */
    public void addCustomProperty(String key, String value) {
        this.customProperties.put(key, value);
    }
    
    /**
     * 获取自定义属性
     * @param key 属性键
     * @return 属性值
     */
    public String getCustomProperty(String key) {
        return this.customProperties.get(key);
    }
    
    /**
     * 通过脚本获取Nacos密码
     * 执行脚本获取加密密码并解密
     * @return 解密后的密码
     */
    public static String getPasswordFromScript() {
        try {
            logger.info("开始执行密码获取脚本...");
            
            // 执行脚本获取加密密码
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", 
                "grep keypass_tomcat /opt/huawei/fce/runtime/security/server_cert/nacos/nacos.conf | awk -F= '{print $2}'");
            Process process = pb.start();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String encryptedPassword = reader.readLine();
            reader.close();
            
            if (encryptedPassword == null || encryptedPassword.trim().isEmpty()) {
                logger.warn("无法获取加密密码，使用默认密码");
                return "default-password";
            }
            
            logger.info("成功获取加密密码，开始解密...");
            
            // 执行Python脚本解密
            ProcessBuilder pythonPb = new ProcessBuilder("python", "-c", 
                "import kmc.kmc as K; import os; os.environ['KMC_DATA_USER']='tomcat'; nacos_pass='" + 
                encryptedPassword.trim() + "'; plain_nacos_pass=K.API().decrypt(0,nacos_pass); print(plain_nacos_pass)");
            Process pythonProcess = pythonPb.start();
            
            BufferedReader pythonReader = new BufferedReader(new InputStreamReader(pythonProcess.getInputStream()));
            String decryptedPassword = pythonReader.readLine();
            pythonReader.close();
            
            if (decryptedPassword == null || decryptedPassword.trim().isEmpty()) {
                logger.warn("密码解密失败，使用默认密码");
                return "default-password";
            }
            
            logger.info("密码获取成功");
            return decryptedPassword.trim();
            
        } catch (Exception e) {
            logger.error("密码获取脚本执行失败，使用默认密码", e);
            return "default-password";
        }
    }
    
    /**
     * 获取密码（带重试机制）
     * 如果脚本获取失败，会尝试从环境变量获取
     * @return 密码
     */
    public static String getPasswordWithFallback() {
        // 首先尝试从脚本获取
        String password = getPasswordFromScript();
        
        // 如果脚本获取失败，尝试从环境变量获取
        if ("default-password".equals(password)) {
            String envPassword = System.getenv("NACOS_PASSWORD");
            if (envPassword != null && !envPassword.trim().isEmpty()) {
                logger.info("从环境变量获取密码");
                return envPassword;
            }
        }
        
        return password;
    }
    
    /**
     * 验证配置是否有效
     * @return 是否有效
     */
    public boolean isValid() {
        if (nacosDiscoveryEnabled) {
            return nacosServerAddr != null && !nacosServerAddr.trim().isEmpty();
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "TerrabaseSDKConfig{" +
                "nacosServerAddr='" + nacosServerAddr + '\'' +
                ", nacosUsername='" + nacosUsername + '\'' +
                ", nacosDiscoveryEnabled=" + nacosDiscoveryEnabled +
                ", nacosRegisterEnabled=" + nacosRegisterEnabled +
                ", serviceName='" + serviceName + '\'' +
                ", jarPath='" + jarPath + '\'' +
                '}';
    }
}
