package com.terrabase.enterprise.impl.commercial.client;

import com.terrabase.enterprise.api.dto.CertCollectInfo;
import com.terrabase.enterprise.api.response.ResultVo;
import com.terrabase.enterprise.impl.commercial.config.FeignInnerRequestAuthInterceptor;
import com.terrabase.enterprise.impl.commercial.config.HttpsFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Component
@FeignClient(value = "Framework", path = "/framework/v1/certificate", contextId = "me-cert-collect",
        configuration = {HttpsFeignClientConfig.class, FeignInnerRequestAuthInterceptor.class})
public interface CertFeignClient {
    /**
     * 获取oms管理的所有证书信息
     *
     * @return 证书信息
     */
    @GetMapping(value = "/action/cert/collect", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResultVo<List<CertCollectInfo>> listCertificateServiceList();
}
