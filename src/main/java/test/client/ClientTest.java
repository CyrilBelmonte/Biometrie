package test.client;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;


public class ClientTest {
    private static String remoteHost = "127.0.0.1";
    private static int remotePort = 4000;

    public static void main(String[] args) throws IOException {
        System.setProperty("javax.net.ssl.keyStore", "C:/client_keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");

        SocketFactory socketFactory = SSLSocketFactory.getDefault();
        SSLSocket socket = (SSLSocket) socketFactory.createSocket(remoteHost, remotePort);
        socket.startHandshake();

        BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
        BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());

        byte[] message = "Test message".getBytes();
        int messageSize = message.length;

        outputStream.write(message, 0, messageSize);
        outputStream.flush();

        byte[] serverMessage = new byte[2048];
        int serverMessageSize = inputStream.read(serverMessage, 0, 2048);

        if (serverMessageSize <= 0) {
            throw new IOException("No data received!");
        }

        System.out.println("Received: " + new String(serverMessage, "UTF-8"));

        inputStream.close();
        outputStream.close();
        socket.close();
    }
}
