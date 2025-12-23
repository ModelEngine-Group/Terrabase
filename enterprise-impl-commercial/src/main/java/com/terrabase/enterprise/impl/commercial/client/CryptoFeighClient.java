package com.terrabase.enterprise.impl.commercial.client;

import com.terrabase.enterprise.api.dto.Ciphertext;
import com.terrabase.enterprise.api.dto.Plaintext;
import com.terrabase.enterprise.api.response.ResultVo;
import com.terrabase.enterprise.impl.commercial.config.FeignInnerRequestAuthInterceptor;
import com.terrabase.enterprise.impl.commercial.config.HttpsFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(value = "Framework", path = "/framework/v1/crypto", contextId = "me-crypto",
        configuration = {HttpsFeignClientConfig.class, FeignInnerRequestAuthInterceptor.class})
public interface CryptoFeighClient {
    /**
     * 加密接口 数据加密，传入要加密的明文，返回加密后的id
     *
     * @param key 加密id
     * @param plaintext 要加密的明文
     * @param username 用户名
     * @return 加密后的id
     */
    @PostMapping({"/{key}/actions/encrypt/internal"})
    ResultVo<Ciphertext> encrypt(@PathVariable("key") String key, @RequestBody Plaintext plaintext,
                                 @RequestHeader("username") String username);

    @PostMapping({"/{key}/actions/decrypt/internal"})
    ResultVo<Plaintext> decrypt(@PathVariable("key") String key, @RequestHeader("username") String username);
}
