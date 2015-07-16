package org.semanticwb.auth.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Logger;

/**
 * Created by serch on 7/15/15.
 */
public class CryptoUtil {
    static Logger log = Logger.getLogger("o.s.a.u.CryptoUtil");
    static SecureRandom random = null;
    static {
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(System.currentTimeMillis());
        } catch (NoSuchAlgorithmException nsae){
            log.throwing("CryptoUtil","StaticInitializer", nsae);
        }
    }

    public static String byteToBase64(byte[] data){
        return Base64.getEncoder().encodeToString(data);
    }

    public static byte[] base64ToByte(String data){
        return Base64.getDecoder().decode(data);
    }

    public static byte[] generateSalt() {
        byte[] data = new byte[8];
        random.nextBytes(data);
        return data;
    }

    public static byte[] getSimpleHash(String password, byte[] salt)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(salt);
        return digest.digest(password.getBytes("UTF-8"));
    }
}
