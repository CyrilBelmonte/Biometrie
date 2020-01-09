package ihm.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class UserInsertion {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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
    private Tab userTab;

    @FXML
    private CheckBox adminCheckbox;

    @FXML
    private TextField firstNameField;

    @FXML
    private ImageView userCardImage;

    @FXML
    private Label userCardLabel;

    @FXML
    private ProgressIndicator userCardProgress;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField userPinField;

    @FXML
    private Button createUserButton;

    @FXML
    private TextField biometryField;

    @FXML
    void createUserButtonHandler(ActionEvent event) {

    }

    @FXML
    void loginButtonHandler(ActionEvent event) {

    }

    @FXML
    void initialize() {
        // Nothing
    }
}
