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
    private static final String REMOTE_HOST = "127.0.0.1";
    private static final int REMOTE_PORT = 4000;
    private static final String KEYSTORE_LOCATION = "C:/client_keystore.jks";
    private static final String KEYSTORE_PASSWORD = "password";

    private static SSLSocket socket;
    private static BufferedInputStream inputStream;
    private static BufferedOutputStream outputStream;

    public static void main(String[] args) throws Exception {
        String sessionKey;
        String[] reply;

        // Authentication
        openSocket();
        send("AUTHENTICATION;1;100002");
        reply = receiveAndPrepare(1024);
        String[] codes = reply[2].split(",");
        send("AUTHENTICATION;2;" + Tools.hmacMD5(Tools.hmacMD5(Tools.hmacMD5("123456", codes[0]), codes[1]), codes[2]));
        String[] biometricData = receiveAndPrepare(4096);
        send("AUTHENTICATION;3;CREATE_SESSION"); // send("AUTHENTICATION;3;TERMINATE");
        reply = receiveAndPrepare(1024);
        sessionKey = reply[2];
        System.out.println("Session key: " + sessionKey);
        close();

        // GET
        openSocket();
        //sessionKey = "dcdd9808-7e87-4f3e-ac1d-4abb92157f8e";
        send("GET;IS_ADMIN;" + sessionKey);
        reply = receiveAndPrepare(1024);
        System.out.println("Is admin ? " + reply[2]);
        close();

        // CREATE
        openSocket();
        //sessionKey = "dcdd9808-7e87-4f3e-ac1d-4abb92157f8e";
        send("CREATE;Tom,DOE,tom.doe@cergy.fr,false,1,2,7a7fd7808e663cbf88bded12a5098c14,ZRoRIAaTUcATXwc7oqlTUQ==;" + sessionKey);
        reply = receiveAndPrepare(1024);
        close();

        // DELETE
        openSocket();
        //sessionKey = "dcdd9808-7e87-4f3e-ac1d-4abb92157f8e";
        send("DELETE;SESSION;" + sessionKey);
        reply = receiveAndPrepare(1024);
        close();
    }

    private static void openSocket() throws IOException {
        System.setProperty("javax.net.ssl.keyStore", KEYSTORE_LOCATION);
        System.setProperty("javax.net.ssl.keyStorePassword", KEYSTORE_PASSWORD);

        SocketFactory socketFactory = SSLSocketFactory.getDefault();
        socket = (SSLSocket) socketFactory.createSocket(REMOTE_HOST, REMOTE_PORT);
        socket.startHandshake();

        inputStream = new BufferedInputStream(socket.getInputStream());
        outputStream = new BufferedOutputStream(socket.getOutputStream());
    }

    private static void send(String message) throws IOException {
        Tools.printLogMessageErr("<-----", "Payload: " + message.replace(";", " "));
        byte[] buffer = message.getBytes(StandardCharsets.UTF_8);
        outputStream.write(buffer, 0, buffer.length);
        outputStream.flush();
    }

    private static String receive(int bufferSize) throws Exception {
        byte[] buffer = new byte[bufferSize];
        int messageSize = inputStream.read(buffer, 0, bufferSize);

        if (messageSize <= 0) {
            throw new Exception("Socket closed before finishing the process");
        }

        return new String(buffer, StandardCharsets.UTF_8).trim();
    }

    private static String[] receiveAndPrepare(int bufferSize) throws Exception {
        String[] reply = receive(bufferSize).split(";");
        Tools.printLogMessageErr("----->", "Payload: " + String.join(" ", reply));
        return reply;
    }

    private static void close() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();

        } catch (Exception e) {}
    }
}
