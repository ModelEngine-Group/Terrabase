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

/**
 * 隔离类加载器，用于加载独立的JAR包中的类，避免类冲突
 * 
 * @author songyongtan
 * @since 2026-02-25
 */
public class IsolatedClassLoader extends URLClassLoader {

    private static final Logger logger = LoggerFactory.getLogger(IsolatedClassLoader.class);

    public IsolatedClassLoader(URL[] urls) {
        super(urls, null);
        logger.debug("IsolatedClassLoader 初始化，使用null作为父类加载器，实现完全隔离");
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            Class<?> loadedClass = findLoadedClass(name);
            if (loadedClass != null) {
                return loadedClass;
            }

            // 优先从父类加载器加载Spring框架类，避免类型转换异常
            if (name.startsWith("org.springframework.") || name.startsWith("org.apache.commons.logging.")) {
                try {
                    return super.loadClass(name, resolve);
                } catch (ClassNotFoundException e) {
                    // 父类加载器找不到，继续尝试从JAR加载
                }
            }

            try {
                return findClass(name);
            } catch (ClassNotFoundException e) {
                // 放行JDK核心类
                if (name.startsWith("java.") || name.startsWith("javax.")
                    || name.startsWith("com.sun.") || name.startsWith("sun.")
                    || name.startsWith("jdk.internal.reflect.") || name.startsWith("jdk.internal.")
                    || name.startsWith("org.slf4j.") || name.startsWith("org.apache.logging")
                    || name.startsWith("org.w3c.") || name.startsWith("org.xml.")) {
                    return super.loadClass(name, resolve);
                }
                throw e;
            }
        }
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}
