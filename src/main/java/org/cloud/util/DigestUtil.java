package org.cloud.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtil {

    public static String md5(String input) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(input.getBytes(StandardCharsets.UTF_8));
            byte[] digest = instance.digest();
            return HexUtil.bin2hex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5(String input, String salt) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update((input + salt).getBytes(StandardCharsets.UTF_8));
            byte[] digest = instance.digest();
            return HexUtil.bin2hex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
