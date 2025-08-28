# Terrabase 架构设计文档

## 文档信息

| 项目名称 | Terrabase |
| ---------- | ----------- |
| 文档版本 | v1.0.0    |
| 创建日期 | 2025年    |
| 最后更新 | 2025年    |
| 文档状态 | 草稿      |

## 1. 架构概述

### 1.1 设计目标

Terrabase 架构设计旨在实现以下目标：

- **特性解耦**：将开源数据使能的核心功能与商业版本的高级特性进行解耦
- **统一接口**：为不同的数据源和处理引擎提供统一的编程接口
- **灵活部署**：支持纯开源部署和商业版本增强部署两种模式
- **性能优化**：结合开源和商业版本的优势，提供最佳的性能表现
- **可扩展性**：支持新功能和模块的灵活添加

### 1.2 架构原则

- **模块化设计**：每个功能模块独立，职责单一
- **接口隔离**：通过接口定义模块间的交互契约
- **配置驱动**：支持运行时配置和特性开关
- **向后兼容**：确保版本升级不影响现有功能
- **性能优先**：在保证功能完整性的前提下优化性能

## 2. 企业级特性清单

### 2.1 OMS对接能力

#### 2.1.1 服务注册与发现
暂不接入(待讨论)

#### 2.1.2 安全与加密
- **KMC加解密**
  - **集成方式**: 直接使用SDK集成，无需额外配置
  - **支持语言**: Java、Python三种语言
  - **Java实现**: 使用common-base SDK进行加解密操作
    - **加密接口定义**:
      ```java
      /**
       * 加密接口
       * @param plaintext 明文数据
       * @return 密文数据
       */
      String encrypt(String plaintext);
      ```
    - **解密接口定义**:
      ```java
      /**
       * 解密接口
       * @param ciphertext 密文数据
       * @return 明文数据
       */
      String decrypt(String ciphertext);
      ```
  - **Python实现**: 使用kmc.kmc SDK进行加解密操作
    - 加密示例代码
    - 解密示例代码
  - **角色注册**
    - **接口定义**:
      ```java
      /**
       * 角色注册接口
       * @param roleRegister 角色注册对象
       */
      void registerRole(RoleRegister roleRegister);
      ```
    - **具体实现**: 调用POST /framework/v1/iam/roles/batch/register/internal进行角色注册
    - **应用场景**: 数据使能
  - **权限注册**
    - **接口定义**:
      ```java
      /**
       * 权限注册接口
       * @param authorityInfos 权限信息对象
       */
      void registerAuthority(AuthorityInfos authorityInfos);
      ```
    - **具体实现**: 调用POST /framework/v1/iam/permission/batch/register/internal进行权限注册
    - **应用场景**: 数据使能
  - **菜单注册**
    - **接口定义**:
      ```java
      /**
       * 菜单注册接口
       * @param menuRegisterInfo 菜单注册信息对象
       */
      void registerMenu(MenuRegisterInfo menuRegisterInfo);
      ```
    - **具体实现**: 调用POST /framework/v1/customize/menu/register/internal进行菜单注册
    - **应用场景**: 数据使能
  - **菜单屏蔽注册**
    - **接口定义**:
      ```java
      /**
       * 菜单屏蔽注册接口
       * @param forbiddenBody 菜单屏蔽信息对象
       */
      void registerMenuForbidden(ForbiddenBody forbiddenBody);
      ```
    - **具体实现**: 调用POST /framework/v1/customize/menu/register/forbidden/item/internal进行菜单屏蔽注册

#### 2.1.3 日志与监控
- **操作日志国际化注册**
  - **接口定义**:
    ```java
    /**
     * 操作日志国际化信息注册接口
     * @param logI18NS 国际化日志信息对象
     */
    void registerOperateLogI18N(LogI18NS logI18NS);
    ```
  - **具体实现**: 调用POST /framework/v1/log/operateLogs/actions/register/internation/internal进行操作日志国际化信息注册
  - **应用场景**: 数据使能
- **上报操作日志**
  - **接口定义**:
    ```java
    /**
     * 上报操作日志接口
     * @param logs 操作日志信息对象
     */
    void reportOperateLog(Logs logs);
    ```
  - **具体实现**: 调用POST /framework/v1/log/operateLogs/actions/register/internal进行上报操作日志
  - **应用场景**: 数据使能
- **syslog日志上报** (待设计)

