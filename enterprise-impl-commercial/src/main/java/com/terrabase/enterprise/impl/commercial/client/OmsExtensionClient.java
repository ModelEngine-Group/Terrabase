package com.terrabase.enterprise.impl.commercial.client;

import com.terrabase.enterprise.api.dto.ResourceGroup;
import com.terrabase.enterprise.impl.commercial.config.FeignInnerRequestAuthInterceptor;
import com.terrabase.enterprise.impl.commercial.config.HttpsFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Component
@FeignClient(name = "oms", path = "/rpc/v1", url = "${oms-extension.api.url:https://oms-extension:8021}",
        configuration = {FeignInnerRequestAuthInterceptor.class, HttpsFeignClientConfig.class})
public interface OmsExtensionClient {
    @GetMapping("/{userName}/user-resource-groups")
    List<ResourceGroup> getUserGroups(@PathVariable("userName")  String userName);
}
