package ihm.controller;


import ihm.utils.Utils;
import ihm.utils.filter.Canny;
import ihm.utils.filter.Laplacien;
import ihm.utils.filter.Prewitt;
import ihm.utils.filter.Sobel;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.opencv.videoio.Videoio.CV_CAP_PROP_FRAME_HEIGHT;
import static org.opencv.videoio.Videoio.CV_CAP_PROP_FRAME_WIDTH;


public class BiometricVerificationController implements Initializable {

    private Stage stage;

    private Image image;
    private Mat frameTmp;
    @FXML
    private ImageView cameraImage;

    private ScheduledExecutorService timer;

    private VideoCapture capture = new VideoCapture();

    private boolean cameraActive = false;

    private static int cameraId = 1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
                this.button.setText("Stop Camera");
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
    private Button button;

    @FXML
    private void startCamera(ActionEvent event) throws IOException, InterruptedException {

        saveToFile(image);

        String filePath = "src\\resources\\tmp\\pictures.png";


        Canny canny = new Canny();
        canny.filtre(filePath);

        System.out.println("test 1 ");

        Sobel sobel = new Sobel();
        sobel.filtre(filePath);

        System.out.println("test 2 ");

        Laplacien laplacien = new Laplacien();
        laplacien.filtre(filePath);

        System.out.println("test 3 ");
        Prewitt prewitt = new Prewitt();
        prewitt.filtre(filePath, "src\resources\tmp");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/DiffTest.fxml"));

        Parent parent = loader.load();

        Scene scene = new Scene(parent, 600, 436);
        Stage filter = new Stage();
        filter.setScene(scene);
        filter.show();
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
