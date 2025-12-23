package com.terrabase.enterprise.impl.commercial.client;

import com.terrabase.enterprise.api.dto.LicenseInfo;
import com.terrabase.enterprise.api.response.ResultVo;
import com.terrabase.enterprise.impl.commercial.config.FeignInnerRequestAuthInterceptor;
import com.terrabase.enterprise.impl.commercial.config.HttpsFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(value = "Framework", path = "/framework/v1/license", contextId = "edm-license",
        configuration = {HttpsFeignClientConfig.class, FeignInnerRequestAuthInterceptor.class})
public interface LicenseClient {
    @GetMapping(value = "/info")
    ResultVo<LicenseInfo> getLicenseInfo();
}
