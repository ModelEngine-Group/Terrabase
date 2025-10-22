package com.terrabase.enterprise.impl.open;

import com.terrabase.enterprise.api.MenuService;
import com.terrabase.enterprise.api.dto.MenuRegisterInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 开源版菜单管理服务实现
 * 提供菜单注册功能
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
@Service
public class OpenMenuServiceImpl implements MenuService {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenMenuServiceImpl.class);

    @Override
    public void registerMenuInfo(MenuRegisterInfo menuRegisterInfo) {

        if (menuRegisterInfo == null) {
            logger.warn("菜单注册对象不能为空");
            return;
        }
        
        try {
            logger.info("开源版执行菜单注册 - 菜单ID: {}, 菜单名称: {}, URL: {}", 
                    menuRegisterInfo.getMenuId(), menuRegisterInfo.getMenuNameCode(), menuRegisterInfo.getUrl());
        } catch (Exception e) {
            logger.error("开源版菜单注册失败: {}", menuRegisterInfo, e);
        }
    }
}
