package com.terrabase.enterprise.impl.commercial;

import com.terrabase.enterprise.api.MenuService;
import com.terrabase.enterprise.api.dto.MenuRegisterInfo;
import com.terrabase.enterprise.api.dto.MenuInfo;
import com.terrabase.enterprise.api.response.ResultVo;
import com.terrabase.enterprise.impl.commercial.client.MenuFeignClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 商业版菜单管理服务实现
 * 提供菜单注册功能
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
@Service
public class CommercialMenuServiceImpl implements MenuService {
    
    private static final Logger logger = LoggerFactory.getLogger(CommercialMenuServiceImpl.class);
    
    @Autowired
    private MenuFeignClient menuFeignClient;
    
    @Override
    public void registerMenuInfo(MenuRegisterInfo menuRegisterInfo) {
        if (menuRegisterInfo == null) {
            logger.warn("菜单注册对象不能为空");
            return;
        }

        try {
            logger.info("商业版执行菜单注册 - 菜单ID: {}, 菜单名称: {}, URL: {}", 
                    menuRegisterInfo.getMenuId(), menuRegisterInfo.getMenuNameCode(), menuRegisterInfo.getUrl());
            
            // 使用Feign客户端进行菜单注册
            ResultVo<MenuInfo> result = menuFeignClient.registerMenuInfo(menuRegisterInfo);
            
            if (result != null && "200".equals(result.getCode())) {
                logger.info("商业版Feign客户端菜单注册成功 - 菜单ID: {}, 菜单名称: {}", 
                        menuRegisterInfo.getMenuId(), menuRegisterInfo.getMenuNameCode());
            } else {
                logger.error("商业版Feign客户端菜单注册失败 - 响应码: {}, 消息: {}", 
                        result != null ? result.getCode() : "null", 
                        result != null ? result.getMsg() : "null");
            }

        } catch (Exception e) {
            logger.error("商业版菜单注册失败: {}", menuRegisterInfo, e);
        }
    }
}
