package com.terrabase.enterprise.impl.commercial.client;

import com.terrabase.enterprise.api.request.RoleRegisterVo;
import com.terrabase.enterprise.api.response.ResultVo;
import com.terrabase.enterprise.impl.commercial.config.FeignInnerRequestAuthInterceptor;
import com.terrabase.enterprise.impl.commercial.config.HttpsFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(value = "Framework", path = "/framework/v1/iam/roles", contextId = "me-role",
        configuration = {HttpsFeignClientConfig.class, FeignInnerRequestAuthInterceptor.class})
public interface RoleFeignClient {
    /**
     * 创建新角色
     *
     * @param roleRegisterVo 待创建新角色
     * @return 创建结果
     */
    @PostMapping(value = "/batch/register/internal", consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    ResultVo<String> batchRegisterRole(@RequestBody RoleRegisterVo roleRegisterVo);
}
