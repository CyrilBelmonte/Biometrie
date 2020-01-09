package ihm.launchers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class MainInscription extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/UserInsertion.fxml"));

        Parent parent = loader.load();

        Scene scene = new Scene(parent, 390, 500);
        stage.setScene(scene);
        stage.setTitle("Administration center");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        nu.pattern.OpenCV.loadShared();
        launch(args);
    }
}
