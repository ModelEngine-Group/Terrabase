package com.terrabase.enterprise.api;

/**
 * 加密算法枚举
 * 定义系统支持的各种加密算法
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
public enum CryptoAlgorithm {
    
    /**
     * AES加密算法 - 高级加密标准
     * 支持128、192、256位密钥长度
     */
    AES("AES", "AES/GCM/NoPadding", 256),
    
    /**
     * RSA加密算法 - 非对称加密
     * 支持1024、2048、4096位密钥长度
     */
    RSA("RSA", "RSA/ECB/PKCS1Padding", 2048),
    
    /**
     * DES加密算法 - 数据加密标准
     * 支持56位密钥长度
     */
    DES("DES", "DES/CBC/PKCS5Padding", 56),
    
    /**
     * 3DES加密算法 - 三重数据加密标准
     * 支持112、168位密钥长度
     */
    TRIPLE_DES("DESede", "DESede/CBC/PKCS5Padding", 168),
    
    /**
     * Blowfish加密算法
     * 支持32-448位可变密钥长度
     */
    BLOWFISH("Blowfish", "Blowfish/CBC/PKCS5Padding", 128),
    
    /**
     * ChaCha20加密算法
     * 流密码算法
     */
    CHACHA20("ChaCha20", "ChaCha20-Poly1305", 256);
    
    private final String algorithm;
    private final String transformation;
    private final int defaultKeyLength;
    
    /**
     * 构造函数
     * @param algorithm 算法名称
     * @param transformation 转换模式
     * @param defaultKeyLength 默认密钥长度（位）
     */
    CryptoAlgorithm(String algorithm, String transformation, int defaultKeyLength) {
        this.algorithm = algorithm;
        this.transformation = transformation;
        this.defaultKeyLength = defaultKeyLength;
    }
    
    /**
     * 获取算法名称
     * @return 算法名称
     */
    public String getAlgorithm() {
        return algorithm;
    }
    
    /**
     * 获取转换模式
     * @return 转换模式
     */
    public String getTransformation() {
        return transformation;
    }
    
    /**
     * 获取默认密钥长度
     * @return 默认密钥长度（位）
     */
    public int getDefaultKeyLength() {
        return defaultKeyLength;
    }
    
    /**
     * 根据算法名称获取枚举值
     * @param algorithmName 算法名称
     * @return 对应的枚举值，如果未找到则返回AES
     */
    public static CryptoAlgorithm fromString(String algorithmName) {
        if (algorithmName == null || algorithmName.trim().isEmpty()) {
            return AES; // 默认使用AES
        }
        
        for (CryptoAlgorithm algorithm : values()) {
            if (algorithm.getAlgorithm().equalsIgnoreCase(algorithmName.trim())) {
                return algorithm;
            }
        }
        
        // 如果未找到匹配的算法，返回AES作为默认值
        return AES;
    }
    
    /**
     * 检查是否为对称加密算法
     * @return true如果是对称加密算法
     */
    public boolean isSymmetric() {
        return this != RSA;
    }
    
    /**
     * 检查是否为非对称加密算法
     * @return true如果是非对称加密算法
     */
    public boolean isAsymmetric() {
        return this == RSA;
    }
    
    /**
     * 检查是否为流密码算法
     * @return true如果是流密码算法
     */
    public boolean isStreamCipher() {
        return this == CHACHA20;
    }
    
    /**
     * 检查是否为分组密码算法
     * @return true如果是分组密码算法
     */
    public boolean isBlockCipher() {
        return this != CHACHA20;
    }
}
