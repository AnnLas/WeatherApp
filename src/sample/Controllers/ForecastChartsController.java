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
import sample.Forecast.Request;
import sample.WeatherData.WeatherData;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * ForecastController jest klasą odpowiedzialną za wyświetlanie prognozy pogody w wybranym mieście
 * (temperatura, ciśnienie, wilgotność) na wykresach w panelu AnchorPane.
 */
public class ForecastChartsController extends AnchorPane implements Initializable {
    private Request request;
    private String townName;
    XYChart.Series<String, Double> temp_series;
    XYChart.Series<String, Double> humidity_series;
    XYChart.Series<String, Double> pressure_series;

    /**
     * Tworzy instancję klasy ForecastController.
     * Wyświetla fragment sceny
     * @param forecastRequest - zapytanie o prognozę pogody
     */
    public ForecastChartsController(Request forecastRequest) {

        this.request = forecastRequest;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/charts.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            showData(forecastRequest);

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


    }


    private void showData(Request request) {

        townName = "";

        for (WeatherData w : request.getForecast()) {
            addDataToChart(w);
        }

    }

    private void addDataToChart(WeatherData weatherData) {
        humidity_chart.setTitle(townName + "forecast");
        pressure_chart.setTitle(townName + " forecast");
        temperature_chart.setTitle(townName + " forecast");
        Platform.runLater(() -> {
            if (townName.equals(weatherData.getTown())) {
                temp_series.getData().add(new XYChart.Data<>(weatherData.getForecastTimeAndDate(), weatherData.getTemp()));
                humidity_series.getData().add(new XYChart.Data<>(weatherData.getForecastTimeAndDate(), weatherData.getHumidity()));
                pressure_series.getData().add(new XYChart.Data<>(weatherData.getForecastTimeAndDate(), weatherData.getPressure()));
            } else {
                temp_series = new XYChart.Series<>();
                humidity_series = new XYChart.Series<>();
                pressure_series = new XYChart.Series<>();
                townName = weatherData.getTown();
                temp_series.getData().add(new XYChart.Data<>(weatherData.getForecastTimeAndDate(), weatherData.getTemp()));
                humidity_series.getData().add(new XYChart.Data<>(weatherData.getForecastTimeAndDate(), weatherData.getHumidity()));
                pressure_series.getData().add(new XYChart.Data<>(weatherData.getForecastTimeAndDate(), weatherData.getPressure()));
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
