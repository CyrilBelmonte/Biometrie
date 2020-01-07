package ihm.controller;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PasswordVerificationController implements Initializable {

    private int numberOfTry;

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
    void cancelButtonHandler(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void continueButtonHandler(ActionEvent event) throws IOException {
        // TODO TEST SI L'UTILISATEUR EST DANS LA BASE
        boolean goodAuthentication = true;
        if (goodAuthentication) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/BiometricVerification.fxml"));

            Parent parent = loader.load();

            Scene scene = new Scene(parent, 700, 277);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            numberOfTry++;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur mauvais mot de passe");
            alert.setHeaderText(null);
            alert.setContentText("le mot de passe est incorrecte , vous avez fait " + numberOfTry + " essai(s) ");
            alert.showAndWait();
            passwordLabel.setText("");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        numberOfTry = 0;
    }

    public void initUserName(String username) {
        usernameLabel.setText(username);
    }
}
