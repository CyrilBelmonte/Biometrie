package server.tools;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;


public class AES {
    private static final String DEFAULT_INIT_VECTOR = "#a4eLn!Mp_71vC/#";
    private AES() {}

    public static String encrypt(String value, String key) {
        return encrypt(value, key, DEFAULT_INIT_VECTOR);
    }

    public static String encrypt(String value, String key, String initVector) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8)));

            byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeBase64String(encrypted);

        } catch (Exception e) {
            System.err.println("[ERROR] AES encryption exception: " + e.getMessage());
            return null;
        }
    }

    public static String decrypt(String value, String key) {
        return decrypt(value, key, DEFAULT_INIT_VECTOR);
    }

    public static String decrypt(String value, String key, String initVector) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8)));

            byte[] original = cipher.doFinal(Base64.decodeBase64(value));
            return new String(original, StandardCharsets.UTF_8);

        } catch (Exception e) {
            System.err.println("[ERROR] AES decryption exception: " + e.getMessage());
            return null;
        }
    }
}
