package org.cloud.web.service.system.impl;

import org.cloud.util.ImageCaptchaGenerator;
import org.cloud.web.model.DTO.out.system.CaptchaImageDTO;
import org.cloud.web.service.system.ICaptchaService;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

@Service
@CacheConfig(cacheNames = "captcha")
public class CaptchaServiceImpl implements ICaptchaService {


    @Value("${captcha.candidate:abcdefghjkmnpqrstuvwxyz23456789}")
    String candidateString;

    @Value("${captcha.width:200}")
    int width;

    @Value("${captcha.height:100}")
    int height;

    @Value("${captcha.enable:false}")
    boolean enable;

    @CachePut(key = "#param.captchaToken + '#PT120S'")
    public String generateCaptchaImage(CaptchaImageDTO param) {
        String captchaText = ImageCaptchaGenerator.generateCaptchaText(candidateString, 4);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageCaptchaGenerator.writeCaptchaImageToOutputStream(captchaText, width, height, os);
            byte[] byteArray = os.toByteArray();

            String base64Data = Base64.getEncoder().encodeToString(byteArray);
            param.setData("data:image/png;base64," + base64Data);

            return captchaText;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean verifyCaptchaCode(String token, String input) {
        if (!enable) {
            return true;
        }
        ICaptchaService captchaService  = (ICaptchaService) AopContext.currentProxy();
        String captchaCode = captchaService.getCaptchaCode(token);
        return input.equals(captchaCode);
    }

    @Override
    public boolean forceVerifyCaptchaCode(String token, String input) {
        ICaptchaService captchaService  = (ICaptchaService) AopContext.currentProxy();
        String captchaCode = captchaService.getCaptchaCode(token);
        return input.equalsIgnoreCase(captchaCode);
    }


    @Cacheable(key = "#token + '#PT120S'", unless = "#result == null")
    public String getCaptchaCode(String token) {
        return null;
    }

    @CacheEvict(key = "#token + '#PT120S'")
    public void removeCaptchaCodeCache(String token) { }


}
