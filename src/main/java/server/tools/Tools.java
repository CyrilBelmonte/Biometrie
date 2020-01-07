package server.tools;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public class Tools {
    private Tools() {}

    public static String hmacMD5(String message, String key) {
        return hmacDigest(message, key, "HmacMD5");
    }

    public static String hmacDigest(String message, String key, String algorithm) {
        String digest = null;

        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(secretKeySpec);

            byte[] bytes = mac.doFinal(message.getBytes("UTF-8"));
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
}
