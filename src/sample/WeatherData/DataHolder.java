package sample.WeatherData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Służy do przechowywania danych pogodowych.
 */
public class DataHolder {
    private ArrayList<WeatherData> weatherDataSet = new ArrayList<>();
    private ArrayList<Double> temperatureValues= new ArrayList<>();
    private ArrayList<Double> pressureValues= new ArrayList<>();
    private ArrayList<Double> humidityValues= new ArrayList<>();

    /**
     * Metoda zwracająca listę rejestrów pogodowych
     * @return weatherDataSet - lista rejestrów pogodowych
     */
    public ArrayList<WeatherData> getWeatherDataSet() {
        return weatherDataSet;
    }

    /**
     * Metoda dodająca listę rejestrów pogodowych.
     * @param weatherDataSet - lista rejestrów pogodowych
     */
    public void setWeatherDataSet(ArrayList<WeatherData> weatherDataSet) {
        clearData();
        this.weatherDataSet = weatherDataSet;
        for (WeatherData w:weatherDataSet) {
            temperatureValues.add(w.getTemp());
            pressureValues.add(w.getPressure());
            humidityValues.add(w.getHumidity());
        }
    }

    /**
     * Służy do zapisu przechowywanych danych pogodwych do jsona.
     * @param filePath - miejsce zapisu pliku
     */
    public void saveToJson(String filePath) {

        Gson gson;
        gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            Writer file = new FileWriter(filePath + "/file.json");
            gson.toJson(weatherDataSet.toArray(), file);
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Służy do zaktualizowania bieżących danych pogodowych
     * @param weatherData - obiekt klasy WeatherData
     */
    public void updateData(WeatherData weatherData) {
        weatherDataSet.add(weatherData);
        temperatureValues.add(weatherData.getTemp());
        humidityValues.add(weatherData.getHumidity());
        pressureValues.add(weatherData.getPressure());
    }

    /**
     * Służy do pobrania listy zarejestrowanych wartości  temperatur.
     * @return temperatureValues - lista temperatur
     */
    public ArrayList<Double> getTemperatureValues() {
        return temperatureValues;
    }
    /**
     * Służy do pobrania listy  zarejestrowanych wartości ciśnienia.
     * @return pressureValues - lista wartości ciśnienia
     */
    public ArrayList<Double> getPressureValues() {
        return pressureValues;
    }
    /**
     * Służy do pobrania listy zarejestrowanych wartości wilgotności.
     * @return temperatureValues - lista wartości wilgotności
     */
    public ArrayList<Double> getHumidityValues() {
        return humidityValues;
    }

    /**
     * Usuwa przechowywane dane.
     */
    public void clearData() {
        weatherDataSet.clear();
        temperatureValues.clear();
        humidityValues.clear();
        pressureValues.clear();
    }

}