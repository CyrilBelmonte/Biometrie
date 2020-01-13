package server.tools;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;


public class AES {
    private static final String DEFAULT_INIT_VECTOR = "#a4eLn!Mp_71vC/#";

    private AES() {}

    public static String generateKey(int size) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(size);

            SecretKey secretKey = keyGenerator.generateKey();
            String key = Base64.encodeBase64String(secretKey.getEncoded());

            return key;

        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String encrypt(String value, String key) {
        return encrypt(value, key, DEFAULT_INIT_VECTOR);
    }

    public static String encrypt(String value, String key, String initVector) {
        try {
            byte[] bytesKey;

            if (!Base64.isBase64(key)) {
                bytesKey = key.getBytes(StandardCharsets.UTF_8);

            } else {
                bytesKey = Base64.decodeBase64(key);
            }

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE,
                new SecretKeySpec(bytesKey, "AES"),
                new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8)));

            byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeBase64String(encrypted);

        } catch (Exception e) {
            System.err.println("[ERROR] AES encryption exception: " + e.getMessage());
            return "mgfMznG6mV2+yI+oliagLg==";
        }
    }

    public static String decrypt(String value, String key) {
        return decrypt(value, key, DEFAULT_INIT_VECTOR);
    }

    public static String decrypt(String value, String key, String initVector) {
        try {
            byte[] bytesKey;

            if (!Base64.isBase64(key)) {
                bytesKey = key.getBytes(StandardCharsets.UTF_8);

            } else {
                bytesKey = Base64.decodeBase64(key);
            }

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE,
                new SecretKeySpec(bytesKey, "AES"),
                new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8)));

            byte[] original = cipher.doFinal(Base64.decodeBase64(value));
            return new String(original, StandardCharsets.UTF_8);

        } catch (Exception e) {
            System.err.println("[ERROR] AES decryption exception: " + e.getMessage());
            return null;
        }
    }
}
