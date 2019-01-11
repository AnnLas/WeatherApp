package sample.WeatherData;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;

/**
 * Służy do wykonywania zapytań do serwera OpenWeather w celu uzyskania bieżących danhych pogodowych
 */
public class WeatherStation extends Observable implements Runnable {

    protected volatile boolean isRunning = false;
    private int interval;
    private Thread worker;
    private String town;
    private WeatherData weatherData;
    private int responseCode;
    private DataHolder dataHolder;

    /**
     * Tworzy instancję klasy WeatherStation, z domyślnym interwałem wykonywania zapytań (1s)
     * oraz inicjalizuje obiekty klas WeatherData i DataHolder;
     */
    public WeatherStation() {
        interval = 10000;
        weatherData = new WeatherData();
        dataHolder = new DataHolder();

    }

    /**
     * Metoda służąca do ustawiania częstotliwości wykonywanych zapytań
     *
     * @param interval - Czas po jakim wykonywane jest ponowne zapytanie, wartość w ms.
     */
    public void setInterval(int interval) {

        this.interval = interval;
    }

    /**
     * Służy do uzyskiwania bieżących warunków pogodowych pochodzących z ostatniego zapytania.
     *
     * @return weatherData - dane pogodowe
     */
    public WeatherData getWeatherData() {
        return weatherData;
    }

    /**
     * Tworzy instancję klasy WeatherStation, z wybranym interwałem wykonywania zapytań (1s)
     */
    public WeatherStation(int interval) {
        this.interval = interval;
    }

    /**
     * Rozpoczyna wysyłanie zapytań z określoną częstotliwością.
     */
    public void start() {
        worker = new Thread(this, " Clock thread");
        worker.start();

    }

    /**
     * Zwraca kod odpowiedzi serwera na zapytanie.
     *
     * @return -kod odpowiedzi
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * Zatrzymuje wysyłanie zapytań z określoną częstotliwością.
     */
    public void stop() {
        isRunning = false;
    }

    /**
     * Przerywa wysyłanie zapytań z określoną częstotliwością.
     */
    public void interrupt() {

        isRunning = false;
        worker.interrupt();
    }

    /**
     * Pozwala na ustawienie nazwy miasta, którego mają dotyczyć zapytania.
     *
     * @param town - nazwa miasta
     */
    public void setTown(String town) {
        this.town = town;
    }

    /**
     * Zwraca nazwę miasta, którego dotyczyły zapytania.
     *
     * @return nazwa miasta
     */
    public String getTown() {
        return town;
    }

    /**
     * Tworzy zapytanie do serwera OpenWeather, o aktualne warunki pogodowe w danym mieście.
     * @param town - nazwa wybranego miasta
     * @return bieżące dane pogodowe jako obiekt klasy WeatherData
     */
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
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();

        JsonObject jsonObj = jsonParser.parse(String.valueOf(response)).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = jsonObj.getAsJsonObject("main");
        WeatherData townWeather = gson.fromJson(jsonElement, WeatherData.class);
        townWeather.setTown(town);
        weatherData.setData(townWeather);
        setCurrentParameters(townWeather);
        dataHolder.updateData(townWeather);
        return townWeather;
    }

    /**
     * Odpowiada za wysyłanie zapytań do serwera o odpowiedniej częstotliwości
     */
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

    /**
     * Zwraca obiekt klasy DataHolder
     * @return obiekt klasy DataHolder
     */
    public DataHolder getDataHolder() {
        return dataHolder;
    }
}