#### 2.1.4 时间管理
- **时间配置管理**
  - **时间配置变更事件订阅**
    - **接口定义**:
      ```java
      /**
       * 时间配置变更事件订阅接口
       * @param subscribe 订阅信息对象
       */
      void subscribeTimeConfigChange(Subscribe subscribe);
      ```
    - **具体实现**: 调用POST /framework/v1/iam/subscribe/internal进行时间配置变更事件订阅
    - **功能说明**: 监听时区、NTP和强制时间同步的设置事件变更
    - **事件通知**: 变更时会回调notifyAddress接口来通知对应事件变更，其中notifyAddress是Subscribe对象中的参数，表示自已服务的接口地址
    - **依赖服务**: 依赖服务注册功能

#### 2.1.5 证书管理
- **证书生命周期管理**
  - **注册证书**
    - **接口定义**:
      ```java
      /**
       * 证书注册接口
       * @param certificate 证书信息对象
       */
      void registerCertificate(RegisterCertificate certificate);
      ```
    - **具体实现**: 调用POST /framework/v1/certificate/action/register/type/internal进行证书注册
  - **导入/更新证书**
    - **接口定义**:
      ```java
      /**
       * 导入/更新证书接口
       * @param certificate 证书详细信息对象
       */
      void importOrUpdateCertificate(CertificateDetail certificate);
      ```
    - **具体实现**: 调用POST /framework/v1/certificate/action/import/om进行证书导入或更新
  - **导入信任CA证书**
    - **接口定义**:
      ```java
      /**
       * 导入信任CA证书接口
       * @param caCertificate CA证书对象
       */
      void importTrustedCaCertificate(CaCertificate caCertificate);
      ```
    - **具体实现**: 调用POST /framework/v1/certificate/action/import进行CA证书导入
  - **查询所有证书信息**
    - **接口定义**:
      ```java
      /**
       * 查询所有证书信息接口
       * @return List<CertCollectInfo> 证书收集信息列表
       */
      List<CertCollectInfo> queryAllCertificates();
      ```
    - **具体实现**: 调用GET /framework/v1/certificate/action/cert/collect进行证书信息查询
    - **应用场景**: 数据使能
- **License管理**
  - **License信息查询**
    - **接口定义**:
      ```java
      /**
       * License信息查询接口
       * @return LicenseInfo License信息对象
       */
      LicenseInfo queryLicenseInfo();
      ```
    - **具体实现**: 调用GET /framework/v1/license/info进行License信息查询
    - **功能说明**: 查询系统License的详细信息，包括有效期、功能模块等
- **HTTPS通信安全**
  - **HTTPS通信证书校验**
    - **接口定义**:
      ```java
      /**
       * HTTPS通信证书校验接口
       * @return SSLSocketFactory SSL套接字工厂
       */
      SSLSocketFactory validateHttpsCertificate();
      ```
    - **具体实现**: 使用common-base SDK集成完成具体实现

#### 2.1.6 系统管理
- **系统备份**
- **LDAPS/LDAP**
- **许可证管理**

#### 2.1.7 通信与传输
- **SMTP邮件**
- **SFTP/FTPS**

#### 2.1.8 远程支持
- **Call Home**
- **接入DME IQ**

#### 2.1.9 网络管理
- **SNMP配置**
- **共享服务器**

#### 2.1.10 任务与工作流
- **任务管理**
- **配置SSO**

#### 2.1.11 告警系统
- **告警管理**
- **告警设置**
- **告警上报**

### 2.2 OMS集成业务梳理

#### 2.2.1 系统配置
- **系统信息管理**
- **IP地址管理**
- **时间配置**
- **系统LOGO**

#### 2.2.2 证书管理
- **证书生命周期**
- **证书存储**

#### 2.2.3 操作日志
- **日志记录**
- **日志管理**

#### 2.2.4 用户管理
- **用户认证**
- **密码管理**
- **SSO单点登录**

#### 2.2.5 第三方接入
- **共享服务器**
- **SFTP/FTP接入**
- **DME IQ远程协助**
- **syslog服务器**

#### 2.2.6 密钥管理
- **密钥生成**
- **密钥存储**
- **密钥使用**

### 2.3 企业级特性实现架构

```
┌─────────────────────────────────────────────────────────────────┐
│                    企业级特性管理模块                              │
├─────────────────────────────────────────────────────────────────┤
│  OMS对接管理  │  业务集成管理  │  特性开关控制  │  许可证管理    │
│  - 服务注册   │  - 系统配置   │  - 动态配置   │  - 验证检查    │
│  - 安全加密   │  - 用户管理   │  - 功能控制   │  - 特性解锁    │
│  - 日志监控   │  - 第三方接入 │  - 权限控制   │  - 使用统计    │
│  - 时间管理   │  - 密钥管理   │  - 审计日志   │  - 过期处理    │
└─────────────────────────────────────────────────────────────────┘
```

