package ihm.controller;

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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class PinCodeVerificationController implements Initializable {

    private int numberOfTry;
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
    void validateButton(ActionEvent event) throws IOException {
        // TODO FONCTION SI LE CLIENT EST AUTHENTIFIÉ
        boolean goodCodeOnCard = true;
        String username = "TMP";
        if (goodCodeOnCard) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/PasswordVerification.fxml"));

            Parent parent = loader.load();

            PasswordVerificationController controller = loader.getController();
            controller.initUserName(username);


            Scene scene = new Scene(parent, 318, 362);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            this.codeLabel.setText("");
            numberOfTry++;
            if (numberOfTry < 3) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention au PIN");
                alert.setHeaderText(null);
                alert.setContentText("Pin incorrecte, il reste " + (3 - numberOfTry) + " essai");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Attention au PIN");
                alert.setHeaderText(null);
                alert.setContentText("Pin incorrecte, vous avez déjà fait " + numberOfTry + " essai votre carte est détruite \n Fin du programme d'authentification");
                alert.showAndWait();
                System.exit(-1);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        numberOfTry = 0;
    }


}

