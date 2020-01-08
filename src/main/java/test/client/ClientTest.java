package test.client;

import server.tools.Tools;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class ClientTest {
    private static String remoteHost = "127.0.0.1";
    private static int remotePort = 4000;
    private static SSLSocket socket;
    private static BufferedInputStream inputStream;
    private static BufferedOutputStream outputStream;

    public static void main(String[] args) throws IOException {
        System.setProperty("javax.net.ssl.keyStore", "C:/client_keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");

        SocketFactory socketFactory = SSLSocketFactory.getDefault();
        socket = (SSLSocket) socketFactory.createSocket(remoteHost, remotePort);
        socket.startHandshake();

        inputStream = new BufferedInputStream(socket.getInputStream());
        outputStream = new BufferedOutputStream(socket.getOutputStream());

        send("AUTHENTICATION;1;100002");
        String[] reply = receive(1024).split(";");
        String[] codes = reply[2].split(",");
        send("AUTHENTICATION;2;" + Tools.hmacMD5(Tools.hmacMD5(Tools.hmacMD5("password", codes[0]), codes[1]), codes[2]));
        String message = receive(1024);
        send("AUTHENTICATION;3;CREATE_SESSION");
        message = receive(1024);
        System.out.println("Session ID: " + message);

        close();
    }

    private static void send(String message) throws IOException {
        byte[] buffer = message.getBytes(StandardCharsets.UTF_8);
        outputStream.write(buffer, 0, buffer.length);
        outputStream.flush();
    }

    private static String receive(int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int messageSize = inputStream.read(buffer, 0, bufferSize);

        if (messageSize <= 0) {
            throw new IOException("No data received!");
        }

        return new String(buffer, StandardCharsets.UTF_8).trim();
    }

    private static void close() throws IOException {
        inputStream.close();
        outputStream.close();
        socket.close();
    }
}
