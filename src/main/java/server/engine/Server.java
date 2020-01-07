package server.engine;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.Socket;


public class Server {
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void listen() {
        try {
            ServerSocketFactory serverSocketFactory = SSLServerSocketFactory.getDefault();
            SSLServerSocket sslServerSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(port);

            System.out.println("[SERVER] Listening on port " + port + "...");

            while (true) {
                Socket clientSocket = sslServerSocket.accept();
                System.out.println("[SERVER] Connecting with " + clientSocket.getRemoteSocketAddress() + "...");

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }

            // sslServerSocket.close();

        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}