## 3. 系统架构

### 3.1 整体架构图

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                             应用层 (Application Layer)                       │
├─────────────────────────────────────────────────────────────────────────────┤
│  Web应用  │  桌面应用  │  移动应用  │  命令行工具  │  API服务              │
└─────────────────────────────────────────────────────────────────────────────┘
                                    │
┌─────────────────────────────────────────────────────────────────────────────┐
│                             适配层 (Adapter Layer)                          │
├─────────────────────────────────────────────────────────────────────────────┤
│  开源数据使能适配器  │  商业版本适配器  │  特性解耦控制器  │  统一接口层        │
│  - 基础数据处理     │  - Nexent集成   │  - 功能开关     │  - 标准API         │
│  - 标准算法库       │  - ModelEngine  │  - 模块化加载   │  - 协议转换       │
│  - 开源协议支持     │  - 商业特性增强 │  - 动态配置     │  - 错误处理       │
└─────────────────────────────────────────────────────────────────────────────┘
                                    │
┌─────────────────────────────────────────────────────────────────────────────┐
│                             核心层 (Core Layer)                             │
├─────────────────────────────────────────────────────────────────────────────┤
│  业务逻辑模块  │  数据处理模块  │  算法引擎模块  │  配置管理模块          │
│  - 业务流程   │  - 数据转换   │  - 机器学习   │  - 环境配置           │
│  - 规则引擎   │  - 数据验证   │  - 统计分析   │  - 特性开关           │
│  - 工作流     │  - 数据缓存   │  - 优化算法   │  - 动态配置           │
└─────────────────────────────────────────────────────────────────────────────┘
                                    │
┌─────────────────────────────────────────────────────────────────────────────┐
│                             基础设施层 (Infrastructure Layer)                │
├─────────────────────────────────────────────────────────────────────────────┤
│  数据存储  │  消息队列  │  缓存系统  │  日志系统  │  监控系统              │
│  - 关系数据库 │  - Kafka  │  - Redis  │  - Log4j  │  - Prometheus        │
│  - NoSQL     │  - RabbitMQ│  - Memcached│  - SLF4J │  - Grafana          │
│  - 文件存储   │  - ActiveMQ│  - 本地缓存│  - ELK   │  - 告警系统          │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 3.2 分层架构说明

#### 3.2.1 应用层 (Application Layer)

- **职责**：提供用户界面和业务入口
- **组件**：Web应用、桌面应用、移动应用、命令行工具、API服务
- **特点**：支持多种部署方式，统一的用户体验

#### 3.2.2 适配层 (Adapter Layer)

- **职责**：实现不同数据源和处理引擎的适配
- **组件**：开源适配器、商业版本适配器、特性解耦控制器、统一接口层
- **特点**：插件化设计，支持热插拔

#### 3.2.3 核心层 (Core Layer)

- **职责**：实现核心业务逻辑和数据处理
- **组件**：业务逻辑模块、数据处理模块、算法引擎模块、配置管理模块
- **特点**：模块化设计，高内聚低耦合

#### 3.2.4 基础设施层 (Infrastructure Layer)

- **职责**：提供基础的技术支撑服务
- **组件**：数据存储、消息队列、缓存系统、日志系统、监控系统
- **特点**：标准化接口，支持多种实现

## 4. 模块设计

### 4.1 核心模块架构

```
                    ┌─────────────────┐
                    │   business-app  │
                    │  (商业应用主模块)  │
                    └─────────┬───────┘
                              │
                    ┌─────────▼───────┐
                    │  enterprise-app │
                    │ (企业级应用模块)  │
                    └─────────┬───────┘
                              │
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
┌───────▼────────┐    ┌──────▼────────┐    ┌──────▼────────┐
│enterprise-impl-│    │enterprise-impl│    │  开源数据使能  │
│  commercial    │    │     -open     │    │    模块       │
│ (商业版本实现)   │    │ (开源版本实现) │    │              │
└────────────────┘    └──────────────┘    └──────────────┘
```

### 4.2 模块详细设计

#### 4.2.1 business-app 模块

- **包结构**：

  ```
  com.terrabase.business
  ├── controller    # 控制器层
  ├── service      # 业务服务层
  ├── repository   # 数据访问层
  ├── model        # 数据模型
  └── config       # 配置类
  ```
- **核心功能**：

  - 业务流程管理
  - 用户权限控制
  - 系统配置管理
  - 业务规则引擎

#### 4.2.2 enterprise-app 模块

- **包结构**：

  ```
  com.terrabase.enterprise
  ├── core         # 核心功能
  ├── integration # 集成接口
  ├── workflow    # 工作流引擎
  └── security    # 安全模块
  ```
