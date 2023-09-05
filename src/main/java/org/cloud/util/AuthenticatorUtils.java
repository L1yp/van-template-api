package org.cloud.util;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey.Builder;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

public class AuthenticatorUtils {

    public static String generateSecretKey() {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }

    public static String generateOTPAuthURL(String secretKey, String accountName, String issuer) {
        GoogleAuthenticatorKey key = new Builder(secretKey).build();
        return GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(issuer, accountName, key);
    }

    public static boolean verifyCode(String secretKey, String userCode) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        return gAuth.authorize(secretKey, Integer.parseInt(userCode));
    }
}
