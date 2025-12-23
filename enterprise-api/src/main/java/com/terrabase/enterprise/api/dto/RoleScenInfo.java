package com.terrabase.enterprise.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleScenInfo {
    @JsonProperty("menuId")
    private String menuId;

    @JsonProperty("roleName")
    private String roleName;

    @JsonProperty("scenId")
    private String scenId;
}
