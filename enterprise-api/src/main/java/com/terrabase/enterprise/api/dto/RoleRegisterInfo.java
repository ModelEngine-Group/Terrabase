package com.terrabase.enterprise.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleRegisterInfo {
    /**
     * 角色名
     */
    @Length(max = 64)
    @NotNull
    private String name;

    /**
     * 角色名国际化code代码
     */
    @NotNull
    private String nameCode;

    /**
     * 角色描述国际化code
     */
    private String description;

    /**
     * 是否可以创建此类用户 0 不支持 1 支持
     */
    private boolean creatable;

    /**
     * 角色支持创建的账户登录类型，0-未定义 1-人机 2-机机 3-人机和机机 4-内部登录
     * （以二进制具体为是否为1代表支持某类型，比如001表示人机，010表示机机，100表示内部，011表示人机和机机）
     */
    private Integer supportLoginType;
}
