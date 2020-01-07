package server.engine;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ClientHandler extends Thread {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedInputStream inputStream = new BufferedInputStream(clientSocket.getInputStream());
            BufferedOutputStream outputStream = new BufferedOutputStream(clientSocket.getOutputStream());

            byte[] clientMessage = new byte[2048];
            int messageSize = inputStream.read(clientMessage, 0, 2048);

            if (messageSize <= 0) {
                throw new IOException("No data received!");
            }

            System.out.println("Received: " + new String(clientMessage, "UTF-8"));

            outputStream.write(clientMessage, 0, messageSize);
            outputStream.flush();

            inputStream.close();
            outputStream.close();
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
