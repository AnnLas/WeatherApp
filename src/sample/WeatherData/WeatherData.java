package sample.WeatherData;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Observable;


public class WeatherData  {
    private double temp;
    private double pressure;
    private double humidity;
    private String town;
    private LocalTime registrationTime;
    private LocalDate registrationDate;
    private String forecastTimeAndDate;



    public WeatherData() {
        this.registrationTime = LocalTime.now();
        this.registrationDate = LocalDate.now();
    }

    public LocalTime getRegistrationTime() {
        return registrationTime;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }


    public double getTemp() {
        return temp;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }



    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    @Override
    public String toString() {
        return String.format("Town: %s%nTemperature: %.2f%nPressure: %.2f%nHumidity: %.2f%n",
                getTown(), getTemp(), getPressure(), getHumidity());
    }


    public void setForecastTimeAndDate(String forecastTime) {
        this.forecastTimeAndDate = forecastTime;
    }

    public String getForecastTimeAndDate() {
        return forecastTimeAndDate;
    }

    public void setData(WeatherData weatherData) {
        this.temp = weatherData.temp;
        this.pressure = weatherData.pressure;
        this.humidity = weatherData.humidity;
         this.town = weatherData.town;

    }


}
