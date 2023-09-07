package org.cloud.web.service.system;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@Slf4j
public class MailService {

    @Resource
    private TemplateEngine templateEngine;

    @Resource
    JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    String from;


    public void sendVerifyCode(String subject, String to, String verifyCode) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);// 第二个参数表示支持附件和内联资源
            messageHelper.setFrom(from);
            messageHelper.setSubject(subject);
            messageHelper.setTo(to);
            Context context = new Context();
            context.setVariables(Map.of("verifyCode", verifyCode));
            String html = templateEngine.process("mail/account-bind.html", context);
            messageHelper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


}
