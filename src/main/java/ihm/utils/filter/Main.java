package ihm.utils.filter;

import ihm.controller.DiffTestController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/diffTest.fxml"));

        Parent parent = loader.load();

        Scene scene = new Scene(parent, 600, 436.0);
        stage.setScene(scene);
        stage.setTitle("Test different filtre");
        stage.show();
    }


    public static void main(String[] args) {
        nu.pattern.OpenCV.loadShared();
        launch(args);
    }
}
