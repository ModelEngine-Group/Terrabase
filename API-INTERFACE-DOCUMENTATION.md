# Terrabase Enterprise API 接口文档

## 概述

本文档详细描述了 Terrabase Enterprise API 模块中所有服务接口的定义、参数、返回值和使用方法。该模块提供了企业级应用的核心功能，包括证书管理、加解密服务、日志管理、菜单管理、监控告警和用户管理等功能。

## 目录

- [通用响应格式](#通用响应格式)
- [证书管理服务 (CertificateService)](#证书管理服务-certificateservice)
- [加解密服务 (CryptoService)](#加解密服务-cryptoservice)
- [日志服务 (LogService)](#日志服务-logservice)
- [菜单管理服务 (MenuService)](#菜单管理服务-menuservice)
- [监控告警服务 (MonitoringService)](#监控告警服务-monitoringservice)
- [用户管理服务 (UserManagementService)](#用户管理服务-usermanagementservice)
- [数据模型](#数据模型)

---

## 通用响应格式

所有 API 接口都使用统一的响应格式 `ResultVo<T>`：

```java
public class ResultVo<T> {
    private String code;    // 响应状态码
    private String msg;     // 响应消息
    private T data;         // 响应数据
}
```

**状态码说明：**
- `200`: 操作成功
- 其他: 具体错误码，详见各接口说明

---

## 证书管理服务 (CertificateService)

### 接口概述
提供证书注册、导入、查询等功能。

### 接口方法

#### 1. 获取证书列表
```java
ResultVo<List<CertCollectInfo>> listCertificateServiceList()
```

**功能描述：** 获取 OMS 管理的所有证书信息

**返回值：** `ResultVo<List<CertCollectInfo>>`
- 成功时返回证书信息列表
- 失败时返回错误信息

**CertCollectInfo 字段说明：**

| 字段名 | 类型 | 说明 |
|--------|------|------|
| productName | String | 产品名称 |
| issueTime | long | 签发时间 |
| expirationTime | long | 过期时间 |
| issuer | String | 颁发者 |
| subject | String | 主题 |
| serialNumber | String | 序列号 |
| certType | String | 证书类型 |
| status | String | 状态 |
| alertBeforeExpirationDays | Integer | 过期前提醒天数 |
| certName | String | 证书名称 |
| productVersion | String | 产品版本 |
| patchVersion | String | 补丁版本 |
| deviceEsn | String | 设备序列号 |

#### 2. 获取许可证信息
```java
ResultVo<LicenseInfo> getLicenseInfo()
```

**功能描述：** 查询 License 信息

**返回值：** `ResultVo<LicenseInfo>`
- 成功时返回许可证信息
- 失败时返回错误信息

**LicenseInfo 字段说明：**

| 字段名 | 类型 | 说明 |
|--------|------|------|
| status | String | 许可证状态 (0:未导入, 1:已上传未激活, 2:已激活, 3:已注销) |
| sboms | List<LicenseInfoEx> | SBOM 信息列表 |

---

## 加解密服务 (CryptoService)

### 接口概述
提供数据加密和解密功能，支持多种加密算法。

### 支持的加密算法

```java
public enum CryptoAlgorithm {
    AES,           // AES加密算法 (默认)
    RSA,           // RSA非对称加密
    DES,           // DES加密算法
    TRIPLE_DES,    // 3DES加密算法
    BLOWFISH,      // Blowfish加密算法
    CHACHA20       // ChaCha20流密码算法
}
```

### 接口方法

#### 1. 数据加密
```java
String encrypt(String plaintext, CryptoAlgorithm algorithm, String username)
```

**功能描述：** 对明文数据进行加密

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| plaintext | String | 是 | 待加密的明文数据 |
| algorithm | CryptoAlgorithm | 否 | 加密算法，为 null 时使用默认 AES 算法 |
| username | String | 是 | 用户名 |

**返回值：** `String` - 加密后的密文数据

**使用示例：**
```java
// 使用默认 AES 算法加密
String ciphertext = cryptoService.encrypt("Hello World", null, "admin");

// 使用 RSA 算法加密
String ciphertext = cryptoService.encrypt("Hello World", CryptoAlgorithm.RSA, "admin");
```

#### 2. 数据解密
```java
String decrypt(String ciphertext, CryptoAlgorithm algorithm, String username)
```

**功能描述：** 对密文数据进行解密

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| ciphertext | String | 是 | 待解密的密文数据 |
| algorithm | CryptoAlgorithm | 否 | 解密算法，为 null 时使用默认 AES 算法 |
| username | String | 是 | 用户名 |

**返回值：** `String` - 解密后的明文数据

**使用示例：**
```java
// 使用默认 AES 算法解密
String plaintext = cryptoService.decrypt(ciphertext, null, "admin");

// 使用 RSA 算法解密
String plaintext = cryptoService.decrypt(ciphertext, CryptoAlgorithm.RSA, "admin");
```

---

## 日志服务 (LogService)

### 接口概述
提供审计日志上报和国际化功能。

### 接口方法

#### 1. 上报审计日志
```java
ResultVo<Integer> registerLogs(List<LogAttributeVo> logs)
```

**功能描述：** 批量上报审计日志

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| logs | List<LogAttributeVo> | 是 | 日志对象列表 |

**LogAttributeVo 字段说明：**

| 字段名 | 类型 | 说明 |
|--------|------|------|
| sn | long | 序列号 |
| logType | String | 日志类型 |
| username | String | 用户名 |
| operation | String | 操作 |
| source | String | 来源 |
| terminal | String | 终端 |
| result | String | 结果 |
| flag | String | 标志 |
| paramType | String | 参数类型 |
| detail | String | 详细信息 |

**返回值：** `ResultVo<Integer>` - 返回成功处理的日志条数

#### 2. 注册审计日志国际化
```java
ResultVo<Boolean> registryInternational(List<LogI18n> logI18ns)
```

**功能描述：** 批量注册审计日志的国际化信息

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| logI18ns | List<LogI18n> | 是 | 国际化对象列表 |

**返回值：** `ResultVo<Boolean>` - 返回注册是否成功

---

## 菜单管理服务 (MenuService)

### 接口概述
提供菜单注册功能。

### 接口方法

#### 1. 注册菜单信息
```java
void registerMenuInfo(MenuRegisterInfo menuRegisterInfo)
```

**功能描述：** 注册菜单信息到系统

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| menuRegisterInfo | MenuRegisterInfo | 是 | 菜单注册信息对象 |

**MenuRegisterInfo 字段说明：**

| 字段名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| menuId | String | 是 | 菜单ID |
| parentMenuId | String | 否 | 父菜单ID |
| menuNameCode | String | 否 | 菜单名称代码 |
| url | String | 否 | 菜单URL |
| iconUrl | String | 否 | 图标URL |
| en | String | 否 | 英文名称 |
| zh | String | 否 | 中文名称 |
| enable | boolean | 否 | 是否启用 |
| roleScneMap | List<RoleScenInfo> | 否 | 角色场景映射 |
| weight | int | 否 | 权重 |

**返回值：** `void` - 无返回值

---

## 监控告警服务 (MonitoringService)

### 接口概述
提供告警定义注册、告警上报、告警查询等功能。

### 接口方法

#### 1. 批量注册告警定义
```java
ResultVo registerEventDefine(RegisterEventDefineReq req)
```

**功能描述：** 批量注册告警定义到系统

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| req | RegisterEventDefineReq | 是 | 注册请求对象 |

**返回值：** `ResultVo` - 注册结果

#### 2. 上报告警
```java
ResultVo<Boolean> sendEvents(List<EventInfo> alarmInfos)
```

**功能描述：** 批量上报告警信息

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| alarmInfos | List<EventInfo> | 是 | 告警信息列表 |

**EventInfo 字段说明：**

| 字段名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | String | 否 | 事件ID (1-32位数字) |
| serialNumber | String | 否 | 序列号 (UUID格式) |
| syncNo | Integer | 否 | 同步号 |
| eventName | String | 否 | 事件名称 (最大255字符) |
| eventType | String | 是 | 事件类型 (alter/event) |
| eventSubject | String | 是 | 事件主题 (最大255字符) |
| eventSubjectType | String | 是 | 事件主题类型 (最大255字符) |
| eventDescription | String | 否 | 事件描述 (最大8000字符) |
| eventDescriptionArgs | List<String> | 否 | 事件描述参数 (最大64个) |
| severity | String | 是 | 严重程度 (warning/minor/major/critical) |
| effect | String | 否 | 影响 (最大8000字符) |
| eventCategory | String | 是 | 事件分类 (最大128字符) |
| possibleCause | String | 否 | 可能原因 (最大8000字符) |
| suggestion | String | 否 | 建议 (最大8000字符) |
| status | String | 是 | 状态 (Uncleared/Cleared) |
| firstOccurTime | String | 否 | 首次发生时间 (最大32字符) |
| clearTime | String | 否 | 清除时间 (最大32字符) |
| evenSource | String | 否 | 事件源 (最大255字符) |
| deviceSn | String | 否 | 设备序列号 (最大255字符) |
| deviceType | String | 否 | 设备类型 (最大255字符) |
| devURL | String | 否 | 设备URL (最大255字符) |
| parts | String | 是 | 部件 (最大64字符) |
| language | String | 否 | 语言 (zh/zh-cn/en/en-us) |
| computerCategory | int | 否 | 计算机分类 |
| shouldDeleted | boolean | 否 | 是否应删除 |
| clearType | String | 否 | 清除类型 |
| defineMatchKey | String | 否 | 定义匹配键 |
| lastEventMatchKey | String | 否 | 最后事件匹配键 |
| shouldSaveDefine | Boolean | 否 | 是否应保存定义 (默认true) |
| deviceId | String | 否 | 设备ID |

**返回值：** `ResultVo<Boolean>` - 返回是否上报成功

#### 3. 批量查询告警信息
```java
ResultVo<EventsCollection> getEventsByPage(GetEventsParams getEventsParams)
```

**功能描述：** 分页查询告警信息

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| getEventsParams | GetEventsParams | 是 | 查询参数对象 |

**返回值：** `ResultVo<EventsCollection>` - 返回分页查询结果

---

## 用户管理服务 (UserManagementService)

### 接口概述
提供角色、权限、菜单等用户管理功能。

### 接口方法

#### 1. 批量角色注册
```java
void batchRegisterRole(RoleRegisterVo roleRegister)
```

**功能描述：** 批量注册角色信息

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| roleRegister | RoleRegisterVo | 是 | 角色注册对象 |

**RoleRegisterVo 字段说明：**

| 字段名 | 类型 | 说明 |
|--------|------|------|
| roleRegisterInfos | List<RoleRegisterInfo> | 角色注册信息列表 |
| roleI18nInfos | List<RoleI18nInfo> | 角色国际化信息列表 |

**返回值：** `void` - 无返回值

#### 2. 批量权限注册
```java
void registerPermission(List<AuthorityInfo> authorityInfos)
```

**功能描述：** 批量注册权限信息

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| authorityInfos | List<AuthorityInfo> | 是 | 权限信息列表 |

**返回值：** `void` - 无返回值

#### 3. 获取用户资源组列表
```java
List<ResourceGroup> getUserGroups(String userName)
```

**功能描述：** 根据用户名获取用户所属的资源组列表

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userName | String | 是 | 用户名 |

**返回值：** `List<ResourceGroup>` - 用户资源组列表

#### 4. 根据Token查询角色名
```java
ResultVo<List<String>> queryRolesByToken()
```

**功能描述：** 根据当前用户的Token查询角色名列表

**返回值：** `ResultVo<List<String>>` - 返回角色名列表

#### 5. 获取当前用户信息
```java
ResultVo<List<LoginUserDto>> getCurrentUserInfo()
```

**功能描述：** 获取当前登录用户的详细信息

**返回值：** `ResultVo<List<LoginUserDto>>` - 返回当前用户信息列表

---

## 数据模型

### 通用数据模型

#### ResultVo<T>
通用响应包装类，用于统一API响应格式。

#### CryptoAlgorithm
加密算法枚举，定义了系统支持的各种加密算法。

### 业务数据模型

#### CertCollectInfo
证书收集信息，包含证书的详细信息。

#### LicenseInfo
许可证信息，包含许可证状态和SBOM信息。

#### EventInfo
事件信息，用于告警和监控。

#### MenuRegisterInfo
菜单注册信息，用于菜单管理。

#### LogAttributeVo
日志属性对象，用于审计日志。

#### RoleRegisterVo
角色注册对象，用于角色管理。

#### AuthorityInfo
权限信息，用于权限管理。

#### ResourceGroup
资源组信息，用于资源管理。

#### LoginUserDto
登录用户信息，用于用户认证。

---

## 使用示例

### 1. SDK 初始化方式

```java
import com.terrabase.enterprise.api.sdk.TerrabaseSDK;
import com.terrabase.enterprise.api.sdk.TerrabaseSDKConfig;
import com.terrabase.enterprise.api.CryptoAlgorithm;

public class TerrabaseSDKExample {
    
    public static void main(String[] args) {
        try {
            // 方式1：使用默认配置初始化（推荐）
            TerrabaseSDK sdk = TerrabaseSDK.init();
            
            // 方式2：使用自定义配置初始化
            TerrabaseSDKConfig config = new TerrabaseSDKConfig();
            config.setNacosDiscoveryEnabled(false); // 开源版不启用Nacos
            TerrabaseSDK sdk2 = TerrabaseSDK.init(config);
            
            // 方式3：使用指定证书路径初始化（推荐用于HTTPS环境）
            TerrabaseSDK sdkWithCert = TerrabaseSDK.initWithCertPath("/path/to/trust/cert.pem");
            
            // 方式4：使用证书路径和Nacos配置初始化（商业版）
            TerrabaseSDK commercialSdk = TerrabaseSDK.initWithCertPathAndNacos(
                "/path/to/trust/cert.pem", 
                "https://nacos-server:8848", 
                "nacos", 
                "password"
            );
            
            // 方式5：直接使用静态方法（最简洁）
            String encrypted = TerrabaseSDK.cryptoService().encrypt("hello", CryptoAlgorithm.AES, "user1");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### 2. 证书路径配置说明

#### 2.1 为什么需要配置证书路径？

在HTTPS环境中，SDK需要验证服务器证书的有效性。默认情况下，SDK使用系统默认的证书路径，但在某些企业环境中，可能需要使用特定的证书文件来建立信任关系。

#### 2.2 证书路径配置方式

**方式1：使用便捷方法（推荐）**
```java
// 仅设置证书路径，使用默认配置
TerrabaseSDK sdk = TerrabaseSDK.initWithCertPath("/path/to/trust/cert.pem");

// 设置证书路径和Nacos配置（商业版）
TerrabaseSDK sdk = TerrabaseSDK.initWithCertPathAndNacos(
    "/path/to/trust/cert.pem", 
    "https://nacos-server:8848", 
    "nacos", 
    "password"
);
```

**方式2：使用配置对象**
```java
// 创建配置对象
TerrabaseSDKConfig config = new TerrabaseSDKConfig();
config.setTrustCertPath("/path/to/trust/cert.pem");
config.setNacosDiscoveryEnabled(false); // 开源版

// 使用配置初始化
TerrabaseSDK sdk = TerrabaseSDK.init(config);
```

**方式3：商业版完整配置**
```java
// 创建商业版配置
TerrabaseSDKConfig commercialConfig = TerrabaseSDKConfig.createCommercial(
    "https://nacos-server:8848", 
    "nacos", 
    "password"
);
commercialConfig.setTrustCertPath("/path/to/trust/cert.pem");

// 使用配置初始化
TerrabaseSDK sdk = TerrabaseSDK.init(commercialConfig);
```

#### 2.3 证书文件格式要求

- **支持格式**：PEM格式（.pem, .crt, .cer）
- **文件内容**：包含完整的证书链
- **文件权限**：确保应用有读取权限

#### 2.4 常见证书路径示例

```java
// Linux/Unix 环境
TerrabaseSDK.initWithCertPath("/etc/ssl/certs/ca-certificates.crt");
TerrabaseSDK.initWithCertPath("/opt/huawei/fce/runtime/security/server_cert/nacos/nacos.crt");

// Windows 环境
TerrabaseSDK.initWithCertPath("C:\\certs\\trust-cert.pem");
TerrabaseSDK.initWithCertPath("C:\\Program Files\\certificates\\ca-bundle.crt");

// 相对路径
TerrabaseSDK.initWithCertPath("./certs/trust-cert.pem");
TerrabaseSDK.initWithCertPath("conf/ssl/trust-store.pem");
```

#### 2.5 证书验证失败处理

如果证书路径无效或证书验证失败，SDK会记录错误日志并可能影响HTTPS连接：

```java
try {
    TerrabaseSDK sdk = TerrabaseSDK.initWithCertPath("/invalid/path/cert.pem");
    // 如果证书路径无效，后续HTTPS请求可能会失败
} catch (Exception e) {
    System.err.println("证书配置失败: " + e.getMessage());
}
```

#### 2.6 动态证书更新

如果需要动态更新证书路径，可以重新初始化SDK：

```java
// 清理现有实例
TerrabaseSDK.getInstance().clearCache();

// 重新初始化（注意：SDK是单例，需要重启应用才能生效）
TerrabaseSDK sdk = TerrabaseSDK.initWithCertPath("/new/path/cert.pem");
```

### 3. 加解密服务使用示例

```java
// 方式1：使用静态方法（最简洁）
String plaintext = "敏感数据";
String ciphertext = TerrabaseSDK.cryptoService().encrypt(plaintext, CryptoAlgorithm.AES, "admin");
System.out.println("加密结果: " + ciphertext);

String decryptedText = TerrabaseSDK.cryptoService().decrypt(ciphertext, CryptoAlgorithm.AES, "admin");
System.out.println("解密结果: " + decryptedText);

// 使用 RSA 算法加密
String rsaCiphertext = TerrabaseSDK.cryptoService().encrypt(plaintext, CryptoAlgorithm.RSA, "admin");
System.out.println("RSA 加密结果: " + rsaCiphertext);

// 方式2：通过实例调用
TerrabaseSDK sdk = TerrabaseSDK.getInstance();
String encrypted = sdk.crypto().encrypt(plaintext, CryptoAlgorithm.AES, "admin");
```

### 4. 日志服务使用示例

```java
// 使用静态方法调用
LogService logService = TerrabaseSDK.logService();

// 创建日志对象
LogAttributeVo log = LogAttributeVo.builder()
    .sn(System.currentTimeMillis())
    .logType("AUDIT")
    .username("admin")
    .operation("LOGIN")
    .source("WEB")
    .terminal("192.168.1.100")
    .result("SUCCESS")
    .flag("NORMAL")
    .paramType("STRING")
    .detail("用户登录成功")
    .build();

// 上报日志
ResultVo<Integer> result = logService.registerLogs(Arrays.asList(log));
if ("200".equals(result.getCode())) {
    System.out.println("日志上报成功，处理条数: " + result.getData());
} else {
    System.out.println("日志上报失败: " + result.getMsg());
}

// 或者直接使用静态方法
ResultVo<Integer> result2 = TerrabaseSDK.logService().registerLogs(Arrays.asList(log));
```

### 5. 菜单服务使用示例

```java
// 使用静态方法获取服务
MenuService menuService = TerrabaseSDK.menuService();

// 创建菜单信息
MenuRegisterInfo menuInfo = MenuRegisterInfo.builder()
    .menuId("MENU_001")
    .parentMenuId("ROOT")
    .menuNameCode("USER_MANAGEMENT")
    .url("/user/management")
    .iconUrl("/icons/user.png")
    .en("User Management")
    .zh("用户管理")
    .enable(true)
    .weight(1)
    .build();

// 注册菜单
try {
    menuService.registerMenuInfo(menuInfo);
    System.out.println("菜单注册成功");
} catch (Exception e) {
    System.out.println("菜单注册失败: " + e.getMessage());
}

// 或者直接使用静态方法
TerrabaseSDK.menuService().registerMenuInfo(menuInfo);
```

### 6. 证书服务使用示例

```java
// 使用静态方法获取证书服务
CertificateService certificateService = TerrabaseSDK.certificateService();

// 获取证书列表
ResultVo<List<CertCollectInfo>> certResult = certificateService.listCertificateServiceList();
if ("200".equals(certResult.getCode())) {
    List<CertCollectInfo> certs = certResult.getData();
    System.out.println("证书数量: " + certs.size());
    for (CertCollectInfo cert : certs) {
        System.out.println("证书名称: " + cert.getCertName() + 
                          ", 状态: " + cert.getStatus() + 
                          ", 过期时间: " + new Date(cert.getExpirationTime()));
    }
} else {
    System.out.println("获取证书列表失败: " + certResult.getMsg());
}

// 获取许可证信息
ResultVo<LicenseInfo> licenseResult = certificateService.getLicenseInfo();
if ("200".equals(licenseResult.getCode())) {
    LicenseInfo license = licenseResult.getData();
    System.out.println("许可证状态: " + license.getStatus());
} else {
    System.out.println("获取许可证信息失败: " + licenseResult.getMsg());
}

// 或者直接使用静态方法
ResultVo<List<CertCollectInfo>> certs = TerrabaseSDK.certificateService().listCertificateServiceList();
```

### 7. 监控告警服务使用示例

```java
// 使用静态方法获取监控服务
MonitoringService monitoringService = TerrabaseSDK.monitoringService();

// 创建告警信息
EventInfo event = EventInfo.builder()
    .eventName("系统异常")
    .eventType("alter")
    .eventSubject("服务器")
    .eventSubjectType("硬件")
    .eventDescription("服务器CPU使用率过高")
    .severity("major")
    .eventCategory("性能")
    .status("Uncleared")
    .parts("CPU")
    .build();

// 上报告警
ResultVo<Boolean> alarmResult = monitoringService.sendEvents(Arrays.asList(event));
if ("200".equals(alarmResult.getCode()) && alarmResult.getData()) {
    System.out.println("告警上报成功");
} else {
    System.out.println("告警上报失败: " + alarmResult.getMsg());
}

// 或者直接使用静态方法
ResultVo<Boolean> result = TerrabaseSDK.monitoringService().sendEvents(Arrays.asList(event));
```

### 8. 用户管理服务使用示例

```java
// 使用静态方法获取用户管理服务
UserManagementService userService = TerrabaseSDK.userManagementService();

// 获取用户资源组
List<ResourceGroup> groups = userService.getUserGroups("admin");
System.out.println("用户资源组数量: " + groups.size());
for (ResourceGroup group : groups) {
    System.out.println("资源组: " + group.getGroupName());
}

// 根据Token查询角色
ResultVo<List<String>> rolesResult = userService.queryRolesByToken();
if ("200".equals(rolesResult.getCode())) {
    List<String> roles = rolesResult.getData();
    System.out.println("用户角色: " + String.join(", ", roles));
} else {
    System.out.println("查询角色失败: " + rolesResult.getMsg());
}

// 获取当前用户信息
ResultVo<List<LoginUserDto>> userResult = userService.getCurrentUserInfo();
if ("200".equals(userResult.getCode())) {
    List<LoginUserDto> users = userResult.getData();
    if (!users.isEmpty()) {
        LoginUserDto user = users.get(0);
        System.out.println("当前用户: " + user.getUsername());
    }
} else {
    System.out.println("获取用户信息失败: " + userResult.getMsg());
}

// 或者直接使用静态方法
ResultVo<List<LoginUserDto>> users = TerrabaseSDK.userManagementService().getCurrentUserInfo();
```

### 9. 完整使用示例

```java
import com.terrabase.enterprise.api.sdk.TerrabaseSDK;
import com.terrabase.enterprise.api.CryptoAlgorithm;
import com.terrabase.enterprise.api.dto.LogAttributeVo;
import com.terrabase.enterprise.api.dto.MenuRegisterInfo;
import com.terrabase.enterprise.api.dto.EventInfo;
import com.terrabase.enterprise.api.response.ResultVo;

public class CompleteExample {
    
    public static void main(String[] args) {
        try {
            // 初始化 SDK（使用证书路径）
            TerrabaseSDK.initWithCertPath("/path/to/trust/cert.pem");
            
            // 1. 加解密操作
            String encrypted = TerrabaseSDK.cryptoService().encrypt("Hello World", CryptoAlgorithm.AES, "admin");
            String decrypted = TerrabaseSDK.cryptoService().decrypt(encrypted, CryptoAlgorithm.AES, "admin");
            System.out.println("解密结果: " + decrypted);
            
            // 2. 日志上报
            LogAttributeVo log = LogAttributeVo.builder()
                .sn(System.currentTimeMillis())
                .logType("AUDIT")
                .username("admin")
                .operation("LOGIN")
                .result("SUCCESS")
                .build();
            ResultVo<Integer> logResult = TerrabaseSDK.logService().registerLogs(Arrays.asList(log));
            
            // 3. 菜单注册
            MenuRegisterInfo menu = MenuRegisterInfo.builder()
                .menuId("MENU_001")
                .menuNameCode("TEST_MENU")
                .zh("测试菜单")
                .enable(true)
                .build();
            TerrabaseSDK.menuService().registerMenuInfo(menu);
            
            // 4. 告警上报
            EventInfo event = EventInfo.builder()
                .eventName("测试告警")
                .eventType("alter")
                .eventSubject("系统")
                .severity("minor")
                .status("Uncleared")
                .parts("TEST")
                .build();
            ResultVo<Boolean> alarmResult = TerrabaseSDK.monitoringService().sendEvents(Arrays.asList(event));
            
            // 5. 用户信息查询
            ResultVo<List<LoginUserDto>> users = TerrabaseSDK.userManagementService().getCurrentUserInfo();
            
            // 6. 证书信息查询
            ResultVo<List<CertCollectInfo>> certs = TerrabaseSDK.certificateService().listCertificateServiceList();
            
            System.out.println("所有操作执行完成！");
            
        } catch (Exception e) {
            System.err.println("操作失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

---

## 注意事项

1. **加密算法选择**：建议根据数据敏感程度选择合适的加密算法，AES适用于一般数据加密，RSA适用于密钥交换。

2. **证书路径配置**：
   - 确保证书文件存在且可读
   - 使用PEM格式的证书文件
   - 证书路径应使用绝对路径，避免相对路径问题
   - 在HTTPS环境中，证书配置错误可能导致连接失败

3. **日志上报**：审计日志应包含完整的操作信息，便于后续审计和追踪。

4. **菜单注册**：菜单注册时需确保菜单ID唯一，避免冲突。

5. **告警信息**：告警信息应包含完整的上下文信息，便于问题定位和处理。

6. **权限管理**：权限注册时应遵循最小权限原则，避免权限过度授予。

7. **错误处理**：所有接口调用都应进行适当的错误处理，根据返回的状态码进行相应处理。

8. **SDK初始化**：SDK采用单例模式，初始化后无法更改配置，如需更改配置需要重启应用。

---

## 版本信息

- **文档版本**: 1.0.0
- **API版本**: 1.0.0
- **最后更新**: 2024年12月

---

## 联系方式

如有问题或建议，请联系 Terrabase 开发团队。
