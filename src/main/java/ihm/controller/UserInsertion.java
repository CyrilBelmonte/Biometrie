package ihm.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import server.tools.AES;
import server.tools.Tools;
import smartcard.smartcardApi;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;


public class UserInsertion {
    private static final String REMOTE_HOST = "127.0.0.1";
    private static final int REMOTE_PORT = 4000;
    private static final String KEYSTORE_LOCATION = "C:/client_keystore.jks";
    private static final String KEYSTORE_PASSWORD = "password";

    private SSLSocket socket;
    private BufferedInputStream inputStream;
    private BufferedOutputStream outputStream;
    private String sessionKey = null;
    private boolean isCardInserted = false;
    private int remainingAttemps = 3;
    private CardTerminal terminal;
    private Card card;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab authTab;

    @FXML
    private Pane authPanel1;

    @FXML
    private Label authCardLabel;

    @FXML
    private ProgressIndicator authCardProgress;

    @FXML
    private Pane authPanel2;

    @FXML
    private PasswordField authPinField;

    @FXML
    private Button loginButton;

    @FXML
    private Tab userTab;

    @FXML
    private CheckBox adminCheckbox;

    @FXML
    private TextField firstNameField;

    @FXML
    private ImageView userCardImage;

    @FXML
    private Label userCardLabel;

    @FXML
    private ProgressIndicator userCardProgress;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField userPinField;

    @FXML
    private Button createUserButton;

    @FXML
    private TextField biometryField;

    @FXML
    void biometryFieldHandler(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select the photo of a user");
        File file = fileChooser.showOpenDialog(biometryField.getScene().getWindow());
        biometryField.setText(file.toString());
    }

    @FXML
    void createUserButtonHandler(ActionEvent event) {
        if (!isCardInserted) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Card error");
            alert.setHeaderText(null);
            alert.setContentText("A card is required for writing data.");
            alert.showAndWait();
            return;
        }

