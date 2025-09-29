# Terrabase 开源版 KMC 加解密功能

## 概述

本文档介绍了 Terrabase 开源版企业服务中集成的 KMC (Key Management Center) 加解密功能。该功能基于开源算法实现，使用 AES-GCM 加密模式，提供安全可靠的数据加解密服务。

## 功能特性

### 核心功能
- **AES-GCM 加密**: 使用 256 位密钥的 AES-GCM 模式进行加解密
- **随机 IV 生成**: 每次加密都生成随机的初始化向量，确保密文唯一性
- **密钥管理**: 支持多密钥管理，包括默认密钥和自定义密钥
- **安全存储**: 密钥在内存中安全缓存，支持动态管理

### 安全特性
- **认证加密**: GCM 模式提供数据完整性和真实性验证
- **密钥隔离**: 不同密钥的密文无法相互解密
- **随机性**: 使用安全随机数生成器确保加密的随机性
- **审计日志**: 可选的加解密操作审计记录

## 技术实现

### 依赖库
- **BouncyCastle**: 提供加密算法实现
- **Apache Commons Codec**: Base64 编码/解码
- **Spring Boot**: 配置管理和依赖注入

### 加密算法
- **算法**: AES (Advanced Encryption Standard)
- **模式**: GCM (Galois/Counter Mode)
- **密钥长度**: 256 位
- **IV 长度**: 96 位 (12 字节)
- **标签长度**: 128 位 (16 字节)

## 配置说明

### application.properties 配置

```properties
# KMC 基本配置
terrabase.kmc.enabled=true                    # 启用 KMC 功能
terrabase.kmc.default-key-id=default         # 默认密钥 ID
terrabase.kmc.algorithm=AES                  # 加密算法
terrabase.kmc.key-length=256                 # 密钥长度（位）
terrabase.kmc.transformation=AES/GCM/NoPadding # 转换模式

# GCM 参数配置
terrabase.kmc.gcm-iv-length=12               # GCM IV 长度（字节）
terrabase.kmc.gcm-tag-length=16              # GCM 标签长度（字节）

# 缓存配置
terrabase.kmc.key-caching=true               # 启用密钥缓存
terrabase.kmc.max-cache-size=100             # 最大缓存密钥数量
terrabase.kmc.key-expiration-time=86400000   # 密钥过期时间（毫秒）

# 高级功能
terrabase.kmc.enable-key-rotation=false      # 启用密钥轮换
terrabase.kmc.key-rotation-interval=604800000 # 密钥轮换间隔（毫秒）
terrabase.kmc.enable-audit-log=true          # 启用审计日志
```

## 使用方法

### 1. 基本加解密

```java
// 获取企业服务实例
@Autowired
private EnterpriseService enterpriseService;

// 加密数据
String plaintext = "需要加密的敏感数据";
String encryptResult = enterpriseService.encrypt(plaintext);
// 返回格式: "开源版KMC加密成功: <Base64编码的密文>"

// 解密数据
String ciphertext = "<从加密结果中提取的Base64密文>";
String decryptResult = enterpriseService.decrypt(ciphertext);
// 返回格式: "开源版KMC解密成功: <原始明文>"
```

### 2. 直接使用工具类

```java
import com.terrabase.enterprise.impl.open.util.KmcCryptoUtil;

// 生成密钥
String keyId = "my_custom_key";
KmcCryptoUtil.generateKey(keyId);

// 使用指定密钥加密
String ciphertext = KmcCryptoUtil.encrypt(plaintext, keyId);

// 使用指定密钥解密
String decryptedText = KmcCryptoUtil.decrypt(ciphertext, keyId);

// 使用默认密钥
String defaultCiphertext = KmcCryptoUtil.encrypt(plaintext);
String defaultDecryptedText = KmcCryptoUtil.decrypt(defaultCiphertext);
```

### 3. 密钥管理

```java
// 检查密钥是否存在
boolean exists = KmcCryptoUtil.hasKey("my_key");

// 获取密钥信息
String keyInfo = KmcCryptoUtil.getKeyInfo("my_key");

// 删除指定密钥
boolean removed = KmcCryptoUtil.removeKey("my_key");

// 获取当前密钥数量
int keyCount = KmcCryptoUtil.getKeyCount();

// 清空所有密钥
KmcCryptoUtil.clearAllKeys();
```

## API 接口

### REST API 端点

通过企业服务的 REST API 可以使用 KMC 功能：

```bash
# 加密数据
curl -X POST http://localhost:8080/api/enterprise/encrypt \
  -H "Content-Type: application/json" \
  -d '{"data": "需要加密的数据"}'

# 解密数据
curl -X POST http://localhost:8080/api/enterprise/decrypt \
  -H "Content-Type: application/json" \
  -d '{"data": "Base64编码的密文"}'
```

## 安全注意事项

### 1. 密钥管理
- 密钥仅在内存中存储，应用重启后需要重新生成
- 生产环境建议使用外部密钥管理系统
- 定期轮换密钥以提高安全性

### 2. 数据保护
- 密文使用 Base64 编码，便于传输和存储
- 每次加密都使用随机 IV，确保相同明文产生不同密文
- GCM 模式提供数据完整性验证

### 3. 性能考虑
- 密钥缓存可提高性能，但需注意内存使用
- 长文本加密可能影响性能，建议分批处理
- 审计日志可能影响性能，生产环境可考虑异步记录

## 测试

### 运行单元测试

```bash
# 运行 KMC 工具类测试
mvn test -Dtest=KmcCryptoUtilTest

# 运行集成测试
mvn test -Dtest=OpenEnterpriseServiceImplKmcTest

# 运行所有测试
mvn test
```

### 测试覆盖范围

- 基本加解密功能
- 密钥管理功能
- 错误处理
- 边界条件测试
- 性能测试
- 安全测试

## 故障排除

### 常见问题

1. **加密失败**
   - 检查 KMC 功能是否启用
   - 验证服务是否正常运行
   - 检查输入数据是否为空

2. **解密失败**
   - 验证密文格式是否正确
   - 检查密钥是否存在
   - 确认使用相同的密钥进行解密

3. **性能问题**
   - 检查密钥缓存配置
   - 考虑禁用审计日志
   - 优化长文本处理

### 日志分析

启用 DEBUG 日志级别可以查看详细的加解密过程：

```properties
logging.level.com.terrabase.enterprise.impl.open.util=DEBUG
logging.level.com.terrabase.enterprise.impl.open.config=DEBUG
```

## 扩展开发

### 添加新的加密算法

1. 在 `KmcCryptoUtil` 中添加新的算法支持
2. 更新 `KmcConfig` 配置类
3. 添加相应的测试用例
4. 更新文档

### 集成外部密钥管理

1. 实现密钥提供者接口
2. 修改密钥获取逻辑
3. 添加密钥同步机制
4. 更新配置管理

## 版本历史

- **v1.0.0**: 初始版本，支持基本的 AES-GCM 加解密功能

## 许可证

本功能基于开源许可证，遵循项目的整体许可证条款。

## 支持

如有问题或建议，请联系 Terrabase 开发团队。
