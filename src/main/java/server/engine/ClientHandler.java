package server.engine;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


public class ClientHandler extends Thread {
    private Socket clientSocket;
    private BufferedInputStream inputStream;
    private BufferedOutputStream outputStream;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;

        System.out.println("[" + LocalDateTime.now() + "] A new client is connected! "
                           + clientSocket.getInetAddress().getHostAddress() + ":"
                           + clientSocket.getPort());
    }

    @Override
    public void run() {
        try {
            // Initialization
            inputStream = new BufferedInputStream(clientSocket.getInputStream());
            outputStream = new BufferedOutputStream(clientSocket.getOutputStream());

            // Operations
            String message = receive(1024);
            System.out.println("Received: " + message);
            send(message);
            
            // Close the socket
            close();

        } catch (Exception e) {
            System.err.println("[ERROR] ClientHandler exception: " + e.getMessage());
        }
    }

    private void send(String message) throws IOException {
        byte[] buffer = message.getBytes(StandardCharsets.UTF_8);
        outputStream.write(buffer, 0, buffer.length);
        outputStream.flush();
    }

    private String receive(int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int messageSize = inputStream.read(buffer, 0, bufferSize);

        if (messageSize <= 0) {
            throw new IOException("No data received!");
        }

        return new String(buffer, StandardCharsets.UTF_8).trim();
    }

    private void close() throws IOException {
        inputStream.close();
        outputStream.close();
        clientSocket.close();
    }
}
