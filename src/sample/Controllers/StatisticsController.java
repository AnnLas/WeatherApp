package sample.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import sample.WeatherData.DataHolder;
import sample.WeatherData.WeatherStation;

import java.io.IOException;
import java.net.URL;
import java.util.*;


/**
 *StaticticsController jest klasą odpowiedzialną za obliczanie i wyświetlanie statystyk pogodowych(średnia, odchylenie standardowe,
 * wartoś maksymalna, wartość minimalna) w panelu AnchorPane. Dane te są na bieżąco aktualizowane i wyświetlane w stosunku do
 * zmian w obiekcie klasy WeatherStation.
 */
public class StatisticsController extends AnchorPane implements Initializable, Observer {
    private WeatherStation weatherStation;
    private DataHolder dataHolder;

    /**
     * Tworzy instancję klasy StatisticsController.
     * Wyświetla fragment sceny.
     * @param weatherStation - obiekt klasy WeatherStation
     */
    public StatisticsController (WeatherStation weatherStation)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/statistics.fxml"));
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

    private void showData(DataHolder dataHolder) {
    }

    @FXML
    private Text temperature_stats;

    @FXML
    private Text humidity_stats;

    @FXML
    private Text pressure_stats;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dataHolder =weatherStation.getDataHolder();
        displayData();
    }

    /**
     * Aktualizuje dane pogodowe i statyski.
     * @param observable
     * @param o
     */
    @Override
    public void update(Observable observable, Object o) {
        weatherStation = (WeatherStation) observable;
        weatherStation.addObserver(this);
        dataHolder = weatherStation.getDataHolder();
        displayData();

    }


    /**
     * Liczy statystyki dla podanej listy danych.
     * @param arrayList - lista danych
     * @return tekst z obliczonymi statystykami
     */
    private String calcStats(ArrayList <Double> arrayList) {
        double sumMean = 0;
        double sumStd = 0;
        double min = 3000;
        double max = -3000;
        for (double value : arrayList) {
            sumMean += value;
            if (value > max) {
                max = value;
            }
            if (value < min) {
                min = value;
            }
        }
        double average = sumMean / (double) arrayList.size();
        for (double value : arrayList) {
            sumStd += Math.pow((value - average), 2);
        }
        double standardDeviation = sumStd / (double) arrayList.size();
        return String.format("%nAverage: %.2f%nNumber of records: %d%nMin: %.2f%nMax: %.2f%nStandard deviation: %.2f%n", average, arrayList.size(), min, max, standardDeviation);
    }

    private void displayData(){
        Platform.runLater(()->{
         pressure_stats.setText(calcStats(dataHolder.getPressureValues()));
         temperature_stats.setText(calcStats(dataHolder.getTemperatureValues()));
         humidity_stats.setText(calcStats(dataHolder.getHumidityValues()));
        });
    }
    /**
     * Usuwa obliczone statystyki z pól tekstowych.
     */
    public void clearData(){
        pressure_stats.setText("-");
        temperature_stats.setText("-");
        humidity_stats.setText("-");
        weatherStation.getDataHolder().clearData();
    }



}