        if (biometryField.getLength() == 0 ||
            firstNameField.getLength() == 0 ||
            lastNameField.getLength() == 0 ||
            emailField.getLength() == 0 ||
            userPinField.getLength() == 0) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Some fields are empty.");
            alert.showAndWait();
            return;
        }

        if (userPinField.getLength() != 6) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect PIN code");
            alert.setHeaderText(null);
            alert.setContentText("The PIN code must contains 6 digits.");
            alert.showAndWait();
            return;
        }

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String userPinCode = userPinField.getText();
        String biometryPath = biometryField.getText();
        boolean isAdmin = adminCheckbox.isSelected();

        int x = Tools.getSeed();
        int y = Tools.getSeed();

        String userPinHash = Tools.hmacMD5(Tools.hmacMD5(userPinCode, String.valueOf(x)), String.valueOf(y));
        String biometryAESKey = AES.generateKey(128);
        String biometryData = AES.encrypt(biometryPath, biometryAESKey);

        // Opening the socket
        try {
            openSocket();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection");
            alert.setHeaderText(null);
            alert.setContentText("Unable to connect to the server.");
            alert.showAndWait();
            return;
        }

        // Creating the user
        String userID;

        try {
            send("CREATE;" + firstName + "," + lastName + "," + email + "," + isAdmin + "," + x + "," + y + "," + userPinHash + "," + biometryData + ";" + sessionKey);
            String[] reply = receiveAndPrepare(1024);
            close();

            if (reply.length != 3) {
                throw new Exception("The reply is incorrect.");

            } else if (reply[1].equals("ERROR") && reply[2].equals("UNKNOWN_SESSION")) {
                userTab.setDisable(true);
                tabPane.getSelectionModel().select(authTab);
                throw new Exception("The session key has expired:\n" + sessionKey);

            } else if (reply[1].equals("ERROR")) {
                throw new Exception("Error: " + reply[2]);
            }

            userID = reply[2];

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        // Flashing the card
        CardChannel channel = card.getBasicChannel();
        smartcardApi.createUserCard(channel, userID + ";" + firstName + " " + lastName, biometryAESKey, Integer.valueOf(userPinCode), terminal, card);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Operation successful");
        alert.setHeaderText(null);
        alert.setContentText("The user has been created and the card is ready!");
        alert.showAndWait();
    }

    @FXML
    void loginButtonHandler(ActionEvent event) {
        userTab.setDisable(true);

        if (authPinField.getLength() != 6) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Incorrect PIN code");
            alert.setHeaderText(null);
            alert.setContentText("The PIN code must contains 6 digits.");
            alert.showAndWait();
            return;
        }

        String enteredPinCode = authPinField.getText();

        CardChannel channel = card.getBasicChannel();

        if (remainingAttemps > 0) {
            try {
                smartcardApi.authCSC(channel, 1, Integer.valueOf(enteredPinCode));
                remainingAttemps = 3;

            } catch (smartcardApi.InvalidSecretCodeException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect PIN code");
                alert.setHeaderText(null);
                alert.setContentText("Enter a valid PIN code:\n" + --remainingAttemps + " attempts remaining");
                alert.showAndWait();
                return;

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect PIN code");
            alert.setHeaderText(null);
            alert.setContentText("The card was blocked after 3 attempts...");
            alert.showAndWait();
            return;
        }

        // Retrieve the id and username from the card
        int id;
        String userFullName;

        try {
            String data = smartcardApi.readUserData(channel, 1);
            String[] dataParts = data.split(";");
            id = Integer.valueOf(dataParts[0]);
            userFullName = dataParts[1];

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        try {
            openSocket();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection");
            alert.setHeaderText(null);
            alert.setContentText("Unable to connect to the server.");
            alert.showAndWait();
            return;
        }

        try {
            send("AUTHENTICATION;1;" + id);
            String[] reply = receiveAndPrepare(1024);

            if (reply.length != 3) {
                throw new Exception("The reply is incorrect.");

            } else if (reply[1].equals("ERROR") && reply[2].equals("UNKNOWN_USER")) {
                throw new Exception("The user doesn't exist.");

            } else if (reply[1].equals("ERROR")) {
                throw new Exception("Error: " + reply[2]);
            }

            String[] codes = reply[2].split(",");
            String x = codes[0];
            String y = codes[1];
            String g = codes[2];
            String z = Tools.hmacMD5(Tools.hmacMD5(Tools.hmacMD5(enteredPinCode, x), y), g);

            send("AUTHENTICATION;2;" + z);

            // Biometric data
            reply = receiveAndPrepare(4096);

            if (reply.length != 3) {
                throw new Exception("The reply is incorrect.");

            } else if (reply[1].equals("ERROR") && reply[2].equals("PASSWORD_ERROR")) {
                throw new Exception("The password is invalid.");

            } else if (reply[1].equals("ERROR")) {
                throw new Exception("Error: " + reply[2]);
            }

            send("AUTHENTICATION;3;CREATE_SESSION");
            reply = receiveAndPrepare(1024);

            if (reply.length != 3) {
                throw new Exception("The reply is incorrect.");

            } else if (reply[1].equals("ERROR")) {
                throw new Exception("Error: " + reply[2]);
            }

            sessionKey = reply[2];
            System.out.println("Session key: " + sessionKey);

            close();
            openSocket();

            send("GET;IS_ADMIN;" + sessionKey);
            reply = receiveAndPrepare(1024);

            if (reply.length != 3) {
                throw new Exception("The reply is incorrect.");

            } else if (reply[1].equals("ERROR")) {
                throw new Exception("Error: " + reply[2]);

            } else if (reply[2].equals("false")) {
                throw new Exception("You don't have the required privileges to create a user.");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Connection");
            alert.setHeaderText("Welcome " + userFullName + "!");
            alert.setContentText("You are connected!\nThe session will automatically expire after 5 minutes of inactivity.");
            alert.showAndWait();

            authPinField.clear();
            userTab.setDisable(false);
            tabPane.getSelectionModel().select(userTab);

            close();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        } finally {
            close();
        }
    }

    @FXML
    void initialize() {
        try {
            List<CardTerminal> availableTerminals = smartcardApi.getTerminals();
            terminal = availableTerminals.get(0);

        } catch (CardException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Terminal error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to connect to the terminal.");
            alert.showAndWait();
            System.exit(0);
        }

        WaitingForAuthCard waitingForAuthCard = new WaitingForAuthCard();
        waitingForAuthCard.start();
    }

    class WaitingForAuthCard extends Thread {
        private boolean isStopped = false;

        @Override
        public void run() {
            try {
                while (!isStopped) {
                    // Check if a card is inserted
                    try {
                        if (isCardInserted != terminal.isCardPresent()) {
                            isCardInserted = terminal.isCardPresent();

                            if (isCardInserted) {
                                Tools.printLogMessage("ihm_au", "A card has been inserted...");
                                card = terminal.connect("T=0");

                            } else {
                                Tools.printLogMessage("ihm_au", "A card has been removed...");
                                card = null;
                            }

                            remainingAttemps = 3;
                        }

                    } catch (CardException e) {
                        System.out.println(e.getMessage());
                        card = null;
                        isCardInserted = false;
                    }

                    if (isCardInserted) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                authCardLabel.setText("A card is inserted!");
                                authPanel2.setDisable(false);
                                authCardProgress.setDisable(true);

                                userCardLabel.setText("A card is inserted!");
                                userCardProgress.setVisible(false);
                                userCardImage.setVisible(true);
                            }
                        });

                    } else {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                authCardLabel.setText("Waiting for a card...");
                                authCardProgress.setDisable(false);
                                authPanel2.setDisable(true);
                                authPinField.clear();

                                userCardLabel.setText("Waiting for the user card...");
                                userCardImage.setVisible(false);
                                userCardProgress.setVisible(true);
                            }
                        });
                    }

                    Thread.sleep(1000);
                }

            } catch (InterruptedException e) {}
        }

        public void shutdown() {
            isStopped = true;
        }
    }

    private void openSocket() throws IOException {
        System.setProperty("javax.net.ssl.keyStore", KEYSTORE_LOCATION);
        System.setProperty("javax.net.ssl.keyStorePassword", KEYSTORE_PASSWORD);

        SocketFactory socketFactory = SSLSocketFactory.getDefault();
        socket = (SSLSocket) socketFactory.createSocket(REMOTE_HOST, REMOTE_PORT);
        socket.startHandshake();

        inputStream = new BufferedInputStream(socket.getInputStream());
        outputStream = new BufferedOutputStream(socket.getOutputStream());
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
        String[] reply = receive(bufferSize).split(";");
        Tools.printLogMessageErr("----->", "Payload: " + String.join(" ", reply));
        return reply;
    }

    private void close() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();

        } catch (Exception e) {}
    }
}
