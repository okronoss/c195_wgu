/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195pa.controllers;

import c195pa.AMS;
import c195pa.models.Appointment;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author alex
 */
public class AddApptController implements Initializable {
    DayOfWeek[] closedDays = {DayOfWeek.SUNDAY, DayOfWeek.SATURDAY};

    @FXML
    private ComboBox<String> custField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField descriptionField;
    @FXML
    private ComboBox<String> locationField;
    @FXML
    private TextField contactField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField urlField;
    @FXML
    private DatePicker dateField;
    @FXML
    private ComboBox<String> endTimeField;
    @FXML
    private ComboBox<String> startTimeField;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private TableView<Appointment> apptTable;
    @FXML
    private TableColumn<Appointment, String> apptTitleCol;
    @FXML
    private TableColumn<Appointment, String> apptTypeCol;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> apptDateCol;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> apptTimeCol;
    @FXML
    private TextField apptSearch;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // set up appointment tableview
        apptTitleCol.setCellValueFactory(cellData -> cellData.getValue().getTitleProperty());
        apptTypeCol.setCellValueFactory(cellData -> cellData.getValue().getTypeProperty());
        apptDateCol.setCellValueFactory(cellData -> cellData.getValue().getStartProperty());
        apptDateCol.setCellFactory(col -> new TableCell<Appointment, ZonedDateTime>() {
            @Override
            protected void updateItem(ZonedDateTime item, boolean isEmpty) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                super.updateItem(item, isEmpty);
                if (isEmpty) {
                    setText(null);
                } else {
                    setText(String.format(item.format(formatter)));
                }
            }
        });
        apptTimeCol.setCellValueFactory(cellData -> cellData.getValue().getStartProperty());
        apptTimeCol.setCellFactory(col -> new TableCell<Appointment, ZonedDateTime>() {
            @Override
            protected void updateItem(ZonedDateTime item, boolean isEmpty) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
                super.updateItem(item, isEmpty);
                if (isEmpty) {
                    setText(null);
                } else {
                    setText(String.format(item.format(formatter)));
                }
            }
        });
        try {
            apptTable.setItems(AMS.initAllAppts());
        } catch (SQLException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        apptSearch.textProperty().addListener(obs -> filterAppt());
        filterAppt();
        
        // fill in combobox options
//        custField.setItems(initAllCusts());
//        locationField.setItems();
//        startTimeField.setItems();
//        endTimeField.setItems();
        dateField.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate item, boolean isEmpty) {
                super.updateItem(item, isEmpty);
                LocalDate today = LocalDate.now();
                setDisable(isEmpty || item.compareTo(today) < 0);
                for (int i = 0; i < closedDays.length; ++i) {
                    setDisable(isDisable() || item.getDayOfWeek() == closedDays[i]);
                }
            }
        });
    }    


    @FXML
    private void saveAppt(ActionEvent event) throws IOException {
        
        // insert appointment
//        insertAppointment();
        returnToMainScreen(event);
    }

    @FXML
    private void returnToMainScreen(ActionEvent event) throws IOException {
        Button button = cancelBtn;
        String fxmlFile = "MainScreen.fxml";
        String title = "Appointment Management System";
        int width = 1000;
        int height = 600;
        
        switchScene(button, fxmlFile, title, width, height);
    }

    @FXML
    private void filterAppt() {
        ObservableList<Appointment> searchResults;
        
        searchResults = AMS.getAppointments(apptSearch.getText());
        
        apptTable.setItems(searchResults);
    }
    
    private void switchScene(Button button, String fxmlFile, String title, int width, int height) throws IOException {
        Stage stage = (Stage) button.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/c195pa/views/" + fxmlFile));
        Scene newScene = new Scene(root, width, height);
        stage.setTitle(title);
        stage.setScene(newScene);
    }
}
