package server.engine;

import server.dao.DAOFactory;
import server.model.Session;
import server.model.User;
import server.tools.SessionsManager;
import server.tools.Tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class ClientHandler extends Thread {
    private int clientID;
    private Socket clientSocket;
    private BufferedInputStream inputStream;
    private BufferedOutputStream outputStream;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.clientID = Tools.getRandomNumber(100000, 999999);

        Tools.printLogMessage(String.valueOf(clientID),
            "A new client is connected! "
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
            String[] request = receiveAndPrepare(1024);

            if (request[0].equals("AUTHENTICATION")) {
                // --------------------------------------------------------------------
                // AUTHENTICATION PHASE 1
                // --------------------------------------------------------------------
                // Starting the authentication process
                int phase = Integer.parseInt(request[1]);
                Tools.printLogMessage(String.valueOf(clientID), "AUTHENTICATION PHASE " + phase + " REQUEST RECEIVED");
                Tools.printLogMessage(String.valueOf(clientID), "=======================================");

                if (phase != 1) {
                    send("REPLY;ERROR;PHASE_ERROR");
                    throw new Exception("Error in the authentication phase " + phase);
                }

                // Retrieve the user
                int id = Integer.parseInt(request[2]);
                Tools.printLogMessage(String.valueOf(clientID), " | Retrieving the user (ID: " + id + ") from the database...");
                User user = DAOFactory.getUserDAO().find(id);

                if (user == null) {
                    send("REPLY;ERROR;UNKNOWN_USER");
                    throw new Exception("Unable to find the user with the ID: " + id);
                }

                Tools.printLogMessage(String.valueOf(clientID), " | The user is " + user.getFirstName() + " " + user.getLastName());

                // Generating a seed
                Tools.printLogMessage(String.valueOf(clientID), " | Generating a seed (G)...");
                int seed = Tools.getSeed();
                Tools.printLogMessage(String.valueOf(clientID), " | Seed G: " + seed);

                // Sending x, y, g
                Tools.printLogMessage(String.valueOf(clientID), " | Sending of X: " + user.getX() + ", Y: " + user.getY() + ", G: " + seed + "...");
                send("AUTHENTICATION;" + phase + ";" + user.getX() + "," + user.getY() + "," + seed);

                // --------------------------------------------------------------------
                // AUTHENTICATION PHASE 2
                // --------------------------------------------------------------------
                // Waiting for the reply (Z)
                request = receiveAndPrepare(1024);
                phase = Integer.parseInt(request[1]);
                Tools.printLogMessage(String.valueOf(clientID), "AUTHENTICATION PHASE " + phase + " REQUEST RECEIVED");
                Tools.printLogMessage(String.valueOf(clientID), "=======================================");

                if (phase != 2) {
                    send("REPLY;ERROR;PHASE_ERROR");
                    throw new Exception("Error in the authentication phase " + phase);
                }

                String z = request[2];
                Tools.printLogMessage(String.valueOf(clientID), " | Calculating the HMAC password with the seed G (Z') and comparing with the received Z...");
                String zBis = Tools.hmacMD5(user.getPassword(), String.valueOf(seed));
                Tools.printLogMessage(String.valueOf(clientID), " | Z : " + z);
                Tools.printLogMessage(String.valueOf(clientID), " | Z': " + zBis);

                if (!z.equals(zBis)) {
                    send("REPLY;ERROR;PASSWORD_ERROR");
                    throw new Exception("The password is incorrect");
                }

                Tools.printLogMessage(String.valueOf(clientID), " | The password is valid!");

                // Sending the biometric data
                Tools.printLogMessage(String.valueOf(clientID), " | Sending the encrypted biometric data...");
                send("AUTHENTICATION;" + phase + ";" + user.getBiometricData());

                // --------------------------------------------------------------------
                // AUTHENTICATION PHASE 3
                // --------------------------------------------------------------------
                // Waiting for the reply (session)
                request = receiveAndPrepare(1024);
                phase = Integer.parseInt(request[1]);
                Tools.printLogMessage(String.valueOf(clientID), "AUTHENTICATION PHASE " + phase + " REQUEST RECEIVED");
                Tools.printLogMessage(String.valueOf(clientID), "=======================================");

                if (phase != 3) {
                    send("REPLY;ERROR;PHASE_ERROR");
                    throw new Exception("Error in the authentication phase " + phase);
                }

                if (request[2].equals("CREATE_SESSION")) {
                    Tools.printLogMessage(String.valueOf(clientID), " | Generating the session...");
                    Session session = SessionsManager.getInstance().createSession(user);
                    Tools.printLogMessage(String.valueOf(clientID), " | Session ID: " + session.getKey());
                    Tools.printLogMessage(String.valueOf(clientID), " | The session is valid until (in case of inactivity): " + session.getExpirationDate());
                    Tools.printLogMessage(String.valueOf(clientID), " | Sending the session key...");
                    send("AUTHENTICATION;" + phase + ";" + session.getKey());

                } else if (request[2].equals("TERMINATE")) {
                    Tools.printLogMessage(String.valueOf(clientID), " | No session will be generated");

                } else {
                    send("REPLY;BAD_REQUEST;UNKNOWN_COMMAND");
                    throw new Exception("Bad request");
                }

                Tools.printLogMessage(String.valueOf(clientID), "AUTHENTICATION PHASE COMPLETED!");

            } else if (request[0].equals("GET")) {
                Tools.printLogMessage(String.valueOf(clientID), "CLIENT -> SERVER GET REQUEST");
                Tools.printLogMessage(String.valueOf(clientID), "=======================================");
                String requestedData = request[1];
                String sessionKey = request[2];

                if (!SessionsManager.getInstance().hasSession(sessionKey)) {
                    send("REPLY;ERROR;UNKNOWN_SESSION");
                    throw new Exception("The session key is incorrect or expired: " + sessionKey);
                }

                Tools.printLogMessage(String.valueOf(clientID), " | Requested data: " + requestedData);
                Tools.printLogMessage(String.valueOf(clientID), " | Session key: " + sessionKey);
                Tools.printLogMessage(String.valueOf(clientID), " | The session key is valid!");

                Session session = SessionsManager.getInstance().getSession(sessionKey);
                User user = session.getUser();

                Tools.printLogMessage(String.valueOf(clientID), " | User corresponding to the session: " + user.getFirstName() + " " + user.getLastName());

                if (!request[1].equals("IS_ADMIN")) {
                    send("REPLY;BAD_REQUEST;UNKNOWN_COMMAND");
                    throw new Exception("Bad request");
                }

                Tools.printLogMessage(String.valueOf(clientID), " | Check if the user has administration privileges...");

                if (user.isAdmin()) {
                    Tools.printLogMessage(String.valueOf(clientID), " | " + user.getFirstName() + " " + user.getLastName() + " is administrator!");
                    send("REPLY;OK;true");

                } else {
                    Tools.printLogMessage(String.valueOf(clientID), " | " + user.getFirstName() + " " + user.getLastName() + " is a standard user");
                    send("REPLY;OK;false");
                }

            } else if (request[0].equals("CREATE")) {
                Tools.printLogMessage(String.valueOf(clientID), "CLIENT -> SERVER CREATE REQUEST");
                Tools.printLogMessage(String.valueOf(clientID), "=======================================");
                String data = request[1];
                String sessionKey = request[2];

                if (!SessionsManager.getInstance().hasSession(sessionKey)) {
                    send("REPLY;ERROR;UNKNOWN_SESSION");
                    throw new Exception("The session key is incorrect or expired: " + sessionKey);
                }

                Tools.printLogMessage(String.valueOf(clientID), " | Data: " + data);
                Tools.printLogMessage(String.valueOf(clientID), " | Session key: " + sessionKey);
                Tools.printLogMessage(String.valueOf(clientID), " | The session key is valid!");

                Session session = SessionsManager.getInstance().getSession(sessionKey);
                User user = session.getUser();

                Tools.printLogMessage(String.valueOf(clientID), " | User corresponding to the session: " + user.getFirstName() + " " + user.getLastName());
                Tools.printLogMessage(String.valueOf(clientID), " | Check if " + user.getFirstName() + " " + user.getLastName() + " has the required privileges to create a user...");

                if (!user.isAdmin()) {
                    send("REPLY;ERROR;PRIVILEGES_ERROR");
                    throw new Exception("Admin privileges are required to perform this action: CREATE");
                }

                Tools.printLogMessage(String.valueOf(clientID), " | Access granted!");

                String[] splitData = data.split(",");

                if (splitData.length != 8) {
                    send("REPLY;BAD_REQUEST;MALFORMED_DATA");
                    throw new Exception("Bad request");
                }

                String firstName = splitData[0];
                String lastName = splitData[1];
                String email = splitData[2];
                boolean isAdmin = Boolean.valueOf(splitData[3]);
                int x = Integer.valueOf(splitData[4]);
                int y = Integer.valueOf(splitData[5]);
                String password = splitData[6];
                String biometricData = splitData[7];

                Tools.printLogMessage(String.valueOf(clientID), " | Inserting of the user into the database: " + firstName + " " + lastName + "...");

                User userToInsert = new User(firstName, lastName, email, isAdmin, x, y, password, biometricData);
                boolean hasSucceeded = DAOFactory.getUserDAO().insert(userToInsert);

                if (hasSucceeded) {
                    Tools.printLogMessage(String.valueOf(clientID), " | OK!");
                    send("REPLY;OK;" + userToInsert.getId());

                } else {
                    Tools.printLogMessageErr(String.valueOf(clientID), " | Insertion error");
                    send("REPLY;ERROR;NULL");
                }

            } else if (request[0].equals("UPDATE")) {
                Tools.printLogMessage(String.valueOf(clientID), "CLIENT -> SERVER UPDATE REQUEST");
                Tools.printLogMessage(String.valueOf(clientID), "=======================================");
                String data = request[1];
                String sessionKey = request[2];

                if (!SessionsManager.getInstance().hasSession(sessionKey)) {
                    send("REPLY;ERROR;UNKNOWN_SESSION");
                    throw new Exception("The session key is incorrect or expired: " + sessionKey);
                }

                Tools.printLogMessage(String.valueOf(clientID), " | Data: " + data);
                Tools.printLogMessage(String.valueOf(clientID), " | Session key: " + sessionKey);
                Tools.printLogMessage(String.valueOf(clientID), " | The session key is valid!");

                Session session = SessionsManager.getInstance().getSession(sessionKey);
                User user = session.getUser();

                Tools.printLogMessage(String.valueOf(clientID), " | User corresponding to the session: " + user.getFirstName() + " " + user.getLastName());
                Tools.printLogMessage(String.valueOf(clientID), " | Check if " + user.getFirstName() + " " + user.getLastName() + " has the required privileges to update a user...");

                if (!user.isAdmin()) {
                    send("REPLY;ERROR;PRIVILEGES_ERROR");
                    throw new Exception("Admin privileges are required to perform this action: UPDATE");
                }

                Tools.printLogMessage(String.valueOf(clientID), " | Access granted!");

                // Do something
                // ...

                send("REPLY;ERROR;NOT_IMPLEMENTED");

            } else if (request[0].equals("DELETE")) {
                Tools.printLogMessage(String.valueOf(clientID), "CLIENT -> SERVER DELETE REQUEST");
                Tools.printLogMessage(String.valueOf(clientID), "=======================================");
                String data = request[1];
                String sessionKey = request[2];

                if (!SessionsManager.getInstance().hasSession(sessionKey)) {
                    send("REPLY;ERROR;UNKNOWN_SESSION");
                    throw new Exception("The session key is incorrect or expired: " + sessionKey);
                }

                Tools.printLogMessage(String.valueOf(clientID), " | Data: " + data);
                Tools.printLogMessage(String.valueOf(clientID), " | Session key: " + sessionKey);
                Tools.printLogMessage(String.valueOf(clientID), " | The session key is valid!");

                if (!request[1].equals("SESSION")) {
                    send("REPLY;BAD_REQUEST;UNKNOWN_COMMAND");
                    throw new Exception("Bad request");
                }

                Tools.printLogMessage(String.valueOf(clientID), " | Invalidating the session...");
                Session session = SessionsManager.getInstance().getSession(sessionKey);
                session.invalidate();
                Tools.printLogMessage(String.valueOf(clientID), " | Session destroyed!");

                send("REPLY;OK;NULL");
            }

        } catch (IOException e) {
            System.err.println("[ERROR] ClientHandler exception: " + e.getMessage());

        } catch (Exception e) {
            Tools.printLogMessageErr(String.valueOf(clientID), " | " + e.getMessage());

        } finally {
            try {
                // Close the socket
                close();

            } catch (IOException e) {}
        }
    }

    private void send(String message) throws IOException {
        Tools.printLogMessageErr("<-----", "Payload: " + message.replace(";", " "));
        byte[] buffer = message.getBytes(StandardCharsets.UTF_8);
        outputStream.write(buffer, 0, buffer.length);
        outputStream.flush();
    }

    private String receive(int bufferSize) throws Exception {
        byte[] buffer = new byte[bufferSize];
        int messageSize = inputStream.read(buffer, 0, bufferSize);

        if (messageSize <= 0) {
            throw new Exception("Socket closed before finishing the process");
        }

        return new String(buffer, StandardCharsets.UTF_8).trim();
    }

    private String[] receiveAndPrepare(int bufferSize) throws Exception {
        String[] request = receive(bufferSize).split(";");
        Tools.printLogMessageErr("----->", "Payload: " + String.join(" ", request));

        // Check the request format
        if (request.length != 3 || !(
            request[0].equals("AUTHENTICATION") ||
            request[0].equals("GET") ||
            request[0].equals("CREATE") ||
            request[0].equals("UPDATE") ||
            request[0].equals("DELETE"))) {

            // We alert the client that the request is invalid
            send("REPLY;BAD_REQUEST;UNKNOWN_COMMAND");

            // We throw an exception to close the socket
            throw new Exception("Bad request");
        }

        return request;
    }

    private void close() throws IOException {
        inputStream.close();
        outputStream.close();
        clientSocket.close();
    }
}
