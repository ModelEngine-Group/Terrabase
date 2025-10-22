package com.terrabase.enterprise.api.request;

import com.terrabase.enterprise.api.dto.EventDefine;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterEventDefineReq {
    private static final int MAX_SIZE = 1000;

    private @NotBlank String serviceName;

    private String ServiceEn;

    private String ServiceZh;

    private boolean deleteAll = false;

    private @Valid @NotEmpty @Size(
            max=1000
    ) List<EventDefine> eventDefines;
}
