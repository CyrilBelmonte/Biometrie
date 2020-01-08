package server.tools;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;


public class Tools {
    private Tools() {}

    public static String hmacMD5(String message, String key) {
        return hmacDigest(message, key, "HmacMD5");
    }

    public static String hmacDigest(String message, String key, String algorithm) {
        String digest = null;

        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(secretKeySpec);

            byte[] bytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();

            for (byte byte_ : bytes) {
                String hex = Integer.toHexString(0xFF & byte_);

                if (hex.length() == 1) {
                    hash.append('0');
                }

                hash.append(hex);
            }

            digest = hash.toString();

        } catch (Exception e) {}

        return digest;
    }

    public static int getRandomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static int getSeed() {
        return getRandomNumber(100000, 999999);
    }

    public static void printLogMessage(String source, String message) {
        System.out.println("[" + LocalDateTime.now() + "] [" + source + "] " + message);
    }

    public static void printLogMessageErr(String source, String error) {
        System.err.println("[" + LocalDateTime.now() + "] [" + source + "] " + error);
    }
}
