package com.terrabase.enterprise.impl.commercial.config;


import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.loadbalancer.LoadBalancerAlgorithm;
import com.alibaba.cloud.nacos.loadbalancer.DefaultLoadBalancerAlgorithm;
import com.alibaba.cloud.nacos.loadbalancer.NacosLoadBalancer;
import com.alibaba.cloud.nacos.util.InetIPv6Utils;

import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClientsProperties;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LoadBalancerClientFactory配置类
 * 规避Nacos在使用FeignClient客户端下无法正确获取Nacos实例地址的问题
 *
 * @author Yehong Pan
 * @since 2025-10-10
 */
@Configuration
@EnableConfigurationProperties(LoadBalancerClientsProperties.class)
public class
LoadBalancerClientFactoryConfig {
    @Resource
    private NacosDiscoveryProperties discoveryProperties;

    @Resource
    private InetIPv6Utils inetIPv6Utils;

    /**
     * 获取LoadBalancerClientFactory对象
     * @param properties properties
     * @return LoadBalancerClientFacotry对象
     */
    @Bean
    @ConditionalOnMissingBean
    public LoadBalancerClientFactory loadBalancerClientFactory(LoadBalancerClientsProperties properties) {
        return new CubeLoadBalancerClientFactory(properties, discoveryProperties, inetIPv6Utils);
    }

    private static class CubeLoadBalancerClientFactory extends LoadBalancerClientFactory {
        private static final Map<String, ReactiveLoadBalancer<ServiceInstance>> SERVICE_INSTANCE_MAP =
                new ConcurrentHashMap<>();

        private final NacosDiscoveryProperties discoveryProperties;

        private final InetIPv6Utils inetIPv6Utils;

        /**
         * 构造函数
         *
         * @param properties springcloud负载均衡环境参数
         * @param discoveryProperties nacos环境配置参数
         * @param inetIPv6Utils 工具
         */
        public CubeLoadBalancerClientFactory(LoadBalancerClientsProperties properties,
                                             NacosDiscoveryProperties discoveryProperties,
                                             InetIPv6Utils inetIPv6Utils) {
            super(properties);
            this.discoveryProperties = discoveryProperties;
            this.inetIPv6Utils = inetIPv6Utils;
        }

        @Override
        public GenericApplicationContext createContext(String name) {
            ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            GenericApplicationContext context = super.createContext(name);
            Thread.currentThread().setContextClassLoader(originalClassLoader);
            return context;
        }

        @Override
        public ReactiveLoadBalancer<ServiceInstance> getInstance(String serviceId) {
            Map<String, LoadBalancerAlgorithm> loadBalancerAlgorithmMap = new HashMap<>();
            loadBalancerAlgorithmMap.put("defaultServiceId", new DefaultLoadBalancerAlgorithm());
            return SERVICE_INSTANCE_MAP.computeIfAbsent(serviceId, key-> {
                NacosLoadBalancer nacosLoadBalancer = new NacosLoadBalancer(
                        getLazyProvider(serviceId, ServiceInstanceListSupplier.class), serviceId, discoveryProperties,
                        inetIPv6Utils, new ArrayList<>(), loadBalancerAlgorithmMap);
                nacosLoadBalancer.init();
                return nacosLoadBalancer;
            });
        }
    }

}
