/*---------------------------------------------------------------------------------------------
 *  Copyright (c) 2025 Huawei Technologies Co., Ltd. All rights reserved.
 *  This file is a part of the ModelEngine Project.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/

package com.terrabase.enterprise.api.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.Closeable;
import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spring JAR 加载器 - 用于加载独立的JAR包中的Spring Bean
 * 支持插件式加载，优先从父类加载器加载Spring等框架类，从插件JAR加载业务类
 * 
 * @author songyongtan
 * @since 2026-02-25
 */
public class SpringJarLoader implements Closeable {

    private static final Logger logger = LoggerFactory.getLogger(SpringJarLoader.class);

    private final String jarPath;
    private final AnnotationConfigApplicationContext context;
    private final PluginClassLoader classLoader;
    private final Map<String, Object> beanCache = new ConcurrentHashMap<>();

    public SpringJarLoader(String jarPath, String... basePackages) throws Exception {
        this.jarPath = jarPath != null ? jarPath : StandaloneJarLoadUtil.findJarPath();

        File jarFile = new File(this.jarPath);
        if (!jarFile.exists() || !jarFile.isDirectory()) {
            throw new IllegalArgumentException("JAR路径不存在或不是目录: " + this.jarPath);
        }

        URL[] urls = findJarUrls(jarFile);
        if (urls == null || urls.length == 0) {
            throw new IllegalArgumentException("未找到任何JAR文件在路径: " + this.jarPath);
        }

        // 使用 PluginClassLoader，传入当前线程的类加载器作为父加载器
        this.classLoader = new PluginClassLoader(urls, Thread.currentThread().getContextClassLoader());

        // 创建 Spring 上下文，使用 Feign 配置类
        this.context = new AnnotationConfigApplicationContext();
        this.context.setClassLoader(this.classLoader);
        this.context.setResourceLoader(new DefaultResourceLoader(this.classLoader));

        // 注册 Feign 配置类
        if (basePackages != null && basePackages.length > 0) {
            registerFeignConfiguration(basePackages);
        }

        this.context.refresh();
        logger.info("SpringJarLoader 初始化完成，加载了 {} 个JAR文件", urls.length);
    }

    private URL[] findJarUrls(File dir) {
        File[] jarFiles = dir.listFiles((d, name) -> name.endsWith(".jar"));
        if (jarFiles == null || jarFiles.length == 0) {
            return new URL[0];
        }

        URL[] urls = new URL[jarFiles.length];
        for (int i = 0; i < jarFiles.length; i++) {
            try {
                urls[i] = jarFiles[i].toURI().toURL();
                logger.debug("找到JAR文件: {}", jarFiles[i].getName());
            } catch (Exception e) {
                logger.warn("转换JAR文件URL失败: {}", jarFiles[i].getName(), e);
            }
        }
        return urls;
    }

    /**
     * 注册 Feign 配置类
     */
    private void registerFeignConfiguration(String... basePackages) {
        // 创建动态 Feign 配置类
        context.registerBean(FeignConfiguration.class, () -> new FeignConfiguration(basePackages));
        logger.info("已注册 Feign 配置，扫描包: {}", String.join(", ", basePackages));
    }

    /**
     * Feign 配置类
     */
    @Configuration
    @EnableFeignClients
    public static class FeignConfiguration {
        private final String[] basePackages;

        public FeignConfiguration(String... basePackages) {
            this.basePackages = basePackages;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        String key = requiredType.getName();

        T cachedBean = (T) beanCache.get(key);
        if (cachedBean != null) {
            return cachedBean;
        }

        try {
            Class<?> isolatedClass = classLoader.loadClass(requiredType.getName());
            Object bean = context.getBean(isolatedClass);
            beanCache.put(key, bean);
            return (T) bean;
        } catch (ClassNotFoundException e) {
            logger.warn("在隔离容器中未找到类: {}", requiredType.getName());
            return null;
        } catch (Exception e) {
            logger.error("从隔离容器获取Bean失败: {}", requiredType.getName(), e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(String name) {
        try {
            return (T) context.getBean(name);
        } catch (Exception e) {
            logger.error("从隔离容器获取Bean失败: {}", name, e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(String name, Class<T> requiredType) {
        try {
            Object bean = context.getBean(name, requiredType);
            return (T) bean;
        } catch (Exception e) {
            logger.error("从隔离容器获取Bean失败: {}", name, e);
            return null;
        }
    }

    public boolean containsBean(String name) {
        return context.containsBean(name);
    }

    public String[] getBeanNamesForType(Class<?> type) {
        return context.getBeanNamesForType(type);
    }

    public PluginClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public void close() {
        if (context != null && context.isActive()) {
            context.close();
            logger.info("SpringJarLoader 已关闭");
        }
    }
}
