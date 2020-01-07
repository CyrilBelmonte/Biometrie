package test.server;

import server.engine.Server;


public class ServerTest {
    // Génération des clés (à faire pour le client et le serveur)
    // "C:\Program Files\Java\jdk1.8.0_211\bin\keytool.exe" -genkey -alias client -keyalg RSA -keystore C:\client_keystore.jks -keysize 2048
    // "C:\Program Files\Java\jdk1.8.0_211\bin\keytool.exe" -genkey -alias server -keyalg RSA -keystore C:\server_keystore.jks -keysize 2048

    // Exportation du certificat du serveur
    // "C:\Program Files\Java\jdk1.8.0_211\bin\keytool.exe" -export -alias server -file C:\server.crt -keystore C:\server_keystore.jks

    // Import du certificat sur le client dans le magasin de certificats de confiance
    // "C:\Program Files\Java\jdk1.8.0_211\bin\keytool.exe" -import -trustcacerts -alias server -file C:\server.crt -keystore "C:\Program Files\Java\jdk1.8.0_211\jre\lib\security\cacerts"

    // Lister clés / certificats
    // "C:\Program Files\Java\jdk1.8.0_211\bin\keytool.exe" -list -v -keystore C:\client_keystore.jks

    // Supprimer clé / certificat
    // "C:\Program Files\Java\jdk1.8.0_211\bin\keytool.exe" -delete -alias client -keystore C:\client_keystore.jks

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("javax.net.ssl.keyStore", "C:/server_keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");

        Server server = new Server(4000);
        server.listenBlock();
        // server.shutdown();
    }
}
