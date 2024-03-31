package org.cloud.web.service.system.impl;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cloud.exception.BusinessException;
import org.cloud.web.service.system.ICaptchaService;
import org.cloud.web.service.system.IMailService;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@Slf4j
@CacheConfig(cacheNames = "mail")
public class MailServiceImpl implements IMailService {

    @Resource
    private TemplateEngine templateEngine;

    @Resource
    ICaptchaService captchaService;

    @Resource
    JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    String from;


    @CachePut(key = "#type + ':' + #param + '#PT180S'")
    public String setVerifyCode(String type, String param, String code, String captchaToken, String captchaCode) {
        if (!captchaService.forceVerifyCaptchaCode(captchaToken, captchaCode)) {
            throw new BusinessException(400, "图形验证码有误");
        }
        if (StringUtils.isNotBlank(captchaToken)) {
            captchaService.removeCaptchaCodeCache(captchaToken);
        }
        return code;
    }

    @Override
    @Cacheable(key = "#type + ':' + #param + '#PT180S'", unless = "#result == null")
    public String getVerifyCode(String type, String param) {
        return null;
    }

    @Override
    @CacheEvict(key = "#type + ':' + #param + '#PT180S'")
    public void removeVerifyCodeInCache(String type, String param) {

    }

    @Override
    public void sendVerifyCode(String subject, String to, String verifyCode) {
        MimeMessage message = mailSender.createMimeMessage();
        try {

            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);// 第二个参数表示支持附件和内联资源
            messageHelper.setFrom(from);
            messageHelper.setSubject(subject);
            messageHelper.setTo(to);
            Context context = new Context();
            context.setVariables(Map.of("verifyCode", verifyCode));
            String html = templateEngine.process("mail/code.html", context);
            messageHelper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


}
