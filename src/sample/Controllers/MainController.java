package sample.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import sample.WeatherData.DataHolder;
import sample.WeatherData.WeatherData;
import sample.WeatherData.WeatherStation;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class MainController implements Observer {
    private WeatherStation weatherStation;
    private Observable weatherData;
    private DataHolder dataHolder;


    public MainController() {
     weatherStation = new WeatherStation();
     weatherStation.addObserver(this);
    }

    @FXML
    private Text temperature_text;

    @FXML
    private Text humidity_text;

    @FXML
    private Text pressure_text;

    @FXML
    private ImageView weather_conditions_image;

    @FXML
    private HBox main_box;

    @FXML
    private TextField town_search;

    @FXML
    private Button registration_button;

    @FXML
    private Button interrupt_button;

    @FXML
    private TextField request_frequency;

    @FXML
    private BorderPane main_pane;

    @FXML
    void inerruptRegistration(ActionEvent event) {
    }

    @FXML
    void startRegistration(ActionEvent event) {
        if (weatherStation.getTown() == null || !weatherStation.getTown().equals(town_search.getText())) {
            weatherStation.setTown(town_search.getText());

        }
        if (registration_button.getText().equals("Start")) {

            if (Double.valueOf(request_frequency.getText()) < 1) {
                interrupt_button.setDisable(true);
                throw new IllegalArgumentException("Interval cannot be smaller than 1s");


            } else {

                weatherStation.setInterval(Integer.valueOf(request_frequency.getText()) * 1000);
                weatherStation.start();
                town_search.setDisable(true);
                interrupt_button.setDisable(false);
                registration_button.setText("Stop");
            }
        } else {
            weatherStation.stop();
            town_search.setDisable(false);
            registration_button.setText("Start");
        }
    }

    public void showCharts(MouseEvent mouseEvent) {
           main_pane.getChildren().clear();
           ChartsController chartsController = new ChartsController(weatherStation);
           main_pane.setCenter(chartsController);

    }

    public void showStatistics(MouseEvent mouseEvent) {
        main_pane.getChildren().clear();
        StatisticsController statisticsController = new StatisticsController(weatherStation);
        main_pane.setCenter(statisticsController);

         
    }

    public void showDataTable(MouseEvent mouseEvent) {
        main_pane.getChildren().clear();
        DataTablesController dataTablesController = new DataTablesController(weatherStation);
        main_pane.setCenter(dataTablesController);
    }


    public void openFile(ActionEvent actionEvent) {
    }

    @Override
    public void update(Observable observable, Object o) {
        weatherStation = (WeatherStation) observable;
        System.out.println("results:"+weatherStation.getWeatherData().toString());

    }
}
