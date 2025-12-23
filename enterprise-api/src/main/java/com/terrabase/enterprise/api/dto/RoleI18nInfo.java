package com.terrabase.enterprise.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleI18nInfo {
    /**
     * 角色名
     */
    private String name;

    /**
     * 角色关键字
     */
    @NotNull
    private String code;

    /**
     * 语言
     */
    @NotNull
    private String language;

    /**
     * 角色的描述内容
     */
    @NotNull
    private String content;
}
