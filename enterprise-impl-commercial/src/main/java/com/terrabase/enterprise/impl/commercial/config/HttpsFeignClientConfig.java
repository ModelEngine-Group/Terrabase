package com.terrabase.enterprise.impl.commercial.config;

import feign.Client;
import feign.codec.ErrorDecoder;
import feign.Retryer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Collections;

/**
 * Feign配置
 * 支持 HTTPS、负载均衡和 Token 重试机制
 */
@Configuration
@ConditionalOnClass(LoadBalancerClientFactory.class)
public class HttpsFeignClientConfig {
    
    @Autowired(required = false)
    private LoadBalancerClient loadBalancerClient;
    
    @Autowired
    private TokenRetryErrorDecoder tokenRetryErrorDecoder;
    
    @Autowired
    private TokenRetryer tokenRetryer;
    
    @Bean
    public Client feignClient(LoadBalancerClientFactory loadBalancerClientFactory)
    throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext instance = SSLContext.getInstance("TLSv1.2");
        instance.init(null, new TrustManager[]{getX509TrustManager()}, SecureRandom.getInstanceStrong());
        Client.Default client = new Client.Default(instance.getSocketFactory(), (s, sslSession) -> true);
        
        // 如果 LoadBalancerClient 可用，使用负载均衡客户端
        if (loadBalancerClient != null) {
            return new FeignBlockingLoadBalancerClient(client, loadBalancerClient, loadBalancerClientFactory,
                    Collections.emptyList());
        } else {
            // 否则返回普通的 HTTPS 客户端
            return client;
        }
    }
    
    @Bean
    public ErrorDecoder errorDecoder() {
        return tokenRetryErrorDecoder;
    }
    
    @Bean
    public Retryer retryer() {
        return tokenRetryer;
    }

    private static X509TrustManager getX509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }
        };
    }
}
