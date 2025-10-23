package com.terrabase.sdk.example;

import com.terrabase.enterprise.api.CryptoAlgorithm;
import com.terrabase.enterprise.api.dto.*;
import com.terrabase.enterprise.api.request.LogAttributeVo;
import com.terrabase.enterprise.api.request.RegisterEventDefineReq;
import com.terrabase.enterprise.api.request.GetEventsParams;
import com.terrabase.enterprise.api.dto.EventDefine;
import com.terrabase.enterprise.api.response.ResultVo;
import com.terrabase.sdk.TerrabaseSDK;

import java.util.List;

/**
 * Terrabase SDK 使用示例
 * 展示如何在不同场景下使用SDK
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
public class SDKUsageExample {
    
    public static void main(String[] args) {
        // 示例1：基本使用
        basicUsageExample();
        
        // 示例2：加解密操作
        cryptoExample();
        
        // 示例3：用户管理
        userManagementExample();
        
        // 示例4：企业服务信息
        enterpriseServiceExample();
        
        // 示例5：日志服务
        logServiceExample();
        
        // 示例6：证书管理
        certificateServiceExample();
        
        // 示例7：监控告警
        monitoringServiceExample();
    }
    
    /**
     * 基本使用示例
     */
    public static void basicUsageExample() {
        System.out.println("=== 基本使用示例 ===");
        
        // 方式1：通过getInstance()调用（推荐）
        String mode = TerrabaseSDK.getInstance().getEnterpriseMode();
        String serviceInfo = TerrabaseSDK.getServiceInfo();
        System.out.println("企业模式: " + mode);
        System.out.println("服务信息: " + serviceInfo);
        
        // 方式2：传统实例化方式
        TerrabaseSDK sdk = TerrabaseSDK.init();
        String sdkMode = sdk.getEnterpriseMode();
        System.out.println("当前企业模式: " + sdkMode);
        
        // 方式3：获取详细信息
        System.out.println("企业服务详细信息: " + serviceInfo);
    }
    
    /**
     * 加解密操作示例
     */
    public static void cryptoExample() {
        System.out.println("\n=== 加解密操作示例 ===");
        
        try {
            String plaintext = "Hello, Terrabase!";
            String username = "user1";
            
            // 方式1：直接使用静态方法（最简洁）
            String encrypted = TerrabaseSDK.cryptoService().encrypt(plaintext, CryptoAlgorithm.AES, username);
            System.out.println("AES加密结果: " + encrypted);
            
            String decrypted = TerrabaseSDK.cryptoService().decrypt(encrypted, CryptoAlgorithm.AES, username);
            System.out.println("AES解密结果: " + decrypted);
            
            // 方式2：RSA加密
            String rsaEncrypted = TerrabaseSDK.cryptoService().encrypt("Another message", CryptoAlgorithm.RSA, username);
            System.out.println("RSA加密结果: " + rsaEncrypted);
            
            // 方式3：传统实例化方式
            TerrabaseSDK sdk = TerrabaseSDK.init();
            String encrypted3 = sdk.crypto().encrypt("Instance method", CryptoAlgorithm.AES, username);
            System.out.println("实例方法加密结果: " + encrypted3);
            
        } catch (Exception e) {
            System.err.println("加解密操作失败: " + e.getMessage());
        }
    }
    
    /**
     * 用户管理示例
     */
    public static void userManagementExample() {
        System.out.println("\n=== 用户管理示例 ===");
        
        try {
            // 方式1：直接使用静态方法（最简洁）
            ResultVo<List<LoginUserDto>> result = TerrabaseSDK.userManagementService().getCurrentUserInfo();
            
            if ("200".equals(result.getCode())) {
                System.out.println("用户信息获取成功:");
                List<LoginUserDto> users = result.getData();
                if (users != null) {
                    for (LoginUserDto user : users) {
                        System.out.println("  用户: " + user.getUserName() + " (ID: " + user.getUserId() + ")");
                    }
                }
            } else {
                System.out.println("用户信息获取失败: " + result.getMsg());
            }
            
            // 方式2：传统实例化方式
            TerrabaseSDK sdk = TerrabaseSDK.init();
            ResultVo<List<LoginUserDto>> result2 = sdk.userManagement().getCurrentUserInfo();
            System.out.println("实例方式获取用户信息: " + (result2.getData() != null ? result2.getData().size() + "个用户" : "无数据"));
            
        } catch (Exception e) {
            System.err.println("用户管理操作失败: " + e.getMessage());
        }
    }
    
    /**
     * 企业服务信息示例
     */
    public static void enterpriseServiceExample() {
        System.out.println("\n=== 企业服务信息示例 ===");
        
        try {
            // 方式1：直接使用静态方法（最简洁）
            System.out.println("企业服务信息:");
            System.out.println(TerrabaseSDK.getServiceInfo());
            
            // 方式2：传统实例化方式
            TerrabaseSDK sdk = TerrabaseSDK.init();
            String mode = sdk.getEnterpriseMode();
            System.out.println("实例方式获取企业模式: " + mode);
            
            // 方式3：展示所有服务的调用
            System.out.println("\n所有服务调用演示:");
            System.out.println("  加解密服务: " + TerrabaseSDK.cryptoService().getClass().getSimpleName());
            System.out.println("  用户管理服务: " + TerrabaseSDK.userManagementService().getClass().getSimpleName());
            System.out.println("  日志服务: " + TerrabaseSDK.logService().getClass().getSimpleName());
            System.out.println("  证书服务: " + TerrabaseSDK.certificateService().getClass().getSimpleName());
            System.out.println("  监控服务: " + TerrabaseSDK.monitoringService().getClass().getSimpleName());
            
        } catch (Exception e) {
            System.err.println("企业服务操作失败: " + e.getMessage());
        }
    }
    
    /**
     * 日志服务示例
     */
    public static void logServiceExample() {
        System.out.println("\n=== 日志服务示例 ===");
        
        try {
            // 创建日志属性对象
            LogAttributeVo log1 = new LogAttributeVo();
            log1.setSn(1L);
            log1.setLogType("INFO");
            log1.setUsername("user1");
            log1.setOperation("登录");
            log1.setSource("web");
            log1.setTerminal("browser");
            log1.setResult("成功");
            log1.setFlag("normal");
            log1.setParamType("string");
            log1.setDetail("用户登录成功");
            
            LogAttributeVo log2 = new LogAttributeVo();
            log2.setSn(2L);
            log2.setLogType("ERROR");
            log2.setUsername("user1");
            log2.setOperation("数据库连接");
            log2.setSource("system");
            log2.setTerminal("server");
            log2.setResult("失败");
            log2.setFlag("error");
            log2.setParamType("string");
            log2.setDetail("数据库连接失败");
            
            // 批量上报日志
            List<LogAttributeVo> logs = List.of(log1, log2);
            ResultVo<Integer> result = TerrabaseSDK.logService().registerLogs(logs);
            if ("200".equals(result.getCode())) {
                System.out.println("日志上报成功，上报了 " + result.getData() + " 条日志");
            }
            
            // 注册日志国际化信息
            LogI18n logI18n = new LogI18n();
            logI18n.setCode("user.login.success");
            logI18n.setLanguage("zh-CN");
            logI18n.setContent("用户登录成功");
            
            ResultVo<Boolean> i18nResult = TerrabaseSDK.logService().registryInternational(List.of(logI18n));
            if ("200".equals(i18nResult.getCode())) {
                System.out.println("日志国际化注册成功: " + i18nResult.getData());
            }
            
        } catch (Exception e) {
            System.err.println("日志服务操作失败: " + e.getMessage());
        }
    }
    
    /**
     * 证书管理服务示例
     */
    public static void certificateServiceExample() {
        System.out.println("\n=== 证书管理服务示例 ===");
        
        try {
            // 获取证书服务列表
            ResultVo<List<CertCollectInfo>> certs = TerrabaseSDK.certificateService().listCertificateServiceList();
            if ("200".equals(certs.getCode()) && certs.getData() != null) {
                System.out.println("获取到 " + certs.getData().size() + " 个证书服务");
                for (CertCollectInfo cert : certs.getData()) {
                    System.out.println("  证书服务: " + cert.getProductName() + " (状态: " + cert.getStatus() + ")");
                }
            }
            
            // 获取License信息
            ResultVo<LicenseInfo> license = TerrabaseSDK.certificateService().getLicenseInfo();
            if ("200".equals(license.getCode()) && license.getData() != null) {
                LicenseInfo licenseInfo = license.getData();
                System.out.println("License信息:");
                System.out.println("  状态: " + licenseInfo.getStatus());
                System.out.println("  SBOM数量: " + (licenseInfo.getSboms() != null ? licenseInfo.getSboms().size() : 0));
            }
            
        } catch (Exception e) {
            System.err.println("证书管理操作失败: " + e.getMessage());
        }
    }
    
    /**
     * 监控告警服务示例
     */
    public static void monitoringServiceExample() {
        System.out.println("\n=== 监控告警服务示例 ===");
        
        try {
            // 注册告警定义
            RegisterEventDefineReq eventDefine = new RegisterEventDefineReq();
            eventDefine.setServiceName("system-service");
            eventDefine.setServiceEn("System Service");
            eventDefine.setServiceZh("系统服务");
            eventDefine.setDeleteAll(false);
            
            // 创建事件定义列表
            EventDefine eventDef = new EventDefine();
            eventDef.setEventId("system-error-001");
            eventDef.setName("系统异常");
            eventDef.setType("event");
            eventDef.setCategory("system");
            eventDef.setDescription("系统发生异常");
            eventDef.setSeverity("major");
            eventDef.setLanguage("zh-cn");
            
            eventDefine.setEventDefines(List.of(eventDef));
            
            ResultVo registerResult = TerrabaseSDK.monitoringService().registerEventDefine(eventDefine);
            if ("200".equals(registerResult.getCode())) {
                System.out.println("告警定义注册成功");
            }
            
            // 上报告警事件
            EventInfo eventInfo = new EventInfo();
            eventInfo.setEventName("系统异常");
            eventInfo.setEventType("event");
            eventInfo.setEventSubject("数据库连接超时");
            eventInfo.setEventSubjectType("system");
            eventInfo.setSeverity("major");
            eventInfo.setEventCategory("system");
            eventInfo.setStatus("Uncleared");
            eventInfo.setFirstOccurTime("2024-01-01T00:00:00Z");
            eventInfo.setParts("system");
            eventInfo.setLanguage("zh-cn");
            
            ResultVo<Boolean> sendResult = TerrabaseSDK.monitoringService().sendEvents(List.of(eventInfo));
            if ("200".equals(sendResult.getCode())) {
                System.out.println("告警事件上报成功: " + sendResult.getData());
            }
            
            // 查询告警事件
            GetEventsParams queryParams = new GetEventsParams();
            queryParams.setAlarmName("系统异常");
            queryParams.setStartTime(1704067200000L); // 2024-01-01
            queryParams.setEndTime(1706745600000L);   // 2024-01-31
            queryParams.setSeverity("major");
            queryParams.setCategory("system");
            queryParams.setEventType("Uncleared");
            queryParams.setOrderBy("first_occur_time desc");
            queryParams.setLanguage("zh-cn");
            queryParams.setPageNum(1);
            queryParams.setPageSize(10);
            
            ResultVo<EventsCollection> events = TerrabaseSDK.monitoringService().getEventsByPage(queryParams);
            if ("200".equals(events.getCode()) && events.getData() != null) {
                EventsCollection collection = events.getData();
                System.out.println("查询到 " + collection.getTotalCount() + " 条告警事件");
                if (collection.getEvents() != null) {
                    for (EventObject event : collection.getEvents()) {
                        System.out.println("  事件: " + event.getEventName() + " (" + event.getEventType() + ")");
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("监控告警操作失败: " + e.getMessage());
        }
    }
    
    /**
     * 高级使用示例：自定义JAR路径
     */
    public static void advancedUsageExample() {
        System.out.println("\n=== 高级使用示例 ===");
        
        try {
            // 使用自定义JAR路径创建SDK实例
            // 注意：这需要修改TerrabaseSDK类以支持自定义路径
            System.out.println("高级使用示例需要扩展SDK类以支持自定义配置");
            
        } catch (Exception e) {
            System.err.println("高级使用示例失败: " + e.getMessage());
        }
    }
}
