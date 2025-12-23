package com.terrabase.enterprise.impl.commercial.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 机器令牌配置类，提供令牌的获取和刷新功能。
 * 通过静态方法管理机器令牌，其他类可直接调用获取或刷新令牌。
 * 
 * 采用直接执行命令的方式获取和解密机器令牌，无需依赖外部脚本文件。
 */
@Configuration
public class MachineTokenConfig {

	private static final Logger logger = LoggerFactory.getLogger(MachineTokenConfig.class);

	/**
	 * 存储解密后的机器令牌
	 */
	private static volatile String MACHINE_TOKEN_DECRYPTED = null;

	/**
	 * 获取机器令牌，如果令牌为空则自动调用脚本获取
	 * @return 解密后的机器令牌，如果获取失败返回null
	 */
	public static String getMachineToken() {
		if (MACHINE_TOKEN_DECRYPTED == null || MACHINE_TOKEN_DECRYPTED.isEmpty()) {
			refreshMachineToken();
		}
		return MACHINE_TOKEN_DECRYPTED;
	}

	/**
	 * 刷新机器令牌，强制重新调用脚本获取新令牌
	 * @return 是否成功获取到新令牌
	 */
	public static synchronized boolean refreshMachineToken() {
		String token = getTokenWithFallback();
		if (token != null && !token.isEmpty()) {
			MACHINE_TOKEN_DECRYPTED = token;
			return true;
		}
		return false;
	}

	/**
	 * 从脚本获取机器令牌（简化版本）
	 * 直接执行命令获取加密令牌，然后通过Python解密
	 * @return 解密后的机器令牌，失败时返回null
	 */
	private static String getTokenFromScript() {
		try {
			logger.info("开始执行机器令牌获取脚本...");
			
			// 执行脚本获取加密令牌
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", 
				"grep 'machine_token=' /opt/huawei/fce/runtime/security/priv/platform.conf | awk -F'=' '{print $2}'");
			Process process = pb.start();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String encryptedToken = reader.readLine();
			reader.close();
			
			if (encryptedToken == null || encryptedToken.trim().isEmpty()) {
				logger.warn("无法获取加密机器令牌");
				return null;
			}
			
			logger.info("成功获取加密机器令牌，开始解密...");
			
			// 执行Python脚本解密
			ProcessBuilder pythonPb = new ProcessBuilder("python", "-c", 
				"import kmc.kmc as K; import os; os.environ['KMC_DATA_USER']='tomcat'; machine_token='" + 
				encryptedToken.trim() + "'; plain_machine_token=K.API().decrypt(0,machine_token); print(plain_machine_token)");
			Process pythonProcess = pythonPb.start();
			
			BufferedReader pythonReader = new BufferedReader(new InputStreamReader(pythonProcess.getInputStream()));
			String decryptedToken = pythonReader.readLine();
			pythonReader.close();
			
			if (decryptedToken == null || decryptedToken.trim().isEmpty()) {
				logger.warn("机器令牌解密失败");
				return null;
			}
			
			logger.info("机器令牌获取成功");
			return decryptedToken.trim();
			
		} catch (Exception e) {
			logger.error("机器令牌获取脚本执行失败", e);
			return null;
		}
	}

	/**
	 * 获取机器令牌（带重试机制）
	 * 如果脚本获取失败，会尝试从环境变量获取
	 * @return 机器令牌
	 */
	public static String getTokenWithFallback() {
		// 首先尝试从脚本获取
		String token = getTokenFromScript();
		
		// 如果脚本获取失败，尝试从环境变量获取
		if (token == null || token.isEmpty()) {
			String envToken = System.getenv("MACHINE_TOKEN");
			if (envToken != null && !envToken.trim().isEmpty()) {
				logger.info("从环境变量获取机器令牌");
				return envToken;
			}
		}
		
		return token;
	}

}


