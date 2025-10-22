package com.terrabase.enterprise.impl.commercial;

import com.terrabase.enterprise.api.CryptoService;
import com.terrabase.enterprise.api.CryptoAlgorithm;
import com.terrabase.enterprise.api.dto.Ciphertext;
import com.terrabase.enterprise.api.dto.Plaintext;
import com.terrabase.enterprise.api.response.ResultVo;
import com.terrabase.enterprise.impl.commercial.client.CryptoFeighClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 商业版加解密服务实现
 * 集成商业组件实现加解密功能
 * 
 * @author Yehong Pan
 * @version 1.0.0
 */
@Service
public class CommercialCryptoServiceImpl implements CryptoService {
    
    private static final Logger logger = LoggerFactory.getLogger(CommercialCryptoServiceImpl.class);

    private final CryptoFeighClient cryptoFeighClient;

    public CommercialCryptoServiceImpl(CryptoFeighClient cryptoFeighClient) {
        this.cryptoFeighClient = cryptoFeighClient;
    }
    

    @Override
    public String encrypt(String plaintext, CryptoAlgorithm algorithm, String username) {

        if (plaintext == null || plaintext.trim().isEmpty()) {
            return "明文数据不能为空";
        }

        try {
            logger.info("商业版执行数据加密，原文长度: {}, 用户: {}", plaintext.length(), username);

            // 生成UUID作为keyId
            String keyId = UUID.randomUUID().toString();

            // 调用Feign客户端进行加密
            ResultVo<Ciphertext> result = cryptoFeighClient.encrypt(keyId, new Plaintext(plaintext), username);

            if (result != null && "200".equals(result.getCode()) && result.getData() != null) {
                String returnedKeyId = result.getData().getKeyId();
                logger.info("商业版Feign加密成功，返回keyId: {}", returnedKeyId);
                return returnedKeyId;
            }

            logger.error("商业版Feign加密失败: {}", (result != null ? result.getMsg() : "结果为空"));
            return "加密失败: " + (result != null ? result.getMsg() : "结果为空");

        } catch (Exception e) {
            logger.error("商业版加密失败", e);
            return "加密失败: " + e.getMessage();
        }
    }

    @Override
    public String decrypt(String ciphertext, CryptoAlgorithm algorithm, String username) {

        if (ciphertext == null || ciphertext.trim().isEmpty()) {
            return "密文数据不能为空";
        }

        try {
            logger.info("商业版执行数据解密，密文长度: {}, 用户: {}", ciphertext.length(), username);

            // ciphertext在新语义下为keyId
            String keyId = ciphertext;

            // 调用Feign客户端进行解密
            ResultVo<Plaintext> result = cryptoFeighClient.decrypt(keyId, username);

            if (result != null && "200".equals(result.getCode()) && result.getData() != null) {
                String plain = result.getData().getPlain();
                logger.info("商业版Feign解密成功，返回明文长度: {}", (plain != null ? plain.length() : 0));
                return plain;
            }

            logger.error("商业版Feign解密失败: {}", (result != null ? result.getMsg() : "结果为空"));
            return "解密失败: " + (result != null ? result.getMsg() : "结果为空");

        } catch (Exception e) {
            logger.error("商业版解密失败", e);
            return "解密失败: " + e.getMessage();
        }
    }
}
