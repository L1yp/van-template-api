package org.cloud.web.service.system;

import org.cloud.web.model.DTO.out.system.CaptchaImageDTO;

import java.io.IOException;
import java.io.OutputStream;

public interface ICaptchaService {

    /**
     * 生成验证码图片
     * @param token 验证码Token
     * @param os 输出流
     * @return 验证码文字
     * @throws IOException 写入流失败抛出
     */
    String generateCaptchaImage(CaptchaImageDTO param);

    /**
     * 验证验证码, 若全局关闭后会直接返回true
     * @param token 验证码Token
     * @param input 用户输入
     * @return 是否正确
     */
    boolean verifyCaptchaCode(String token, String input);

    /**
     * 强制验证验证码
     * @param token 验证码Token
     * @param input 用户输入
     * @return 是否正确
     */
    boolean forceVerifyCaptchaCode(String token, String input);


    /**
     * 获取验证码
     */
    String getCaptchaCode(String token);

    /**
     * 移除验证码缓存
     * @param token 验证码token
     */
    void removeCaptchaCodeCache(String token);

}
