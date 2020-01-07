package ihm.launchers;

import ihm.controller.InsertSmartCardController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/InsertSmartCard.fxml"));
            Parent parent = loader.load();

            InsertSmartCardController controller = loader.getController();


            Scene scene = new Scene(parent, 300, 300);

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Insert smart card");
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.show();
            primaryStage.hide();
        } catch (IOException ex) {

            System.err.println("Erreur au chargement: " + ex);
        }
    }
}
