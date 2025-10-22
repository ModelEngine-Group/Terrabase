package com.terrabase.enterprise.impl.open;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 开源版企业服务基本功能测试（适配当前API）
 */
@DisplayName("开源版企业服务基本功能测试")
public class OpenEnterpriseServiceImplKmcTest {
    
    private OpenEnterpriseServiceImpl service;
    
    @BeforeEach
    void setUp() {
        service = new OpenEnterpriseServiceImpl();
    }
    
    @Test
    @DisplayName("测试服务初始化与健康状态")
    void testServiceInitialization() {
        assertEquals("Open Source Enterprise Service", service.getServiceName());
        assertEquals("1.0.0-open", service.getServiceVersion());
        assertEquals("open", service.getServiceType());
        
        String healthStatus = service.getHealthStatus();
        assertNotNull(healthStatus);
        assertTrue(healthStatus.contains("正常"));
        assertTrue(healthStatus.contains("开源版"));
    }
    
    @Test
    @DisplayName("测试服务元数据")
    void testServiceMetadata() {
        assertEquals("Open Source Enterprise Service", service.getServiceName());
        assertEquals("1.0.0-open", service.getServiceVersion());
        assertEquals("open", service.getServiceType());
        
        assertNotNull(service.getServiceName());
        assertNotNull(service.getServiceVersion());
        assertNotNull(service.getServiceType());
    }
    
    @Test
    @DisplayName("测试健康状态信息格式")
    void testHealthStatusInformation() {
        String healthStatus = service.getHealthStatus();
        assertNotNull(healthStatus);
        assertTrue(healthStatus.contains("开源版"));
        assertTrue(healthStatus.contains("正常"));
    }
}