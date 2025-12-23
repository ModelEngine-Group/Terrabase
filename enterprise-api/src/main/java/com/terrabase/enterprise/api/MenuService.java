package com.terrabase.enterprise.api;

import com.terrabase.enterprise.api.dto.MenuRegisterInfo;

/**
 * 菜单管理服务接口
 * 提供菜单注册功能
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
public interface MenuService {
    /**
     * 注册菜单信息
     * @param menuRegisterInfo 菜单注册信息对象
     */
    void registerMenuInfo(MenuRegisterInfo menuRegisterInfo);
}
