package com.terrabase.enterprise.impl.commercial.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * FeignClient内部调用前置拦截器
 */
@Slf4j
@Component
public class FeignInnerRequestAuthInterceptor implements RequestInterceptor {
    private static final String X_AUTH_TOKEN_NAME = "X-Auth-Token-Inner";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = MachineTokenConfig.getMachineToken();
        if (StringUtils.isBlank(token)) {
            log.warn("Failed to get machine token, using empty token for inner request");
            token = StringUtils.EMPTY;
        }
        requestTemplate.header(X_AUTH_TOKEN_NAME, token);
    }
}
