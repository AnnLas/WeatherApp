package sample.WeatherData;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;

public class WeatherStation extends Observable implements Runnable {

    protected volatile boolean isRunning = false;
    private int interval;
    private Thread worker;
    private String town;
    private WeatherData weatherData;
    private int responseCode;
    private DataHolder dataHolder;


    public WeatherStation() {
        interval = 10000;
        weatherData = new WeatherData();
        dataHolder = new DataHolder();

    }

    public void setInterval(int interval) {

        this.interval = interval;
    }

    public WeatherData getWeatherData() {
        return weatherData;
    }

    public WeatherStation(int interval) {
        this.interval = interval;
    }

    public void start() {
        worker = new Thread(this, " Clock thread");
        worker.start();

    }

    public int getResponseCode() {
        return responseCode;
    }

    public void stop() {
        isRunning = false;
    }

    public void interrupt() {

        isRunning = false;
        worker.interrupt();
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getTown() {
        return town;
    }

    public WeatherData request(String town) {
        String APPID = "eea1a6f437205efef41e5b8f5f9cd097";
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + town + "&units=metric&APPID=" + APPID;
        StringBuffer response = new StringBuffer();

        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } else {

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();

        JsonObject jsonObj = jsonParser.parse(String.valueOf(response)).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Object jsonElement = jsonObj.getAsJsonObject("main");
        WeatherData townWeather = gson.fromJson((JsonElement) jsonElement, WeatherData.class);
        townWeather.setTown(town);
        weatherData.setData(townWeather);
        // System.out.println(townWeather.toString());
        setCurrentParameters(townWeather);
        dataHolder.updateData(townWeather);
        return townWeather;
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {

            try {
                request(getTown());
                Thread.sleep(interval);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Failed to complete operation");
            }

        }

    }

    private void setCurrentParameters(WeatherData weatherData) {
        this.weatherData = weatherData;
        parametersChanged();
    }

    private void parametersChanged() {
        setChanged();
        notifyObservers();
    }

    public DataHolder getDataHolder() {
        return dataHolder;
    }
}
