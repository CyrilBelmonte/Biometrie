package server.engine;

import server.tools.Tools;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.net.Socket;


public class Server extends Thread {
    private int port;
    private SSLServerSocket sslServerSocket;
    private boolean isStopped;

    public Server(int port) {
        this.port = port;
        this.isStopped = false;
    }

    public void listen() {
        start();
    }

    public void listenAndBlock() {
        listen();

        try {
            join();

        } catch (Exception e) {}
    }

    @Override
    public void run() {
        try {
            sslServerSocket = (SSLServerSocket) SSLServerSocketFactory
                .getDefault()
                .createServerSocket(port);

            Tools.printLogMessage(" main ", "Listening on port " + port + "...");

            while (!isStopped) {
                Socket clientSocket = sslServerSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }

        } catch (Exception e) {}
    }

    public void shutdown() {
        try {
            isStopped = true;
            sslServerSocket.close();
            join();

        } catch (Exception e) {}
    }
}
