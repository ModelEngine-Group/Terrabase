package com.terrabase.enterprise.impl.open;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 开源版企业服务集成测试（适配当前API）
 */
class OpenEnterpriseServiceImplIntegrationTest {

    private OpenEnterpriseServiceImpl enterpriseService;

    @BeforeEach
    void setUp() {
        enterpriseService = new OpenEnterpriseServiceImpl();
    }

    @Test
    void testBasicServiceOperations() {
        assertEquals("Open Source Enterprise Service", enterpriseService.getServiceName());
        assertEquals("1.0.0-open", enterpriseService.getServiceVersion());
        assertEquals("open", enterpriseService.getServiceType());
        
        String healthStatus = enterpriseService.getHealthStatus();
        assertNotNull(healthStatus);
        assertTrue(healthStatus.contains("正常"));
    }

    @Test
    void testServiceMetadata() {
        assertEquals("Open Source Enterprise Service", enterpriseService.getServiceName());
        assertEquals("1.0.0-open", enterpriseService.getServiceVersion());
        assertEquals("open", enterpriseService.getServiceType());
        
        assertNotNull(enterpriseService.getServiceName());
        assertNotNull(enterpriseService.getServiceVersion());
        assertNotNull(enterpriseService.getServiceType());
    }

    @Test
    void testHealthStatus() {
        String healthStatus = enterpriseService.getHealthStatus();
        assertNotNull(healthStatus);
        assertTrue(healthStatus.contains("开源版"));
        assertTrue(healthStatus.contains("正常"));
    }

    @Test
    void testServiceInformationConsistency() {
        String serviceName1 = enterpriseService.getServiceName();
        String serviceName2 = enterpriseService.getServiceName();
        assertEquals(serviceName1, serviceName2);
        
        String serviceVersion1 = enterpriseService.getServiceVersion();
        String serviceVersion2 = enterpriseService.getServiceVersion();
        assertEquals(serviceVersion1, serviceVersion2);
        
        String serviceType1 = enterpriseService.getServiceType();
        String serviceType2 = enterpriseService.getServiceType();
        assertEquals(serviceType1, serviceType2);
    }
}