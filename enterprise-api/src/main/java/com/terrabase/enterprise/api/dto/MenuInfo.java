package com.terrabase.enterprise.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuInfo {
    private int id;

    private String menuId;

    private String parentMenuId;

    private String menuName;

    private String url;

    private String en;

    private String zh;

    private boolean enable;
}
