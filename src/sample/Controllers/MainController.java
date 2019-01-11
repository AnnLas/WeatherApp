package sample.Controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Forecast.Request;
import sample.WeatherData.WeatherData;
import sample.WeatherData.WeatherStation;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

/**
 * Główny kontroler odpowiadający za wyświetlanie odpowiednich widoków w głównym oknie aplikacji
 */
public class MainController implements Observer {
    private WeatherStation weatherStation;
    private ChartsController chartsController;
    private DataTablesController dataTablesController;
    private StatisticsController statisticsController;
    private ForecastChartsController forecastChartsController;

    /**
     * Tworzy instancję klasy MainController
     */
    public MainController() {
        weatherStation = new WeatherStation();
        weatherStation.addObserver(this);
        showCurrentData(weatherStation.getWeatherData());
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
    private TextField town_search;

    @FXML
    private Button registration_button;

    @FXML
    private Button interrupt_button;

    @FXML
    private TextField request_frequency;

    @FXML
    private BorderPane main_pane;

    /**
     * Przerywa wysyłanie zapytań do serwera OpenWeather
     * Wyświetla okno z wyborem ścieżki do miejsca zapisu danych pogodowych.
     * Usuwa dane z pól tekstowych, tabel i wykresów.
     * Zapisuje dane pogodowe w wybranym miejscu.
     *
     * @param event - naciśnięcie przycisku
     */
    @FXML
    void inerruptRegistration(ActionEvent event) {
        weatherStation.interrupt();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage = new Stage();
        stage.setTitle("Choose directory");
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory == null) {

        } else {
            System.out.println(selectedDirectory.getAbsolutePath());
            weatherStation.getDataHolder().saveToJson(selectedDirectory.getAbsolutePath());

        }
        clearData();
        interrupt_button.setDisable(true);
    }

    /**
     * Rozpoczyna wysyłanie zapytań do serwera OpenWeather
     *
     * @param event - naciśnięcie przycisku
     */
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
                interrupt_button.setDisable(true);
                registration_button.setText("Stop");
            }
        } else {
            weatherStation.stop();
            town_search.setDisable(false);
            interrupt_button.setDisable(false);
            registration_button.setText("Start");
        }
    }

    /**
     * Umieszcza widok prognozy pogody w głównym panelu sceny.
     *
     * @param event - naciśnięcie przycisku
     */
    public void showForecast(MouseEvent event) {
        main_pane.getChildren().clear();
        Request request = new Request(town_search.getText());
        forecastChartsController = new ForecastChartsController(request);
        main_pane.setCenter(forecastChartsController);

    }

    /**
     * Umieszcza widok wykresów w głównym panelu sceny.
     *
     * @param mouseEvent - naciśnięcie przycisku
     */
    public void showCharts(MouseEvent mouseEvent) {
        main_pane.getChildren().clear();
        chartsController = new ChartsController(weatherStation);
        main_pane.setCenter(chartsController);


    }

    /**
     * Umieszcza widok statystyk pogodowych w głównym panelu sceny.
     *
     * @param mouseEvent - naciśnięcie przycisku
     */
    public void showStatistics(MouseEvent mouseEvent) {
        main_pane.getChildren().clear();
        statisticsController = new StatisticsController(weatherStation);
        main_pane.setCenter(statisticsController);


    }

    /**
     * Umieszcza widok tabel z danymi pogodowymi w głównym panelu sceny.
     *
     * @param mouseEvent - naciśnięcie przycisku
     */
    public void showDataTable(MouseEvent mouseEvent) {
        main_pane.getChildren().clear();
        dataTablesController = new DataTablesController(weatherStation);
        main_pane.setCenter(dataTablesController);

    }

    /**
     * Wyświetla okno z wyborem pliku typu json z danymi pogodowymi.
     * Dodaje dane do pól tekstowych, tabel i wykresów.
     *
     * @param event - nacisnięcie przycisku
     * @return dane pogodowe
     */
    @FXML
    WeatherData[] openFile(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        Stage stage = new Stage();
        stage.setTitle("Choose directory");
        File selectedDirectory = fileChooser.showOpenDialog(stage);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        WeatherData[] dataFromFile = null;
        if (selectedDirectory == null) {

        } else {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(selectedDirectory));
                dataFromFile = gson.fromJson(bufferedReader, WeatherData[].class);
                System.out.println(Arrays.toString(dataFromFile));
                weatherStation.getDataHolder().setWeatherDataSet(new ArrayList<WeatherData>(Arrays.asList(dataFromFile)));


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        return dataFromFile;
    }

    /**
     * Aktualizuje dane pogodowe wyświetlane w panelu boczne i przesyłane do pozostałych kontrolerów.
     * @param observable
     * @param o
     */
    @Override
    public void update(Observable observable, Object o) {
        weatherStation = (WeatherStation) observable;
        weatherStation.addObserver(this);
        System.out.println("results:" + weatherStation.getWeatherData().toString());
        showCurrentData(weatherStation.getWeatherData());
    }

    /**
     * Wyświetla bierzące dane pogodowe w panelu bocznym
     *
     * @param weather - dane pogodowe
     */
    private void showCurrentData(WeatherData weather) {
        if (weatherStation.getResponseCode() == 200 && weatherStation.getDataHolder().getHumidityValues().size() != 0) {
            temperature_text.setText((weather.getTemp()) + "C");
            humidity_text.setText((weather.getHumidity()) + "%");
            pressure_text.setText((weather.getPressure() + "HPa"));
            String imagePath = "";


            if (weather.getTemp() < 0) {
                if (weather.getHumidity() > 80) {
                    imagePath = "src/sample/images/snow.png";
                } else imagePath = "src/sample/images/cold.png";
            }
            if (weather.getTemp() >= 0) {
                if (weather.getHumidity() > 80) {
                    imagePath = "src/sample/images/rain.png";
                } else if (weather.getTemp() < 10) {
                    imagePath = "src/sample/images/cloudy.png";
                } else if (weather.getTemp() < 20) {
                    imagePath = "src/sample/images/midinglyhot.png";
                } else {
                    imagePath = "src/sample/images/hot.png";
                }
            }
            File file = new File(imagePath);
            Image image = new Image(file.toURI().toString());
            weather_conditions_image.setImage(image);
        }


    }

    private void clearData() {

        weatherStation.getDataHolder().clearData();
        if (chartsController != null) {
            chartsController.clearData();
        }
        if (statisticsController != null) {
            statisticsController.clearData();
        }
        if (dataTablesController != null) {
            dataTablesController.clearData();
        }

        temperature_text.setText("-");
        humidity_text.setText("-");
        pressure_text.setText("-");

    }

}
