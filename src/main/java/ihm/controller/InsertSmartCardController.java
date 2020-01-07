package ihm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InsertSmartCardController {

    private Stage thisStage;

    private Stage previousStage;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button closeButton;

    @FXML
    private Button okButton;

    @FXML
    void continueHandler(ActionEvent event) throws IOException {
        previousStage.hide();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/PinCodeVerification.fxml"));
            Parent parent = loader.load();

            Scene scene = new Scene(parent);

            PinCodeVerificationController controller = loader.getController();

            Stage windowsStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            controller.getDataFromPreviousStage(windowsStage);
            windowsStage.setScene(scene);
            windowsStage.initStyle(StageStyle.UNDECORATED);
            windowsStage.show();
        } catch (IOException e) {
            System.err.println("IOException " + e);
        }


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

    public void getDataFromPreviousStage(Stage previousStage) {
        this.previousStage = previousStage;
    }
}