- **核心功能**：

  - 企业级功能支持
  - 高级工作流管理
  - 企业安全策略
  - 审计日志

#### 4.2.3 enterprise-impl-commercial 模块

- **包结构**：

  ```
  com.terrabase.enterprise.commercial
  ├── nexent      # Nexent集成
  ├── modelengine # ModelEngine集成
  ├── features    # 商业特性
  └── licensing   # 许可证管理
  ```
- **核心功能**：

  - Nexent商业版本集成
  - ModelEngine商业版本集成
  - 高级算法支持
  - 许可证验证

#### 4.2.4 enterprise-impl-open 模块

- **包结构**：

  ```
  com.terrabase.enterprise.open
  ├── dataenable  # 数据使能
  ├── algorithms  # 开源算法
  ├── protocols   # 开源协议
  └── community   # 社区功能
  ```
- **核心功能**：

  - 开源数据使能技术
  - 标准算法库
  - 开源协议支持
  - 社区贡献功能

## 5. 接口设计

### 5.1 统一接口层

#### 5.1.1 数据处理接口

```java
public interface DataProcessor {
    /**
     * 处理数据
     * @param data 输入数据
     * @return 处理结果
     */
    Result process(Data data);
  
    /**
     * 批量处理数据
     * @param dataList 数据列表
     * @return 处理结果列表
     */
    List<Result> processBatch(List<Data> dataList);
  
    /**
     * 获取处理器能力
     * @return 能力描述
     */
    Capability getCapability();
}
```

#### 5.1.2 特性开关接口

```java
public interface FeatureToggle {
    /**
     * 检查特性是否启用
     * @param featureName 特性名称
     * @return 是否启用
     */
    boolean isEnabled(String featureName);
  
    /**
     * 动态启用特性
     * @param featureName 特性名称
     */
    void enableFeature(String featureName);
  
    /**
     * 动态禁用特性
     * @param featureName 特性名称
     */
    void disableFeature(String featureName);
}
```

### 5.2 适配器接口

#### 5.2.1 开源适配器接口

```java
public interface OpenSourceAdapter {
    /**
     * 初始化开源组件
     */
    void initialize();
  
    /**
     * 获取开源组件信息
     * @return 组件信息
     */
    ComponentInfo getComponentInfo();
  
    /**
     * 执行开源算法
     * @param algorithm 算法名称
     * @param parameters 参数
     * @return 执行结果
     */
    ExecutionResult executeAlgorithm(String algorithm, Map<String, Object> parameters);
}
```

#### 5.2.2 商业版本适配器接口

```java
public interface CommercialAdapter {
    /**
     * 验证许可证
     * @return 验证结果
     */
    LicenseValidationResult validateLicense();
  
    /**
     * 获取商业特性列表
     * @return 特性列表
     */
    List<CommercialFeature> getAvailableFeatures();
  
    /**
     * 执行商业算法
     * @param feature 商业特性
     * @param parameters 参数
     * @return 执行结果
     */
    CommercialExecutionResult executeCommercialFeature(CommercialFeature feature, Map<String, Object> parameters);
}
```

## 6. 数据流设计

### 6.1 数据处理流程

```
用户请求 → 应用层 → 适配层 → 核心层 → 基础设施层
    ↑         ↓        ↓        ↓         ↓
    ← 响应结果 ← 结果处理 ← 业务逻辑 ← 数据存储
```

### 6.2 特性解耦流程

```
配置检查 → 特性开关 → 模块选择 → 功能执行 → 结果返回
    ↓         ↓         ↓         ↓         ↓
许可证验证  动态配置   适配器选择  算法执行   统一格式
```

## 7. 配置设计

### 7.1 配置文件结构

```yaml
terrabase:
  # 基础配置
  app:
    name: "Terrabase"
    version: "1.0.0"
    environment: "production"
  
  # 特性开关配置
  features:
    open_source:
      enabled: true
      data_enable: true
      algorithms: true
    commercial:
      enabled: false
      nexent: false
      modelengine: false
  
  # 模块配置
  modules:
    business_app:
      enabled: true
      config_path: "classpath:business-config.xml"
    enterprise_app:
      enabled: true
      config_path: "classpath:enterprise-config.xml"
  
  # 适配器配置
  adapters:
    open_source:
      class: "com.terrabase.enterprise.open.OpenSourceAdapterImpl"
      config:
        algorithm_library: "classpath:algorithms/"
        protocol_support: ["http", "https", "ftp"]
    commercial:
      class: "com.terrabase.enterprise.commercial.CommercialAdapterImpl"
      config:
        license_file: "license.key"
        nexent_endpoint: "https://nexent.com/api"
        modelengine_endpoint: "https://modelengine.com/api"
```

