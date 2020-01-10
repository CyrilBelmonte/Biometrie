package ihm.launchers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/InsertSmartCard.fxml"));

        Parent parent = loader.load();

        Scene scene = new Scene(parent, 300, 300);
        stage.setScene(scene);
        stage.setTitle("Insert smart card");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }


    public static void main(String[] args) {
        nu.pattern.OpenCV.loadShared();
        launch(args);
    }
}
