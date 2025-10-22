package com.terrabase.business.controller;

import com.terrabase.business.util.JarLoadUtil;
import com.terrabase.enterprise.api.UserManagementService;
import com.terrabase.enterprise.api.dto.AuthorityInfo;
import com.terrabase.enterprise.api.dto.LoginUserDto;
import com.terrabase.enterprise.api.dto.ResourceGroup;
import com.terrabase.enterprise.api.request.RoleRegisterVo;
import com.terrabase.enterprise.api.response.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理服务控制器
 * 负责提供用户管理相关的REST API接口
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/enterprise/user")
@CrossOrigin(origins = "*")
public class UserManagementController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserManagementController.class);
    
    @Autowired
    private JarLoadUtil jarLoadUtil;
    
    /**
     * 批量角色注册
     */
    @PostMapping("/roles/register")
    public ResponseEntity<Map<String, Object>> batchRegisterRole(@RequestBody RoleRegisterVo roleRegister) {
        try {
            if (roleRegister == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "角色注册信息不能为空");
                return ResponseEntity.badRequest().body(error);
            }
            
            UserManagementService userService = jarLoadUtil.loadUserManagementService();
            userService.batchRegisterRole(roleRegister);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "角色批量注册成功");
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("批量角色注册失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("status", "failed");
            error.put("error", "批量角色注册失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * 批量权限注册
     */
    @PostMapping("/permissions/register")
    public ResponseEntity<Map<String, Object>> registerPermission(@RequestBody List<AuthorityInfo> authorityInfos) {
        try {
            if (authorityInfos == null || authorityInfos.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "权限信息列表不能为空");
                return ResponseEntity.badRequest().body(error);
            }
            
            UserManagementService userService = jarLoadUtil.loadUserManagementService();
            userService.registerPermission(authorityInfos);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "权限批量注册成功");
            response.put("permissionCount", authorityInfos.size());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("批量权限注册失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("status", "failed");
            error.put("error", "批量权限注册失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * 获取用户资源组列表
     */
    @GetMapping("/{username}/groups")
    public ResponseEntity<Map<String, Object>> getUserGroups(@PathVariable String username) {
        try {
            if (username == null || username.trim().isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "用户名不能为空");
                return ResponseEntity.badRequest().body(error);
            }
            
            UserManagementService userService = jarLoadUtil.loadUserManagementService();
            List<ResourceGroup> resourceGroups = userService.getUserGroups(username);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("username", username);
            response.put("resourceGroups", resourceGroups);
            response.put("groupCount", resourceGroups != null ? resourceGroups.size() : 0);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("获取用户资源组列表失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("status", "failed");
            error.put("error", "获取用户资源组列表失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * 根据token查询角色名
     */
    @GetMapping("/roles/by-token")
    public ResponseEntity<Map<String, Object>> queryRolesByToken() {
        try {
            UserManagementService userService = jarLoadUtil.loadUserManagementService();
            ResultVo<List<String>> result = userService.queryRolesByToken();
            
            Map<String, Object> response = new HashMap<>();
            if ("200".equals(result.getCode())) {
                response.put("status", "success");
                response.put("roles", result.getData());
                response.put("roleCount", result.getData() != null ? result.getData().size() : 0);
                response.put("timestamp", System.currentTimeMillis());
            } else {
                response.put("status", "failed");
                response.put("error", result.getMsg());
                response.put("code", result.getCode());
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("根据token查询角色名失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("status", "failed");
            error.put("error", "根据token查询角色名失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/current-user")
    public ResponseEntity<Map<String, Object>> getCurrentUserInfo() {
        try {
            UserManagementService userService = jarLoadUtil.loadUserManagementService();
            ResultVo<List<LoginUserDto>> result = userService.getCurrentUserInfo();
            
            Map<String, Object> response = new HashMap<>();
            if ("200".equals(result.getCode())) {
                response.put("status", "success");
                response.put("users", result.getData());
                response.put("userCount", result.getData() != null ? result.getData().size() : 0);
                response.put("timestamp", System.currentTimeMillis());
            } else {
                response.put("status", "failed");
                response.put("error", result.getMsg());
                response.put("code", result.getCode());
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("获取当前用户信息失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("status", "failed");
            error.put("error", "获取当前用户信息失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
}
