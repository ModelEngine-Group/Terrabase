package com.terrabase.enterprise.impl.commercial.client;

import com.terrabase.enterprise.api.dto.LoginUserDto;
import com.terrabase.enterprise.api.response.ResultVo;
import com.terrabase.enterprise.impl.commercial.config.FeignInnerRequestAuthInterceptor;
import com.terrabase.enterprise.impl.commercial.config.HttpsFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Component
@FeignClient(value = "Framework", path = "/framework/v1", contextId = "frameworkRole",
        configuration = {HttpsFeignClientConfig.class, FeignInnerRequestAuthInterceptor.class})
public interface ManualAuthenticationClient {
    /**
     * 调用oms侧接口，如果鉴权成功会返回角色名
     *
     * @return ResultVo<List<String>>
     */
    @PostMapping(value = "/iam/roles/query-by-token")
    ResultVo<List<String>> queryRolesByToken();

    /**
     * 调用oms侧接口，返回当前的用户信息
     *
     * @return Result<List<LoginUserDto>>
     */
    @GetMapping(value = "/sessions/current/internal")
    ResultVo<List<LoginUserDto>> sessionCur();
}
