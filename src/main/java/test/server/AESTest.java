package test.server;

import server.tools.AES;


public class AESTest {
    public static void main(String[] args) {
        String message = "Message de test";
        String key = "AZERTYUIOPQSDFGH"; // 128 bits
        // key = "AZERTYUIOPQSDFGHAZERTYUIOPQSDFGH"; // 256 bits
        String encrypted = AES.encrypt(message, key);
        String decrypted = AES.decrypt(encrypted, key);

        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypted);
    }
}
