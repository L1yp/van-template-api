package org.cloud.web.service.system;

public interface IMailService {


    /**
     * 设置邮件验证码缓存你
     * @param type 类型
     * @param param 参数
     * @param code 验证码
     * @param captchaToken 图形验证码Token
     * @param captchaCode 图形验证码
     * @return CachePut缓存结果
     */
    String setVerifyCode(String type, String param, String code, String captchaToken, String captchaCode);

    /**
     * 获取验证码缓存
     * @param type 类型
     * @param param 参数
     * @return 验证码
     */
    String getVerifyCode(String type, String param);

    /**
     * 移除验证码缓存
     * @param type 类型
     * @param param 参数
     * @return 验证码
     */
    void removeVerifyCodeInCache(String type, String param);


    /**
     * 发送邮件验证码
     * @param subject 邮件主题
     * @param to 目标邮箱
     * @param verifyCode 验证码内容
     */
    void sendVerifyCode(String subject, String to, String verifyCode);

}
