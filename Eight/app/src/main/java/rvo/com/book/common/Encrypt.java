package rvo.com.book.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Robert on 29/03/2018.
 */

public class Encrypt {

    public static String encryptString(String string) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(string.getBytes(StandardCharsets.UTF_8));
            StringBuffer encryptedString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                encryptedString.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
            }
            return encryptedString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
