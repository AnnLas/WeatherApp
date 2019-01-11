package sample.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import sample.WeatherData.WeatherData;
import sample.WeatherData.WeatherStation;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
/**
 *DataTables Controller jest klasą odpowiedzialną za wyświetlanie danych pogodowych(temperatura, ciśnienie, wilgotność)
 * w tabeli w panelu AnchorPane. Dane te są na bieżąco aktualizowane i wyświetlane w stosunku do zmian w obiekcie
 * klasy WeatherStation.
 */
public class DataTablesController extends AnchorPane implements Initializable, Observer {
    private WeatherStation weatherStation;

    @FXML
    private TableView<WeatherData> weatherdata_table;
    @FXML
    private TableColumn<WeatherData, String> time_column;

    @FXML
    private TableColumn<WeatherData, String> date_column;

    @FXML
    private TableColumn<WeatherData, String> town_column;

    @FXML
    private TableColumn<WeatherData, String> temperature_column;

    @FXML
    private TableColumn<WeatherData, String> pressure_column;

    @FXML
    private TableColumn<WeatherData, String> humidity_column;

    /**
     * Tworzy instancję klasy DataTablesController.
     * Wyświetla fragment sceny.
     * @param weatherStation - obiekt klasy WeatherStation
     */
    public DataTablesController(WeatherStation weatherStation) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/data_tables.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        this.weatherStation = weatherStation;
        weatherStation.addObserver(this);


        try {
            fxmlLoader.load();
            weatherdata_table.getItems().addAll(weatherStation.getDataHolder().getWeatherDataSet());

        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        weatherStation= new WeatherStation();
        columnsInit();
        displayData();


    }

    private void displayData() {
        weatherdata_table.getItems().addAll(weatherStation.getDataHolder().getWeatherDataSet());

    }

    /**
     * Aktualizuje dane pogodowe w tabelach.
     * @param observable
     * @param o
     */
    @Override
        public void update(Observable observable, Object o) {
        weatherStation = (WeatherStation) observable;
        weatherdata_table.getItems().setAll(weatherStation.getDataHolder().getWeatherDataSet());

    }


    private void columnsInit(){
        town_column.setCellValueFactory(new PropertyValueFactory<>("town"));
        pressure_column.setCellValueFactory(new PropertyValueFactory<>("pressure"));
        humidity_column.setCellValueFactory(new PropertyValueFactory<>("humidity"));
        temperature_column.setCellValueFactory(new PropertyValueFactory<>("temp"));
        time_column.setCellValueFactory(new PropertyValueFactory<>("registrationTime"));
        date_column.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));


    }

    public void clearData() {
        weatherdata_table.getItems().removeAll();
        weatherdata_table.refresh();
        for (int i=0; i<weatherdata_table.getItems().size();i++)
        weatherdata_table.getItems().set(i,null);
    }
}
