package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Klasa służąca do inicjalizacji głównej sceny aplikacji.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/resources/main.fxml"));
        primaryStage.setTitle("Weather App");
        primaryStage.setScene(new Scene(root, 1300, 1275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
