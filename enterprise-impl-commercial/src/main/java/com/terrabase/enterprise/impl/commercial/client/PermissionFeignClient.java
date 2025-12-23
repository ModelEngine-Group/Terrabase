package com.terrabase.enterprise.impl.commercial.client;

import com.terrabase.enterprise.api.dto.AuthorityInfo;
import com.terrabase.enterprise.api.response.ResultVo;
import com.terrabase.enterprise.impl.commercial.config.FeignInnerRequestAuthInterceptor;
import com.terrabase.enterprise.impl.commercial.config.HttpsFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
@FeignClient(value = "Framework", path = "/framework/v1/iam/permission", contextId = "eDataMate-permission",
        configuration = {HttpsFeignClientConfig.class, FeignInnerRequestAuthInterceptor.class})
public interface PermissionFeignClient {
    /**
     * 批量注册权限
     *
     * @param authorityInfos 待注册权限实体列表
     * @return 注册结果
     */
    @PostMapping(value = "/batch/register/internal", consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    ResultVo<String> registerPermission(@RequestBody List<AuthorityInfo> authorityInfos);
}
