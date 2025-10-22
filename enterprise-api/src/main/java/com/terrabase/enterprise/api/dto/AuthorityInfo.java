package com.terrabase.enterprise.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityInfo {
    /**
     * 资源唯一标识
     */
    @JsonProperty("Description")
    private String description;

    /**
     * 资源唯一标识
     */
    @JsonProperty("ResourceKey")
    private String resourceKey;

    /**
     * 是否跳过权限检查
     */
    @JsonProperty("SkipCheck")
    private boolean skipCheck;

    /**
     * 所需角色
     */
    @JsonProperty("Roles")
    private List<String> roles = new ArrayList<>(10);
}




