package ihm.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class DiffTestController implements Initializable {

    @FXML
    private ImageView img1;

    @FXML
    private ImageView img2;

    @FXML
    private ImageView img3;

    @FXML
    private ImageView img4;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("src\\resources\\tmp\\Canny.png");
        Image image = new Image(file.toURI().toString());
        img1.setImage(image);

        File file1 = new File("src\\resources\\tmp\\Prewitt.png");
        Image image1 = new Image(file1.toURI().toString());
        img2.setImage(image1);

        File file3 = new File("src\\resources\\tmp\\Sobel.png");
        Image image3 = new Image(file3.toURI().toString());
        img3.setImage(image3);

        File file4 = new File("src\\resources\\tmp\\laplacien.png");
        Image image4 = new Image(file4.toURI().toString());
        img4.setImage(image4);

    }
}
