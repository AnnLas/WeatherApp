package sample.WeatherData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;

public class DataHolder {
    private ArrayList<WeatherData> weatherDataSet = new ArrayList<>();
    private ArrayList<Double> temperatureValues= new ArrayList<>();
    private ArrayList<Double> pressureValues= new ArrayList<>();
    private ArrayList<Double> humidityValues= new ArrayList<>();


    public ArrayList<WeatherData> getWeatherDataSet() {
        return weatherDataSet;
    }

    public void setWeatherDataSet(ArrayList<WeatherData> weatherDataSet) {
        clearData();
        this.weatherDataSet = weatherDataSet;
        for (WeatherData w:weatherDataSet) {
            temperatureValues.add(w.getTemp());
            pressureValues.add(w.getPressure());
            humidityValues.add(w.getHumidity());
        }
    }

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

    public void updateData(WeatherData weatherData) {
        weatherDataSet.add(weatherData);
        temperatureValues.add(weatherData.getTemp());
        humidityValues.add(weatherData.getHumidity());
        pressureValues.add(weatherData.getPressure());
    }

    public ArrayList<Double> getTemperatureValues() {
        return temperatureValues;
    }

    public ArrayList<Double> getPressureValues() {
        return pressureValues;
    }

    public ArrayList<Double> getHumidityValues() {
        return humidityValues;
    }

    public void clearData() {
        weatherDataSet.clear();
        temperatureValues.clear();
        humidityValues.clear();
        pressureValues.clear();
    }

}