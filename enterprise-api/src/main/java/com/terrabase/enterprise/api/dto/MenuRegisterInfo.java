package com.terrabase.enterprise.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuRegisterInfo {
    @NotNull
    private String menuId;

    private String parentMenuId;

    @JsonProperty("menuName")
    private String menuNameCode;

    private String url;

    private String iconUrl;

    private String en;

    private String zh;

    private boolean enable;

    private List<RoleScenInfo> roleScneMap;

    private int weight;
}