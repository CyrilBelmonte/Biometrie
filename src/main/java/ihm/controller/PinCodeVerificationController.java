package ihm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

public class PinCodeVerificationController {

    private Stage previousStage;

    @FXML
    private Button fourButtonHandler;

    @FXML
    private Button validateButton;

    @FXML
    private Button oneButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button threeButton;

    @FXML
    private Button sevenButton;

    @FXML
    private Button twoButton;

    @FXML
    private Button eightButton;

    @FXML
    private Button sixButton;

    @FXML
    private Button nineButton;

    @FXML
    private Label codeLabel;

    @FXML
    private Button zeroButton;

    @FXML
    private Button fiveButton;

    @FXML
    void oneButtonHandler(ActionEvent event) {
        if (this.codeLabel.getText().length() >= 4) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attention au PIN");
            alert.setHeaderText(null);
            alert.setContentText("Le pin ne contient que 4 caracteres");
            alert.showAndWait();
        } else {
            this.codeLabel.setText(codeLabel.getText() + "1");
        }
    }

    @FXML
    void twoButtonHandler(ActionEvent event) {
        if (this.codeLabel.getText().length() >= 4) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attention au PIN");
            alert.setHeaderText(null);
            alert.setContentText("Le pin ne contient que 4 caracteres");
            alert.showAndWait();
        } else {
            this.codeLabel.setText(codeLabel.getText() + "2");
        }
    }

    @FXML
    void threeButton(ActionEvent event) {
        if (this.codeLabel.getText().length() >= 4) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attention au PIN");
            alert.setHeaderText(null);
            alert.setContentText("Le pin ne contient que 4 caracteres");
            alert.showAndWait();
        } else {
            this.codeLabel.setText(codeLabel.getText() + "3");
        }
    }

    @FXML
    void fourButtonHandler(ActionEvent event) {
        if (this.codeLabel.getText().length() >= 4) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attention au PIN");
            alert.setHeaderText(null);
            alert.setContentText("Le pin ne contient que 4 caracteres");
            alert.showAndWait();
        } else {
            this.codeLabel.setText(codeLabel.getText() + "4");
        }
    }

    @FXML
    void fiveButtonHandler(ActionEvent event) {
        if (this.codeLabel.getText().length() >= 4) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attention au PIN");
            alert.setHeaderText(null);
            alert.setContentText("Le pin ne contient que 4 caracteres");
            alert.showAndWait();
        } else {
            this.codeLabel.setText(codeLabel.getText() + "5");
        }
    }

    @FXML
    void sixButtonHandler(ActionEvent event) {
        if (this.codeLabel.getText().length() >= 4) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attention au PIN");
            alert.setHeaderText(null);
            alert.setContentText("Le pin ne contient que 4 caracteres");
            alert.showAndWait();
        } else {
            this.codeLabel.setText(codeLabel.getText() + "6");
        }
    }


    @FXML
    void sevenButtonHandler(ActionEvent event) {
        if (this.codeLabel.getText().length() >= 4) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attention au PIN");
            alert.setHeaderText(null);
            alert.setContentText("Le pin ne contient que 4 caracteres");
            alert.showAndWait();
        } else {
            this.codeLabel.setText(codeLabel.getText() + "7");
        }
    }

    @FXML
    void eightButtonHandler(ActionEvent event) {
        if (this.codeLabel.getText().length() >= 4) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attention au PIN");
            alert.setHeaderText(null);
            alert.setContentText("Le pin ne contient que 4 caracteres");
            alert.showAndWait();
        } else {
            this.codeLabel.setText(codeLabel.getText() + "8");
        }
    }

    @FXML
    void nineButtonHandler(ActionEvent event) {
        if (this.codeLabel.getText().length() >= 4) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attention au PIN");
            alert.setHeaderText(null);
            alert.setContentText("Le pin ne contient que 4 caracteres");
            alert.showAndWait();
        } else {
            this.codeLabel.setText(codeLabel.getText() + "9");
        }
    }

    @FXML
    void zeroButtonHandler(ActionEvent event) {
        if (this.codeLabel.getText().length() >= 4) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attention au PIN");
            alert.setHeaderText(null);
            alert.setContentText("Le pin ne contient que 4 caracteres");
            alert.showAndWait();
        } else {
            this.codeLabel.setText(codeLabel.getText() + "0");
        }
    }

    @FXML
    void cancelButtonHandler(ActionEvent event) {
        if (!this.codeLabel.getText().isEmpty()) {
            this.codeLabel.setText(this.codeLabel.getText().substring(0, this.codeLabel.getText().length() - 1));
        }
    }

    @FXML
    void closeButtonHandler(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void validateButton(ActionEvent event) {
        // TODO FONCTION SI LE CLIEN EST AUTHENTIFIÉ
        Stage primaryStage = new Stage();

        try {
            FXMLLoader loader = new FXMLLoader();
            final URL url = this.getClass().getResource("/fxml/PasswordVerification.fxml");
            final FXMLLoader fxmlLoader = new FXMLLoader(url);
            // Chargement du FXML.
            final AnchorPane root = (AnchorPane) fxmlLoader.load();
            // Création de la scène.
            final Scene scene = new Scene(root, 318, 362);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
        } catch (IOException ex) {
            System.err.println("Erreur au chargement: " + ex);
        }
        primaryStage.setTitle("Login with password");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }

    public void getDataFromPreviousStage(Stage windowStage){
        windowStage.close();
    }
}

