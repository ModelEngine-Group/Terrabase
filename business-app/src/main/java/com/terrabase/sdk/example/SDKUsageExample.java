package com.terrabase.sdk.example;

import com.terrabase.enterprise.api.CryptoAlgorithm;
import com.terrabase.enterprise.api.dto.LoginUserDto;
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
    }
    
    /**
     * 基本使用示例
     */
    public static void basicUsageExample() {
        System.out.println("=== 基本使用示例 ===");
        
        // 初始化SDK
        TerrabaseSDK sdk = TerrabaseSDK.init();
        
        // 获取企业模式
        String mode = sdk.getEnterpriseMode();
        System.out.println("当前企业模式: " + mode);
        
        // 获取企业服务信息
        String serviceInfo = TerrabaseSDK.getServiceInfo();
        System.out.println("企业服务信息: " + serviceInfo);
    }
    
    /**
     * 加解密操作示例
     */
    public static void cryptoExample() {
        System.out.println("\n=== 加解密操作示例 ===");
        
        try {
            // 初始化SDK
            TerrabaseSDK.init();
            
            String plaintext = "Hello, Terrabase!";
            String username = "user1";
            
            // 使用静态方法快速加密
            String encrypted = TerrabaseSDK.encrypt(plaintext, CryptoAlgorithm.AES, username);
            System.out.println("加密结果: " + encrypted);
            
            // 使用静态方法快速解密
            String decrypted = TerrabaseSDK.decrypt(encrypted, CryptoAlgorithm.AES, username);
            System.out.println("解密结果: " + decrypted);
            
            // 使用实例方法
            TerrabaseSDK sdk = TerrabaseSDK.getInstance();
            String encrypted2 = sdk.crypto().encrypt("Another message", CryptoAlgorithm.RSA, username);
            System.out.println("RSA加密结果: " + encrypted2);
            
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
            // 初始化SDK
            TerrabaseSDK.init();
            
            // 获取当前用户信息
            ResultVo<List<LoginUserDto>> result = TerrabaseSDK.getCurrentUserInfo();
            
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
            // 初始化SDK
            TerrabaseSDK sdk = TerrabaseSDK.init();
            
            // 获取企业服务
            var enterpriseService = sdk.enterprise();
            
            System.out.println("企业服务信息:");
            System.out.println("  服务名称: " + enterpriseService.getServiceName());
            System.out.println("  服务版本: " + enterpriseService.getServiceVersion());
            System.out.println("  服务类型: " + enterpriseService.getServiceType());
            System.out.println("  健康状态: " + enterpriseService.getHealthStatus());
            
            // 获取其他服务
            var cryptoService = sdk.crypto();
            var userService = sdk.userManagement();
            var logService = sdk.logManagement();
            var certService = sdk.certificate();
            var monitorService = sdk.monitoring();
            
            System.out.println("所有服务已加载完成");
            
        } catch (Exception e) {
            System.err.println("企业服务操作失败: " + e.getMessage());
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
