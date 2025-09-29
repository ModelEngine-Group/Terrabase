package com.terrabase.enterprise.api;

import com.terrabase.enterprise.api.dto.*;

/**
 * 加解密服务接口
 * 提供数据加密和解密功能
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
public interface CryptoService {
    
    /**
     * 获取服务名称
     * @return 服务名称
     */
    String getServiceName();
    
    /**
     * 获取服务版本
     * @return 服务版本
     */
    String getServiceVersion();
    
    /**
     * 获取服务类型（commercial 或 open）
     * @return 服务类型
     */
    String getServiceType();

    /**
     * 获取服务健康状态
     * @return 健康状态信息
     */
    String getHealthStatus();

    /**
     * 启动服务
     * 将服务的running状态设置为true
     */
    void start();

    /**
     * 停止服务
     * 将服务的running状态设置为false
     */
    void stop();

    /**
     * 检查服务是否正在运行
     * @return true如果服务正在运行
     */
    boolean isRunning();

    /**
     * 加密接口
     * @param plaintext 明文数据
     * @param algorithm 加密算法，如果为null则使用默认AES算法
     * @return 密文数据
     */
    String encrypt(String plaintext, CryptoAlgorithm algorithm);

    /**
     * 解密接口
     * @param ciphertext 密文数据
     * @param algorithm 解密算法，如果为null则使用默认AES算法
     * @return 明文数据
     */
    String decrypt(String ciphertext, CryptoAlgorithm algorithm);
}
