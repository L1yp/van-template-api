package org.cloud.web.service.system.impl;

import org.cloud.cache.CacheTTL;
import org.cloud.util.ImageCaptchaGenerator;
import org.cloud.web.service.system.ICaptchaService;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;

@Service
@CacheConfig(cacheNames = "captcha")
public class CaptchaServiceImpl implements ICaptchaService {


    @Value("${captcha.candidate:abcdefghjkmnpqrstuvwxyz23456789}")
    String candidateString;

    @Value("${captcha.width:200}")
    int width;

    @Value("${captcha.height:100}")
    int height;


    @CacheTTL(120L)
    @CachePut(key = "#token", unless = "#result == null")
    public String generateCaptchaImage(String token, OutputStream os) throws IOException {
        String captchaText = ImageCaptchaGenerator.generateCaptchaText(candidateString, 4);
        ImageCaptchaGenerator.writeCaptchaImageToOutputStream(captchaText, width, height, os);
        return captchaText;
    }

    public boolean verifyCaptchaCode(String token, String input) {
        // FIXME 正式机去掉
        if (input.equals("1111")) {
            return true;
        }
        ICaptchaService captchaService  = (ICaptchaService) AopContext.currentProxy();
        String captchaCode = captchaService.getCaptchaCode(token);
        return input.equals(captchaCode);
    }


    @Cacheable(key = "#token", unless = "#result == null")
    public String getCaptchaCode(String token) {
        return null;
    }

    @CacheTTL(120L)
    @CacheEvict(key = "#token")
    public void removeCaptchaCodeCache(String token) { }


}
