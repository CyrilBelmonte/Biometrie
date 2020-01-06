package ihm.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class InsertSmartCardController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button closeButton;

    @FXML
    private Button okButton;

    @FXML
    void continueHandler(ActionEvent event) {

        Stage primaryStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader();
            final URL url = this.getClass().getResource("/fxml/PinCodeVerification.fxml");
            final FXMLLoader fxmlLoader = new FXMLLoader(url);
            // Chargement du FXML.
            final AnchorPane root = (AnchorPane) fxmlLoader.load();
            // Création de la scène.
            final Scene scene = new Scene(root, 261, 374.0);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
        } catch (IOException ex) {
            System.err.println("Erreur au chargement: " + ex);
        }
        primaryStage.setTitle("Test FXML");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();

    }

    @FXML
    void closeHandler(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void initialize() {
        assert closeButton != null : "fx:id=\"closeButton\" was not injected: check your FXML file 'InsertSmartCard.fxml'.";
        assert okButton != null : "fx:id=\"okButton\" was not injected: check your FXML file 'InsertSmartCard.fxml'.";

    }
}
