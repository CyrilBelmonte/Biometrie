package ihm.controller;



import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PasswordVerificationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label usernameLabel;

    @FXML
    private Button continueButton;

    @FXML
    private TextField passwordLabel;

    @FXML
    void continueButtonHandler(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert usernameLabel != null : "fx:id=\"usernameLabel\" was not injected: check your FXML file 'PasswordVerification.fxml'.";
        assert continueButton != null : "fx:id=\"continueButton\" was not injected: check your FXML file 'PasswordVerification.fxml'.";
        assert passwordLabel != null : "fx:id=\"passwordLabel\" was not injected: check your FXML file 'PasswordVerification.fxml'.";

    }
}
