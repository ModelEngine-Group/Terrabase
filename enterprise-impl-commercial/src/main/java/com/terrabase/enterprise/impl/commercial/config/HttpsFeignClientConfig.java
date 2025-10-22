package com.terrabase.enterprise.impl.commercial.config;

import feign.Client;

import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
 */
@Configuration
public class HttpsFeignClientConfig {
    @Bean
    public Client feignClient(LoadBalancerClient loadBalancerClient,
                              LoadBalancerClientFactory loadBalancerClientFactory)
    throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext instance = SSLContext.getInstance("TLSv1.2");
        instance.init(null, new TrustManager[]{getX509TrustManager()}, SecureRandom.getInstanceStrong());
        Client.Default client = new Client.Default(instance.getSocketFactory(), (s, sslSession) -> true);
        return new FeignBlockingLoadBalancerClient(client, loadBalancerClient, loadBalancerClientFactory,
                Collections.emptyList());
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
