package sample.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import sample.WeatherData.DataHolder;
import sample.WeatherData.WeatherData;
import sample.WeatherData.WeatherStation;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

/**
 * ChartsController jest klasą odpowiedzialną za wyświetlanie danych pogodowych(temperatura, ciśnienie, wilgotność)
 * na wykresach w panelu AnchorPane. Dane te są na bieżąco aktualizowane i wyświetlane w stosunku do zmian w obiekcie
 * klasy WeatherStation.
 */
public class ChartsController extends AnchorPane implements Initializable, Observer {
    private WeatherStation weatherStation;
    private XYChart.Series<String, Double> temp_series;
    private XYChart.Series<String, Double> humidity_series;
    private XYChart.Series<String, Double> pressure_series;
    private String townName;

    /**
     * Tworzy instancję klasy ChartsController.
     * Wyświetla fragment sceny.
     * @param weatherStation - obiekt klasy WeatherStation
     */
    public ChartsController(WeatherStation weatherStation) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/charts.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.weatherStation = weatherStation;
        weatherStation.addObserver(this);


        try {
            fxmlLoader.load();
            showData(weatherStation.getDataHolder());

        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private TabPane charts;

    @FXML
    private LineChart<String, Double> temperature_chart;

    @FXML
    private ScatterChart<String, Double> humidity_chart;

    @FXML
    private LineChart<String, Double> pressure_chart;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        weatherStation = new WeatherStation();

    }

    /**
     * Aktualizuje dane poodowe na wykresie.
     * @param observable
     * @param o
     */
    @Override
    public void update(Observable observable, Object o) {
        weatherStation = (WeatherStation) observable;
        WeatherData w = weatherStation.getWeatherData();
        weatherStation.addObserver(this);
        addDataToChart(w);
    }

    private void showData(DataHolder dataHolder) {

        townName = "";

        for (WeatherData w : dataHolder.getWeatherDataSet()) {
            addDataToChart(w);
        }


    }

    private void addDataToChart(WeatherData weatherData) {
        Platform.runLater(() -> {
            if (townName.equals(weatherData.getTown())) {
                temp_series.getData().add(new XYChart.Data<>(weatherData.getRegistrationTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")), weatherData.getTemp()));
                humidity_series.getData().add(new XYChart.Data<>(weatherData.getRegistrationTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")), weatherData.getHumidity()));
                pressure_series.getData().add(new XYChart.Data<>(weatherData.getRegistrationTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")), weatherData.getPressure()));
            } else {
                temp_series = new XYChart.Series<>();
                humidity_series = new XYChart.Series<>();
                pressure_series = new XYChart.Series<>();
                townName = weatherData.getTown();
                temp_series.getData().add(new XYChart.Data<>(weatherData.getRegistrationTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")), weatherData.getTemp()));
                humidity_series.getData().add(new XYChart.Data<>(weatherData.getRegistrationTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")), weatherData.getHumidity()));
                pressure_series.getData().add(new XYChart.Data<>(weatherData.getRegistrationTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")), weatherData.getPressure()));
                temp_series.setName(townName);
                humidity_series.setName(townName);
                pressure_series.setName(townName);
                temperature_chart.getData().addAll(temp_series);
                humidity_chart.getData().addAll(humidity_series);
                pressure_chart.getData().addAll(pressure_series);
            }
        });

    }

    /**
     * Usuwa dane pogodowe z wykresów i serii danych
     */
    public void clearData() {
        humidity_series.getData().clear();
        temp_series.getData().clear();
        pressure_series.getData().clear();
        humidity_chart.getData().clear();
        temperature_chart.getData().clear();
        pressure_chart.getData().clear();

    }


}
