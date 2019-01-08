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

public class ChartsController extends AnchorPane implements Initializable, Observer  {
    private WeatherStation weatherStation;
    XYChart.Series<String, Double> temp_series;
    XYChart.Series<String, Double> humidity_series;
    XYChart.Series<String, Double> pressure_series;
    private String townName;

    public ChartsController(WeatherStation weatherStation)
    {
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
        weatherStation= new WeatherStation();

    }

    @Override
    public void update(Observable observable, Object o) {
        weatherStation = (WeatherStation) observable;
        WeatherData w = weatherStation.getWeatherData();
        addDataToChart(w);
    }

    public void showData(DataHolder dataHolder) {

        townName = "";

        for (WeatherData w : dataHolder.getWeatherDataSet()) {
           addDataToChart(w);
        }



    }

    private void addDataToChart(WeatherData weatherData){
        Platform.runLater(() -> {
            if (townName.equals(weatherData.getTown())) {
                temp_series.getData().add(new XYChart.Data<>(weatherData.getRegistrationTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")), weatherData.getTemp()));
                humidity_series.getData().add(new XYChart.Data<>(weatherData.getRegistrationTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")), weatherData.getHumidity()));
                pressure_series.getData().add(new XYChart.Data<>(weatherData.getRegistrationTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")), weatherData.getPressure()));
            }
            else {
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

}
