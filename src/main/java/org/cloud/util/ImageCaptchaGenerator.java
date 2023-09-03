package org.cloud.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class ImageCaptchaGenerator {

    public static void writeCaptchaImageToOutputStream(String captchaText, int width, int height, OutputStream os) throws IOException {
        BufferedImage image = generateCaptchaImage(width, height, captchaText); // 生成验证码图片
        ImageIO.write(image, "png", os);
    }

    // 生成指定长度的验证码文本
    public static String generateCaptchaText(String candidateLetter, int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(candidateLetter.length());
            sb.append(candidateLetter.charAt(index));
        }
        return sb.toString();
    }

    // 生成验证码图片
    private static BufferedImage generateCaptchaImage(int width, int height, String text) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.setColor(Color.BLACK);
        g.drawString(text, 20, 35);
        g.dispose();
        return image;
    }
}
