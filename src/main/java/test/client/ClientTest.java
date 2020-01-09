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

    public static void main(String[] args) throws IOException {
        System.setProperty("javax.net.ssl.keyStore", "C:/client_keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");

        // AUTHENTICATION
        SSLSocket socket = createSocket(remoteHost, remotePort);
        BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
        BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());

        send("AUTHENTICATION;1;100002", outputStream);
        String[] reply = receive(1024, inputStream).split(";");
        String[] codes = reply[2].split(",");
        send("AUTHENTICATION;2;" + Tools.hmacMD5(Tools.hmacMD5(Tools.hmacMD5("password", codes[0]), codes[1]), codes[2]), outputStream);
        String biometricData = receive(1024, inputStream);
        send("AUTHENTICATION;3;CREATE_SESSION", outputStream);
        String[] reply2 = receive(1024, inputStream).split(";");
        String sessionKey = reply2[2];
        System.out.println("Session key: " + sessionKey);

        inputStream.close();
        outputStream.close();
        socket.close();

        // GET TEST
        socket = createSocket(remoteHost, remotePort);
        inputStream = new BufferedInputStream(socket.getInputStream());
        outputStream = new BufferedOutputStream(socket.getOutputStream());

        send("GET;IS_ADMIN;" + sessionKey, outputStream);
        String reply3 = receive(1024, inputStream);
        System.out.println(reply3);

        inputStream.close();
        outputStream.close();
        socket.close();

        // INSERT TEST
        socket = createSocket(remoteHost, remotePort);
        inputStream = new BufferedInputStream(socket.getInputStream());
        outputStream = new BufferedOutputStream(socket.getOutputStream());

        send("CREATE;Tom,DOE,tom.doe@cergy.fr,false,1,2,7a7fd7808e663cbf88bded12a5098c14,ZRoRIAaTUcATXwc7oqlTUQ==;" + sessionKey, outputStream);
        String reply4 = receive(1024, inputStream);
        System.out.println(reply4);

        inputStream.close();
        outputStream.close();
        socket.close();
    }

    private static void send(String message, BufferedOutputStream outputStream) throws IOException {
        byte[] buffer = message.getBytes(StandardCharsets.UTF_8);
        outputStream.write(buffer, 0, buffer.length);
        outputStream.flush();
    }

    private static String receive(int bufferSize, BufferedInputStream inputStream) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int messageSize = inputStream.read(buffer, 0, bufferSize);

        if (messageSize <= 0) {
            throw new IOException("No data received!");
        }

        return new String(buffer, StandardCharsets.UTF_8).trim();
    }

    public static SSLSocket createSocket(String remoteHost, int remotePort) throws IOException {
        SocketFactory socketFactory = SSLSocketFactory.getDefault();
        SSLSocket socket = (SSLSocket) socketFactory.createSocket(remoteHost, remotePort);
        socket.startHandshake();

        return socket;
    }
}
