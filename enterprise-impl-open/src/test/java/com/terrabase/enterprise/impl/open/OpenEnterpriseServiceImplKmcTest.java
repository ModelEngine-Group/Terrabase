package com.terrabase.enterprise.impl.open;

import com.terrabase.enterprise.impl.open.config.KmcConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 开源版企业服务KMC功能集成测试
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
@SpringBootTest(classes = TestConfiguration.class)
@TestPropertySource(properties = {
    "terrabase.kmc.enabled=true",
    "terrabase.kmc.default-key-id=test_default",
    "terrabase.kmc.enable-audit-log=true"
})
@DisplayName("开源版企业服务KMC功能集成测试")
public class OpenEnterpriseServiceImplKmcTest {
    
    private OpenEnterpriseServiceImpl service;
    private KmcConfig kmcConfig;
    
    @BeforeEach
    void setUp() {
        service = new OpenEnterpriseServiceImpl();
        kmcConfig = new KmcConfig();
        kmcConfig.setEnabled(true);
        kmcConfig.setDefaultKeyId("test_default");
        kmcConfig.setEnableAuditLog(true);
        
        // 使用反射设置私有字段
        try {
            var field = service.getClass().getDeclaredField("kmcConfig");
            field.setAccessible(true);
            field.set(service, kmcConfig);
            
            // 启动服务
            var runningField = service.getClass().getDeclaredField("running");
            runningField.setAccessible(true);
            var running = (java.util.concurrent.atomic.AtomicBoolean) runningField.get(service);
            running.set(true);
        } catch (Exception e) {
            throw new RuntimeException("设置KMC配置失败", e);
        }
    }
    
    @Test
    @DisplayName("测试服务初始化")
    void testServiceInitialization() {
        // 验证服务信息
        assertEquals("Open Source Enterprise Service", service.getServiceName());
        assertEquals("1.0.0-open", service.getServiceVersion());
        assertEquals("open", service.getServiceType());
        
        // 验证健康状态
        String healthStatus = service.getHealthStatus();
        assertTrue(healthStatus.contains("正常"));
        assertTrue(healthStatus.contains("开源版"));
    }
    
    @Test
    @DisplayName("测试服务启动")
    void testServiceStart() {
        // 停止服务
        service.stop();
        assertFalse(service.isRunning());
        
        // 启动服务
        service.start();
        assertTrue(service.isRunning());
        
        // 验证健康状态
        String healthStatus = service.getHealthStatus();
        assertTrue(healthStatus.contains("正常"));
    }
    
    @Test
    @DisplayName("测试服务停止")
    void testServiceStop() {
        // 启动服务
        service.start();
        assertTrue(service.isRunning());
        
        // 停止服务
        service.stop();
        assertFalse(service.isRunning());
        
        // 验证健康状态
        String healthStatus = service.getHealthStatus();
        assertTrue(healthStatus.contains("停止"));
    }
    
    @Test
    @DisplayName("测试服务重启")
    void testServiceRestart() {
        // 启动服务
        service.start();
        assertTrue(service.isRunning());
        
        // 停止服务
        service.stop();
        assertFalse(service.isRunning());
        
        // 重启服务
        service.start();
        assertTrue(service.isRunning());
        
        // 验证健康状态
        String healthStatus = service.getHealthStatus();
        assertTrue(healthStatus.contains("正常"));
    }
    
    @Test
    @DisplayName("测试KMC配置")
    void testKmcConfiguration() {
        // 验证KMC配置已设置
        assertNotNull(kmcConfig);
        assertTrue(kmcConfig.isEnabled());
        assertEquals("test_default", kmcConfig.getDefaultKeyId());
        assertTrue(kmcConfig.isEnableAuditLog());
    }
    
    @Test
    @DisplayName("测试服务状态一致性")
    void testServiceStateConsistency() {
        // 测试多次调用状态的一致性
        assertTrue(service.isRunning());
        assertTrue(service.isRunning());
        
        service.stop();
        assertFalse(service.isRunning());
        assertFalse(service.isRunning());
        
        service.start();
        assertTrue(service.isRunning());
        assertTrue(service.isRunning());
    }
    
    @Test
    @DisplayName("测试健康状态信息")
    void testHealthStatusInformation() {
        String healthStatus = service.getHealthStatus();
        
        // 验证健康状态包含必要信息
        assertNotNull(healthStatus);
        assertTrue(healthStatus.contains("开源版"));
        assertTrue(healthStatus.contains("正常"));
        
        // 停止服务后验证健康状态变化
        service.stop();
        String stoppedHealthStatus = service.getHealthStatus();
        assertTrue(stoppedHealthStatus.contains("停止"));
    }
    
    @Test
    @DisplayName("测试服务元数据")
    void testServiceMetadata() {
        // 验证服务名称
        assertEquals("Open Source Enterprise Service", service.getServiceName());
        
        // 验证服务版本
        assertEquals("1.0.0-open", service.getServiceVersion());
        
        // 验证服务类型
        assertEquals("open", service.getServiceType());
        
        // 验证元数据一致性
        assertNotNull(service.getServiceName());
        assertNotNull(service.getServiceVersion());
        assertNotNull(service.getServiceType());
    }
    
    @Test
    @DisplayName("测试并发访问")
    void testConcurrentAccess() throws InterruptedException {
        // 创建多个线程同时访问服务
        Thread[] threads = new Thread[10];
        boolean[] results = new boolean[10];
        
        for (int i = 0; i < 10; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    // 模拟并发访问
                    String serviceName = service.getServiceName();
                    String serviceVersion = service.getServiceVersion();
                    String serviceType = service.getServiceType();
                    boolean isRunning = service.isRunning();
                    String healthStatus = service.getHealthStatus();
                    
                    results[index] = serviceName != null && 
                                   serviceVersion != null && 
                                   serviceType != null && 
                                   healthStatus != null;
                } catch (Exception e) {
                    results[index] = false;
                }
            });
        }
        
        // 启动所有线程
        for (Thread thread : threads) {
            thread.start();
        }
        
        // 等待所有线程完成
        for (Thread thread : threads) {
            thread.join();
        }
        
        // 验证所有线程都成功访问了服务
        for (boolean result : results) {
            assertTrue(result, "并发访问应该成功");
        }
    }
    
    @Test
    @DisplayName("测试服务生命周期")
    void testServiceLifecycle() {
        // 初始状态应该是停止的
        assertFalse(service.isRunning());
        
        // 启动服务
        service.start();
        assertTrue(service.isRunning());
        
        // 再次启动应该不会出错
        assertDoesNotThrow(() -> service.start());
        assertTrue(service.isRunning());
        
        // 停止服务
        service.stop();
        assertFalse(service.isRunning());
        
        // 再次停止应该不会出错
        assertDoesNotThrow(() -> service.stop());
        assertFalse(service.isRunning());
    }
}