package com.terrabase.enterprise.api.request;

import com.terrabase.enterprise.api.dto.RoleI18nInfo;
import com.terrabase.enterprise.api.dto.RoleRegisterInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoleRegisterVo {
    private List<RoleRegisterInfo> roleRegisterInfos;

    private List<RoleI18nInfo> roleI18nInfos;
}
