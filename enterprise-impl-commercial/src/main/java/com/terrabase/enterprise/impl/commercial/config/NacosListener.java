package com.terrabase.enterprise.impl.commercial.config;

import com.alibaba.cloud.nacos.ConditionalOnNacosDiscoveryEnabled;
import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistryAutoConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;


/**
 * Nacos控制器
 */
@Configuration
@ConditionalOnNacosDiscoveryEnabled
@AutoConfigureAfter(NacosServiceRegistryAutoConfiguration.class)
public class NacosListener implements ApplicationContextAware {
    private NacosAutoServiceRegistration registration;

    @Autowired(required = false)
    public void setRegistration(NacosAutoServiceRegistration registration) {
        this.registration = registration;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (registration != null) {
            registration.start();
        }
    }
}
