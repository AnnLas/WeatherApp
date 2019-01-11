package sample.WeatherData;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Pojedynczy rejestr danych pogodowych. Zawiera informacje o temperaturze, wiglotności, ciśnieniu, nazwie miasta,
 * którego dotyczy pomiar oraz dacie i godzinie wykonanej rejestracji.
 */
public class WeatherData {
    private double temp;
    private double pressure;
    private double humidity;
    private String town;
    private LocalTime registrationTime;
    private LocalDate registrationDate;
    private String forecastTimeAndDate;

    /**
     * Tworzy instację klasy WeatherData, wraz z danymi dotyczącymi czasu i daty utworzenia danych.
     */
    public WeatherData() {
        this.registrationTime = LocalTime.now();
        this.registrationDate = LocalDate.now();
    }

    /**
     * Metoda zwracająca czas wykonania rejestracji.
     * @return czas wykonania rejestracji
     */
    public LocalTime getRegistrationTime() {
        return registrationTime;
    }

    /**
     * Metoda zwracająca zarejestrowną temperaturę.
     * @return temp - temperatura
     */
    public double getTemp() {
        return temp;
    }
    /**
     * Metoda zwracająca zarejestrowne ciśnienie.
     * @return pressure - ciśnienie
     */
    public double getPressure() {
        return pressure;
    }
    /**
     * Metoda zwracająca zarejestrowną wilgotność.
     * @return humidity - wilgotność
     */
    public double getHumidity() {
        return humidity;
    }

    /**
     * Metoda zwracająca miasto, którego dotyczą otrzymane dane pogodowe.
     * @return town - nazwa miasta
     */
    public String getTown() {
        return town;
    }

    /**
     * Metoda służąca do ustawiania miasta, dla którego zostały wykonane pomiary.
     * @param town - nazwa miasta, dla którego wykonano pomiary
     */
    public void setTown(String town) {
        this.town = town;
    }

    @Override
    public String toString() {
        return String.format("Town: %s%nTemperature: %.2f%nPressure: %.2f%nHumidity: %.2f%n",
                getTown(), getTemp(), getPressure(), getHumidity());
    }

    /**
     * Metoda służąca do ustawienia czasu i daty, którym odpowiada dany rejestr prognozy pogody.
     * @param forecastTimeAndDate - data i czas, którym odpowiada dany rejestr prognozy pogody.
     */
    public void setForecastTimeAndDate(String forecastTimeAndDate) {
        this.forecastTimeAndDate = forecastTimeAndDate;
    }

    /**
     *  Metoda służąca do pobrania czasu i daty, którym odpowiada dany rejestr prognozy pogody.
     * @return forecastTimeAndDate - data i czas, którym odpowiada dany rejestr prognozy pogody.
     */
    public String getForecastTimeAndDate() {
        return forecastTimeAndDate;
    }

    /**
     * Służy do ustawienia danych pogodowych w obiekcie WeatherData
     * @param weatherData
     */
    public void setData(WeatherData weatherData) {
        this.temp = weatherData.temp;
        this.pressure = weatherData.pressure;
        this.humidity = weatherData.humidity;
        this.town = weatherData.town;

    }


}
