/*---------------------------------------------------------------------------------------------
 *  Copyright (c) 2025 Huawei Technologies Co., Ltd. All rights reserved.
 *  This file is a part of the ModelEngine Project.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/

package com.terrabase.enterprise.api.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
        
/**
 * Spring JAR 加载器管理器 - 用于管理多个 SpringJarLoader 实例
 * 支持根据 JAR 路径和基础包动态创建和获取 SpringJarLoader 实例
 * 
 * @author songyongtan
 * @since 2026-02-25
 */
public class SpringJarLoaderManager {

    private static final Logger logger = LoggerFactory.getLogger(SpringJarLoaderManager.class);

    private static volatile SpringJarLoaderManager instance;

    private final Map<String, SpringJarLoader> loaders = new ConcurrentHashMap<>();

    private SpringJarLoaderManager() {
    }

    public static SpringJarLoaderManager getInstance() {
        if (instance == null) {
            synchronized (SpringJarLoaderManager.class) {
                if (instance == null) {
                    instance = new SpringJarLoaderManager();
                }
            }
        }
        return instance;
    }

    public SpringJarLoader getOrCreateLoader(String jarPath, String... basePackages) {
        String key = jarPath + ":" + String.join(",", basePackages);

        return loaders.computeIfAbsent(key, k -> {
            try {
                logger.info("创建新的 SpringJarLoader，路径: {}, 包: {}", jarPath, String.join(",", basePackages));
                return new SpringJarLoader(jarPath, basePackages);
            } catch (Exception e) {
                logger.error("创建 SpringJarLoader 失败", e);
                throw new RuntimeException("创建 SpringJarLoader 失败: " + jarPath, e);
            }
        });
    }

    public SpringJarLoader getLoader(String jarPath, String... basePackages) {
        String key = jarPath + ":" + String.join(",", basePackages);
        return loaders.get(key);
    }

    public void removeLoader(String jarPath, String... basePackages) {
        String key = jarPath + ":" + String.join(",", basePackages);
        SpringJarLoader loader = loaders.remove(key);
        if (loader != null) {
            try {
                loader.close();
                logger.info("已移除并关闭 SpringJarLoader: {}", key);
            } catch (Exception e) {
                logger.warn("关闭 SpringJarLoader 失败: {}", key, e);
            }
        }
    }

    public void clearAll() {
        logger.info("清理所有 SpringJarLoader");
        for (Map.Entry<String, SpringJarLoader> entry : loaders.entrySet()) {
            try {
                entry.getValue().close();
            } catch (Exception e) {
                logger.warn("关闭 SpringJarLoader 失败: {}", entry.getKey(), e);
            }
        }
        loaders.clear();
    }
}
