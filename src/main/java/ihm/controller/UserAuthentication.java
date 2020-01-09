package ihm.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ihm.utils.Utils;
import ihm.utils.filter.Canny;
import ihm.utils.filter.Laplacien;
import ihm.utils.filter.Prewitt;
import ihm.utils.filter.Sobel;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import server.tools.AES;
import server.tools.Tools;
import smartcard.smartcardApi;

import javax.imageio.ImageIO;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;

import static org.opencv.videoio.Videoio.CV_CAP_PROP_FRAME_HEIGHT;
import static org.opencv.videoio.Videoio.CV_CAP_PROP_FRAME_WIDTH;


public class UserAuthentication {
    private static final String REMOTE_HOST = "127.0.0.1";
    private static final int REMOTE_PORT = 4000;
    private static final String KEYSTORE_LOCATION = "C:/client_keystore.jks";
    private static final String KEYSTORE_PASSWORD = "password";

    private SSLSocket socket;
    private BufferedInputStream inputStream;
    private BufferedOutputStream outputStream;
    private String sessionKey = null;
    private String biometryData = null;
    private boolean isCardInserted = false;
    private CardTerminal terminal;
    private Card card;

    private Image image;
    private Mat frameTmp;
    private static int LIMIT = 90;
    private ScheduledExecutorService timer;
    private VideoCapture capture = new VideoCapture();
    private boolean cameraActive = false;
    private static int cameraId = 0;

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
    private Tab biometryAuthTab;

    @FXML
    private ImageView cameraImage;

    @FXML
    private Button captureButton;

    @FXML
    private Tab accountTab;

    @FXML
    private Label sessionKeyLabel;

    @FXML
    void captureButtonHandler(ActionEvent event) {
        saveToFile(image);

        String fileIn = "src\\resources\\tmp\\pictures.png";
        String fileOut = "src\\resources\\tmp\\";


        Canny canny = new Canny(LIMIT);
        canny.filterImg(fileIn, fileOut);


        Sobel sobel = new Sobel(LIMIT);
        sobel.filterImg(fileIn, fileOut);


        Laplacien laplacien = new Laplacien(LIMIT);
        laplacien.filterImg(fileIn, fileOut);

        Prewitt prewitt = new Prewitt(LIMIT);
        prewitt.filterImg(fileIn, fileOut);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/DiffTest.fxml"));

        Parent parent = null;

        try {
            parent = loader.load();

        } catch (IOException e) {}

        Scene scene = new Scene(parent, 600, 436);
        Stage filter = new Stage();
        filter.setScene(scene);
        filter.show();

        // TODO: REPLACE THE VARIABLE "biometryOK"
        boolean biometryOK = true;

        if (biometryOK) {
            stopAcquisition();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Connection");
            alert.setHeaderText(null);
            alert.setContentText("You are connected!\nThe session will automatically expire after 5 minutes of inactivity.");
            alert.showAndWait();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection");
            alert.setHeaderText(null);
            alert.setContentText("Unable to recognize you. Retry please.");
            alert.showAndWait();
            return;
        }

        try {
            send("AUTHENTICATION;3;CREATE_SESSION");
            String[] reply = receive(1024).split(";");

            if (reply.length != 3) {
                throw new Exception("The reply is incorrect.");

            } else if (reply[1].equals("ERROR")) {
                throw new Exception("Error: " + reply[2]);
            }

            sessionKey = reply[2];
            System.out.println("Session key: " + sessionKey);

            close();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        accountTab.setDisable(false);
        tabPane.getSelectionModel().select(accountTab);
        sessionKeyLabel.setText("Session key: " + sessionKey);
    }

    @FXML
    void loginButtonHandler(ActionEvent event) {
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
        int remainingAttemps = 3;

        if (remainingAttemps > 0) {
            try {
                smartcardApi.authCSC(channel, 1, Integer.valueOf(enteredPinCode));

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
        String biometryKey;

        try {
            String data = smartcardApi.readUserData(channel, 1);
            String[] dataParts = data.split(";");
            id = Integer.valueOf(dataParts[0]);
            userFullName = dataParts[1];

            smartcardApi.authCSC(channel, 2, Integer.valueOf(enteredPinCode));
            biometryKey = smartcardApi.readUserData(channel, 2);

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

            String biometryDataAES = reply[2];
            biometryData = AES.decrypt(biometryDataAES, biometryKey);

            if (biometryData == null) {
                throw new Exception("The AES key stored in the card is invalid.");
            }

            System.out.println("Biometry data: " + biometryData);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Connection");
            alert.setHeaderText("Welcome " + userFullName + "!");
            alert.setContentText("Confirm your identity by scanning your face!");
            alert.showAndWait();

            biometryAuthTab.setDisable(false);
            tabPane.getSelectionModel().select(biometryAuthTab);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            close();
        }

        capture.set(CV_CAP_PROP_FRAME_WIDTH, 1920);
        capture.set(CV_CAP_PROP_FRAME_HEIGHT, 1080);

        if (!this.cameraActive) {
            // start the video capture
            this.capture.open(cameraId);

            // is the video stream available?
            if (this.capture.isOpened()) {
                this.cameraActive = true;

                // grab a frame every 33 ms (30 frames/sec)
                Runnable frameGrabber = new Runnable() {

                    @Override
                    public void run() {
                        // effectively grab and process a single frame
                        Mat frame = grabFrame();
                        // convert and show the frame
                        Image imageToShow = Utils.mat2Image(frame);
                        frameTmp = frame;
                        image = imageToShow;
                        updateImageView(cameraImage, imageToShow);
                    }
                };

                this.timer = Executors.newSingleThreadScheduledExecutor();
                this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

                // update the button content
                this.captureButton.setText("Capture");
            } else {
                // log the error
                System.err.println("Impossible to open the camera connection...");
            }
        } else {
            // the camera is not active at this point
            this.cameraActive = false;
            // update again the button content

            // stop the timer
            this.stopAcquisition();
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
                                card = terminal.connect("T=0");

                            } else {
                                card = null;
                            }
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

                                biometryAuthTab.setDisable(true);
                                accountTab.setDisable(true);
                                tabPane.getSelectionModel().select(authTab);
                            }
                        });

                        stopAcquisition();
                        close();
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
        return receive(bufferSize).split(";");
    }

    private void close() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();

        } catch (Exception e) {}
    }

    private Mat grabFrame() {
        // init everything
        Mat frame = new Mat();

        // check if the capture is open
        if (this.capture.isOpened()) {
            try {
                // read the current frame
                this.capture.read(frame);

                // if the frame is not empty, process it
                if (!frame.empty()) {

                }

            } catch (Exception e) {
                // log the error
                System.err.println("Exception during the image elaboration: " + e);
            }
        }

        return frame;
    }

    /**
     * Stop the acquisition from the camera and release all the resources
     */
    private void stopAcquisition() {
        if (this.timer != null && !this.timer.isShutdown()) {
            try {
                // stop the timer
                this.timer.shutdown();
                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                // log any exception
                System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
            }
        }

        if (this.capture.isOpened()) {
            // release the camera
            this.capture.release();
        }

        this.cameraActive = false;
    }

    private void updateImageView(ImageView view, Image image) {
        Utils.onFXThread(view.imageProperty(), image);
    }

    /**
     * On application close, stop the acquisition from the camera
     */
    protected void setClosed() {
        this.stopAcquisition();
    }

    public static void saveToFile(Image image) {
        File outputFile = new File("src\\resources\\tmp\\pictures.png");
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        try {
            ImageIO.write(bImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
