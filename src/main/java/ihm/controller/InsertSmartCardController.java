package ihm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class InsertSmartCardController {

    private Stage stage;

    @FXML
    private Button closeButton;
    @FXML
    private Button okButton;

    @FXML
    void continueHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/PinCodeVerification.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent, 261, 374);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    void closeHandler(ActionEvent event) {
        System.exit(0);
    }


}
