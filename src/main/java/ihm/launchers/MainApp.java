package ihm.launchers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            final URL url = this.getClass().getResource("/fxml/InsertSmartCard.fxml");
            final FXMLLoader fxmlLoader = new FXMLLoader(url);
            // Chargement du FXML.
            final AnchorPane root = (AnchorPane) fxmlLoader.load();
            // Création de la scène.
            final Scene scene = new Scene(root, 300, 300);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
        } catch (IOException ex) {
            System.err.println("Erreur au chargement: " + ex);
        }
        primaryStage.setTitle("Test FXML");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }
}
