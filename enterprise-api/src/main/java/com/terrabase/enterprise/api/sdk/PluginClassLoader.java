/*---------------------------------------------------------------------------------------------
 *  Copyright (c) 2025 Huawei Technologies Co., Ltd. All rights reserved.
 *  This file is a part of the ModelEngine Project.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/

package com.terrabase.enterprise.api.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

/**
 * 插件类加载器 - 用于加载插件JAR包中的类
 * 优先从父类加载器加载Spring等框架类，从插件JAR加载业务类
 * 
 * @author songyongtan
 * @since 2026-02-25
 */
public class PluginClassLoader extends URLClassLoader {

    private static final Logger logger = LoggerFactory.getLogger(PluginClassLoader.class);

    // 需要从父类加载器加载的包前缀
    private static final Set<String> PARENT_FIRST_PACKAGES = Set.of(
        "org.springframework.",
        "org.apache.commons.logging.",
        "org.slf4j.",
        "org.apache.logging.",
        "java.",
        "javax.",
        "com.sun.",
        "sun.",
        "jdk.internal.",
        "jdk.internal.reflect.",
        "org.w3c.",
        "org.xml.",
        "com.fasterxml.jackson.",
        "io.netty.",
        "reactor.",
        "org.reactivestreams.",
        "com.terrabase.enterprise.api."  // API接口类从父类加载器加载
    );

    public PluginClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
        logger.debug("PluginClassLoader 初始化，父类加载器: {}", parent != null ? parent.getClass().getName() : "null");
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            Class<?> loadedClass = findLoadedClass(name);
            if (loadedClass != null) {
                return loadedClass;
            }

            // 检查是否应该从父类加载器加载
            if (shouldLoadFromParent(name)) {
                try {
                    Class<?> clazz = super.loadClass(name, resolve);
                    logger.trace("从父类加载器加载: {}", name);
                    return clazz;
                } catch (ClassNotFoundException e) {
                    // 父类加载器找不到，尝试从插件JAR加载
                    logger.trace("父类加载器找不到，尝试从插件JAR加载: {}", name);
                }
            }

            // 尝试从插件JAR加载
            try {
                Class<?> clazz = findClass(name);
                logger.trace("从插件JAR加载: {}", name);
                return clazz;
            } catch (ClassNotFoundException e) {
                // 如果插件JAR找不到，且不是应该从父类加载的包，则尝试父类加载器
                if (!shouldLoadFromParent(name)) {
                    return super.loadClass(name, resolve);
                }
                throw e;
            }
        }
    }

    /**
     * 判断类是否应该从父类加载器加载
     */
    private boolean shouldLoadFromParent(String className) {
        for (String prefix : PARENT_FIRST_PACKAGES) {
            if (className.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}
