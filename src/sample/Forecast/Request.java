package sample.Forecast;

import com.google.gson.*;
import sample.WeatherData.WeatherData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Służy do wykonywania zapytań do serwera OpenWeather w celu uzyskania prognozy pogody.
 */
public class Request {
    private ArrayList<WeatherData> forecast;

    /**
     * Tworzy i wysyła zapytanie do serwera OpenWeather, o prognozę pogody w danym mieście.
     *
     * @param town - nazwa wybranego miasta
     */

    public Request(String town) {
        String APPID = "eea1a6f437205efef41e5b8f5f9cd097";
        String url = "http://api.openweathermap.org/data/2.5/forecast?q=" + town + "&units=metric&APPID=" + APPID;
        StringBuffer response = new StringBuffer();

        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObj = jsonParser.parse(String.valueOf(response)).getAsJsonObject();
        JsonArray jsonArray = jsonObj.getAsJsonArray("list");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        forecast = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject listItem = (JsonObject) jsonArray.get(i);
            Object jsonElement = listItem.getAsJsonObject("main");
            forecast.add(gson.fromJson((JsonElement) jsonElement, WeatherData.class));
            forecast.get(i).setForecastTimeAndDate(listItem.get("dt_txt").toString());
            forecast.get(i).setTown(town);


        }
        System.out.println(Arrays.asList(forecast));

    }

    /**
     * Metoda zwracająca prognozę pogody
     *
     * @return forecast - lista z danymi dotyczącymi przyszłych warunków pogodowych w danym mieście
     */
    public ArrayList<WeatherData> getForecast() {
        return forecast;
    }
}