### 7.2 环境配置

#### 7.2.1 开发环境

- 启用所有开源特性
- 禁用商业特性
- 详细日志输出
- 开发工具支持

#### 7.2.2 测试环境

- 启用开源特性
- 模拟商业特性
- 性能监控
- 自动化测试

#### 7.2.3 生产环境

- 根据许可证启用特性
- 性能优化
- 安全加固
- 监控告警

## 8. 安全设计

### 8.1 安全架构

- **身份认证**：支持多种认证方式（用户名密码、OAuth、JWT等）
- **权限控制**：基于角色的访问控制（RBAC）
- **数据加密**：敏感数据加密存储和传输
- **审计日志**：完整的操作审计记录
- **安全配置**：安全相关的配置管理

### 8.2 许可证管理

- **许可证验证**：运行时许可证有效性检查
- **特性控制**：根据许可证级别控制功能访问
- **过期处理**：许可证过期后的降级策略
- **更新机制**：许可证在线更新和验证

## 9. 性能设计

### 9.1 性能优化策略

- **缓存策略**：多级缓存设计，提高数据访问性能
- **异步处理**：非阻塞异步处理，提高系统吞吐量
- **连接池**：数据库和外部服务连接池管理
- **负载均衡**：支持水平扩展和负载均衡
- **性能监控**：实时性能指标监控和告警

### 9.2 扩展性设计

- **水平扩展**：支持多实例部署和负载均衡
- **垂直扩展**：支持单实例资源扩展
- **模块化扩展**：支持新功能模块的动态加载
- **插件化架构**：支持第三方插件集成

## 10. 部署设计

### 10.1 部署架构

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   负载均衡器     │    │   负载均衡器     │    │   负载均衡器     │
│   (Nginx)      │    │   (Nginx)      │    │   (Nginx)      │
└─────────┬───────┘    └─────────┬───────┘    └─────────┬───────┘
          │                      │                      │
┌─────────▼───────┐    ┌─────────▼───────┐    ┌─────────▼───────┐
│  应用实例1      │    │  应用实例2      │    │  应用实例N      │
│  (business)    │    │  (enterprise)   │    │  (adapter)     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
          │                      │                      │
          └──────────────────────┼──────────────────────┘
                                 │
                    ┌─────────────▼─────────────┐
                    │        共享存储           │
                    │   (数据库、缓存、文件)      │
                    └───────────────────────────┘
```

### 10.2 部署模式

#### 10.2.1 单机部署

- 适用于开发和测试环境
- 所有模块部署在同一台服务器
- 简单的配置和管理

#### 10.2.2 集群部署

- 适用于生产环境
- 多实例负载均衡
- 高可用性和可扩展性

#### 10.2.3 容器化部署

- 支持Docker容器化部署
- 支持Kubernetes编排
- 便于环境一致性管理

## 11. 监控设计

### 11.1 监控指标

- **系统指标**：CPU、内存、磁盘、网络使用率
- **应用指标**：响应时间、吞吐量、错误率
- **业务指标**：数据处理量、成功率、性能指标
- **安全指标**：认证失败、权限拒绝、异常访问

### 11.2 监控工具

- **系统监控**：Prometheus + Grafana
- **日志监控**：ELK Stack (Elasticsearch + Logstash + Kibana)
- **链路追踪**：Jaeger 或 Zipkin
- **告警系统**：AlertManager

## 12. 测试设计

### 12.1 测试策略

- **单元测试**：模块级别的功能测试
- **集成测试**：模块间交互测试
- **系统测试**：端到端功能测试
- **性能测试**：负载和压力测试
- **安全测试**：安全漏洞和渗透测试

### 12.2 测试环境

- **开发环境**：本地开发测试
- **测试环境**：集成测试环境
- **预生产环境**：生产环境模拟
- **生产环境**：生产环境验证

## 13. 文档维护

### 13.1 文档更新策略

- 架构变更时同步更新文档
- 定期文档审查和更新
- 版本控制和变更记录
- 团队协作和知识共享

### 13.2 文档模板

- 统一的文档格式和模板
- 清晰的章节结构和导航
- 丰富的图表和示例
- 易于维护和更新

**文档版本历史**

| 版本 | 日期 | 变更内容 | 变更人 |
|------|------|----------|--------|
| v1.0.0 | 2025年 | 初始版本 | 架构团队 |

**审核记录**

| 审核人 | 审核日期 | 审核意见 | 状态 |
|--------|----------|----------|------| 
```
