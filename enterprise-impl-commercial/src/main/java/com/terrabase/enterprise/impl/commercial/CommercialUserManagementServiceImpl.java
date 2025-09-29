package com.terrabase.enterprise.impl.commercial;

import com.terrabase.enterprise.api.UserManagementService;
import com.terrabase.enterprise.api.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 商业版用户管理服务实现
 * 集成商业组件实现用户管理功能
 * 
 * @author Terrabase Team
 * @version 1.0.0
 */
@Service
public class CommercialUserManagementServiceImpl implements UserManagementService {
    
    private static final Logger logger = LoggerFactory.getLogger(CommercialUserManagementServiceImpl.class);

    private final AtomicBoolean running = new AtomicBoolean(false);
    
    private final RestTemplate restTemplate;
    
    private String omsBaseUrl;
    private int timeout;
    
    public CommercialUserManagementServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        // 设置默认值
        this.omsBaseUrl = "http://localhost:8081/api/";
        this.timeout = 30000;
    }
    
    public CommercialUserManagementServiceImpl(RestTemplate restTemplate, String omsBaseUrl, int timeout) {
        this.restTemplate = restTemplate;
        this.omsBaseUrl = omsBaseUrl;
        this.timeout = timeout;
    }
    
    @Override
    public String getServiceName() {
        return "Commercial User Management Service";
    }
    
    @Override
    public String getServiceVersion() {
        return "1.0.0-commercial";
    }
    
    @Override
    public String getServiceType() {
        return "commercial";
    }
    
    @Override
    public String getHealthStatus() {
        if (!running.get()) {
            return "服务未运行";
        }
        
        return String.format("商业版用户管理服务健康状态: 正常 (版本: %s, 类型: %s)", 
                getServiceVersion(), getServiceType());
    }

    @Override
    public void start() {
        if (running.compareAndSet(false, true)) {
            logger.info("商业版用户管理服务已启动");
        } else {
            logger.warn("商业版用户管理服务已经在运行中");
        }
    }

    @Override
    public void stop() {
        if (running.compareAndSet(true, false)) {
            logger.info("商业版用户管理服务已停止");
        } else {
            logger.warn("商业版用户管理服务已经停止");
        }
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    // ========== 用户注册相关接口实现 ==========

    @Override
    public void registerRole(RoleRegister roleRegister) {
        if (!running.get()) {
            logger.warn("服务未运行，无法执行角色注册操作");
            return;
        }

        if (roleRegister == null) {
            logger.warn("角色注册对象不能为空");
            return;
        }

        try {
            logger.info("商业版执行角色注册: {}", roleRegister);
            
            // 构建请求参数
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("roleId", roleRegister.getRoleId());
            requestBody.put("roleName", roleRegister.getRoleName());
            requestBody.put("applicationScenario", roleRegister.getApplicationScenario());
            requestBody.put("roleDescription", roleRegister.getRoleDescription());
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // 构建角色注册接口URL
            String registerRoleUrl = omsBaseUrl + "/framework/v1/iam/roles/batch/register/internal";
            
            // 调用REST接口进行角色注册
            ResponseEntity<Map> response = restTemplate.postForEntity(registerRoleUrl, requestEntity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                String status = (String) responseBody.get("status");
                
                if ("success".equals(status)) {
                    logger.info("商业版REST接口角色注册成功 - 角色ID: {}, 角色名称: {}, 应用场景: {}",
                            roleRegister.getRoleId(), roleRegister.getRoleName(), roleRegister.getApplicationScenario());
                } else {
                    String errorMsg = (String) responseBody.get("message");
                    logger.error("商业版REST接口角色注册失败: {}", errorMsg);
                }
            } else {
                logger.error("商业版REST接口角色注册失败，HTTP状态码: {}", response.getStatusCode());
            }

        } catch (RestClientException e) {
            logger.error("商业版REST接口角色注册调用异常", e);
        } catch (Exception e) {
            logger.error("商业版角色注册失败: {}", roleRegister, e);
        }
    }

    @Override
    public void registerAuthority(AuthorityInfos authorityInfos) {
        if (!running.get()) {
            logger.warn("服务未运行，无法执行权限注册操作");
            return;
        }

        if (authorityInfos == null || authorityInfos.getAuthorityList() == null) {
            logger.warn("权限注册对象不能为空");
            return;
        }

        try {
            logger.info("商业版执行权限注册，权限数量: {}", authorityInfos.getAuthorityList().size());
            
            // 构建请求参数
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("authorityList", authorityInfos.getAuthorityList());
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // 构建权限注册接口URL
            String registerAuthorityUrl = omsBaseUrl + "/framework/v1/iam/permission/batch/register/internal";
            
            // 调用REST接口进行权限注册
            ResponseEntity<Map> response = restTemplate.postForEntity(registerAuthorityUrl, requestEntity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                String status = (String) responseBody.get("status");
                
                if ("success".equals(status)) {
                    logger.info("商业版REST接口权限注册成功，权限数量: {}", authorityInfos.getAuthorityList().size());
                    for (AuthorityInfos.AuthorityInfo authorityInfo : authorityInfos.getAuthorityList()) {
                        logger.info("商业版权限注册成功 - 权限ID: {}, 权限名称: {}, 权限类型: {}",
                                authorityInfo.getAuthorityId(), authorityInfo.getAuthorityName(), authorityInfo.getAuthorityType());
                    }
                } else {
                    String errorMsg = (String) responseBody.get("message");
                    logger.error("商业版REST接口权限注册失败: {}", errorMsg);
                }
            } else {
                logger.error("商业版REST接口权限注册失败，HTTP状态码: {}", response.getStatusCode());
            }
            
        } catch (RestClientException e) {
            logger.error("商业版REST接口权限注册调用异常", e);
        } catch (Exception e) {
            logger.error("商业版权限注册失败: {}", authorityInfos, e);
        }
    }

    @Override
    public void registerMenu(MenuRegisterInfo menuRegisterInfo) {
        if (!running.get()) {
            logger.warn("服务未运行，无法执行菜单注册操作");
            return;
        }

        if (menuRegisterInfo == null || menuRegisterInfo.getMenuList() == null) {
            logger.warn("菜单注册对象不能为空");
            return;
        }

        try {
            logger.info("商业版执行菜单注册，菜单数量: {}", menuRegisterInfo.getMenuList().size());
            
            // 构建请求参数
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("menuList", menuRegisterInfo.getMenuList());
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // 构建菜单注册接口URL
            String registerMenuUrl = omsBaseUrl + "/framework/v1/customize/menu/register/internal";
            
            // 调用REST接口进行菜单注册
            ResponseEntity<Map> response = restTemplate.postForEntity(registerMenuUrl, requestEntity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                String status = (String) responseBody.get("status");
                
                if ("success".equals(status)) {
                    logger.info("商业版REST接口菜单注册成功，菜单数量: {}", menuRegisterInfo.getMenuList().size());
                    for (MenuRegisterInfo.MenuInfo menuInfo : menuRegisterInfo.getMenuList()) {
                        logger.info("商业版菜单注册成功 - 菜单ID: {}, 菜单名称: {}, 菜单路径: {}",
                                menuInfo.getMenuId(), menuInfo.getMenuName(), menuInfo.getMenuPath());
                    }
                } else {
                    String errorMsg = (String) responseBody.get("message");
                    logger.error("商业版REST接口菜单注册失败: {}", errorMsg);
                }
            } else {
                logger.error("商业版REST接口菜单注册失败，HTTP状态码: {}", response.getStatusCode());
            }

        } catch (RestClientException e) {
            logger.error("商业版REST接口菜单注册调用异常", e);
        } catch (Exception e) {
            logger.error("商业版菜单注册失败: {}", menuRegisterInfo, e);
        }
    }

    @Override
    public void registerMenuForbidden(ForbiddenBody forbiddenBody) {
        if (!running.get()) {
            logger.warn("服务未运行，无法执行菜单屏蔽注册操作");
            return;
        }

        if (forbiddenBody == null || forbiddenBody.getForbiddenMenuIds() == null) {
            logger.warn("菜单屏蔽对象不能为空");
            return;
        }

        try {
            logger.info("商业版执行菜单屏蔽注册，屏蔽菜单数量: {}", forbiddenBody.getForbiddenMenuIds().size());
            
            // 构建请求参数
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("forbiddenMenuIds", forbiddenBody.getForbiddenMenuIds());
            requestBody.put("reason", forbiddenBody.getReason());
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // 构建菜单屏蔽注册接口URL
            String registerMenuForbiddenUrl = omsBaseUrl + "/framework/v1/customize/menu/register/forbidden/item/internal";
            
            // 调用REST接口进行菜单屏蔽注册
            ResponseEntity<Map> response = restTemplate.postForEntity(registerMenuForbiddenUrl, requestEntity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                String status = (String) responseBody.get("status");
                
                if ("success".equals(status)) {
                    logger.info("商业版REST接口菜单屏蔽注册成功，屏蔽菜单数量: {}", forbiddenBody.getForbiddenMenuIds().size());
                    for (String menuId : forbiddenBody.getForbiddenMenuIds()) {
                        logger.info("商业版菜单屏蔽注册成功 - 菜单ID: {}, 屏蔽原因: {}", menuId, forbiddenBody.getReason());
                    }
                } else {
                    String errorMsg = (String) responseBody.get("message");
                    logger.error("商业版REST接口菜单屏蔽注册失败: {}", errorMsg);
                }
            } else {
                logger.error("商业版REST接口菜单屏蔽注册失败，HTTP状态码: {}", response.getStatusCode());
            }

        } catch (RestClientException e) {
            logger.error("商业版REST接口菜单屏蔽注册调用异常", e);
        } catch (Exception e) {
            logger.error("商业版菜单屏蔽注册失败: {}", forbiddenBody, e);
        }
    }

    // ========== 时间管理相关接口实现 ==========

    @Override
    public void subscribeTimeConfigChange(Subscribe subscribe) {
        if (!running.get()) {
            logger.warn("服务未运行，无法执行时间配置变更事件订阅操作");
            return;
        }

        if (subscribe == null) {
            logger.warn("订阅信息对象不能为空");
            return;
        }

        try {
            logger.info("商业版执行时间配置变更事件订阅: {}", subscribe);
            
            // 构建请求参数
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("subscribeId", subscribe.getSubscribeId());
            requestBody.put("serviceName", subscribe.getServiceName());
            requestBody.put("notifyAddress", subscribe.getNotifyAddress());
            requestBody.put("eventType", subscribe.getEventType());
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // 构建时间配置变更事件订阅接口URL
            String subscribeTimeConfigUrl = omsBaseUrl + "/framework/v1/iam/subscribe/internal";
            
            // 调用REST接口进行时间配置变更事件订阅
            ResponseEntity<Map> response = restTemplate.postForEntity(subscribeTimeConfigUrl, requestEntity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                String status = (String) responseBody.get("status");
                
                if ("success".equals(status)) {
                    logger.info("商业版REST接口时间配置变更事件订阅成功 - 订阅ID: {}, 服务名称: {}, 通知地址: {}",
                            subscribe.getSubscribeId(), subscribe.getServiceName(), subscribe.getNotifyAddress());
                } else {
                    String errorMsg = (String) responseBody.get("message");
                    logger.error("商业版REST接口时间配置变更事件订阅失败: {}", errorMsg);
                }
            } else {
                logger.error("商业版REST接口时间配置变更事件订阅失败，HTTP状态码: {}", response.getStatusCode());
            }

        } catch (RestClientException e) {
            logger.error("商业版REST接口时间配置变更事件订阅调用异常", e);
        } catch (Exception e) {
            logger.error("商业版时间配置变更事件订阅失败: {}", subscribe, e);
        }
    }
}
