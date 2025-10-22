package com.terrabase.enterprise.impl.commercial.client;

import com.terrabase.enterprise.api.dto.MenuInfo;
import com.terrabase.enterprise.api.dto.MenuRegisterInfo;
import com.terrabase.enterprise.impl.commercial.config.FeignInnerRequestAuthInterceptor;
import com.terrabase.enterprise.impl.commercial.config.HttpsFeignClientConfig;
import com.terrabase.enterprise.api.response.ResultVo;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(value = "Framework", path = "/framework/v1/customize", contextId = "settings-menu",
configuration = {HttpsFeignClientConfig.class, FeignInnerRequestAuthInterceptor.class})
public interface MenuFeignClient {
    @PostMapping(value = "/menu/register/internal", consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    ResultVo<MenuInfo> registerMenuInfo(@RequestBody MenuRegisterInfo menuRegisterInfo);
}
