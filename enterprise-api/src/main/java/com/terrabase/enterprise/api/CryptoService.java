package com.terrabase.enterprise.api;

import com.terrabase.enterprise.api.dto.*;

/**
 * 加解密服务接口
 * 提供数据加密和解密功能
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
public interface CryptoService {

    /**
     * 加密接口
     * @param plaintext 明文数据
     * @param algorithm 加密算法，如果为null则使用默认AES算法
     * @param username 用户名
     * @return 密文数据
     */
    String encrypt(String plaintext, CryptoAlgorithm algorithm, String username);

    /**
     * 解密接口
     * @param ciphertext 密文数据
     * @param algorithm 解密算法，如果为null则使用默认AES算法
     * @param username 用户名
     * @return 明文数据
     */
    String decrypt(String ciphertext, CryptoAlgorithm algorithm, String username);
}
