/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195pa.controllers;

import static c195pa.AMS.getAppointments;
import static c195pa.AMS.initActiveCustNames;
import static c195pa.AMS.initAllAppts;
import c195pa.models.Appointment;
import static c195pa.models.Appointment.getAllLocations;
import static c195pa.models.Appointment.getClosedDays;
import static c195pa.models.Appointment.getHours;
import static c195pa.models.Appointment.insertAppointment;
import static c195pa.models.Customer.getId;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author alex
 */
public class AddApptController implements Initializable {

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
    @FXML
    private Text errText;

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
            apptTable.setItems(initAllAppts());
            custField.setItems(initActiveCustNames());
        } catch (SQLException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        apptSearch.textProperty().addListener(obs -> filterAppt());
        filterAppt();
        
        locationField.setItems(getAllLocations());
        startTimeField.setItems(getHours());
        endTimeField.setItems(getHours());
        dateField.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean isEmpty) {
                super.updateItem(item, isEmpty);
                LocalDate today = LocalDate.now();
                setDisable(isEmpty || item.compareTo(today) < 0);
                DayOfWeek[] closed = (DayOfWeek[]) getClosedDays();
                for (int i = 0; i < closed.length; ++i) {
                    setDisable(isDisable() || item.getDayOfWeek() == closed[i]);
                }
            }
        });
    }    


    @FXML
    private void saveAppt(ActionEvent event) throws IOException, SQLException {
        // insert appointment
        int customerId = -1;
        String title = titleField.getText();
        String description = descriptionField.getText();
        String location = locationField.getValue();
        String contact = contactField.getText();
        String type = typeField.getText();
        String url = urlField.getText();
        LocalDate date = dateField.getValue();
        LocalTime startTime;
        LocalTime endTime;
        ZonedDateTime start = null;
        ZonedDateTime end = null;
        boolean valid = true;
        
        errText.setVisible(false);
        
        if (custField.getValue() == null) {
            errText.setText("Customer is Required.");
            errText.setVisible(true);
            valid = false;
        } else {
            customerId = getId(custField.getValue());
        }
        
        if (type.isEmpty()) {
            errText.setText("Type is Required.");
            errText.setVisible(true);
            valid = false;
        }
        
        if (date == null || startTimeField.getValue() == null || endTimeField.getValue() == null) {
            errText.setText("Date, Start Time, and End Time is Required.");
            errText.setVisible(true);
            valid = false;
        } else {
            startTime = LocalTime.parse(startTimeField.getValue(), DateTimeFormatter.ofPattern("hh:mm a")) ;
            endTime = LocalTime.parse(endTimeField.getValue(), DateTimeFormatter.ofPattern("hh:mm a"));
            
            start = ZonedDateTime.of(date, startTime, ZoneId.systemDefault());
            end = ZonedDateTime.of(date, endTime, ZoneId.systemDefault());
        }

        if (valid) {
            insertAppointment(customerId, title, description, location, contact, type, url, start, end);
            returnToMainScreen(event);
        }    
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
        
        searchResults = getAppointments(apptSearch.getText());
        
        apptTable.setItems(searchResults);
    }

    @FXML
    private void validateTimes(ActionEvent event) {
        LocalTime start = startTimeField.getValue() != null ? LocalTime.parse(startTimeField.getValue(), DateTimeFormatter.ofPattern("hh:mm a")) : null ;
        LocalTime end = endTimeField.getValue() != null ? LocalTime.parse(endTimeField.getValue(), DateTimeFormatter.ofPattern("hh:mm a")) : null;
        
        errText.setVisible(false);

        if (start != null && end != null && (start.isAfter(end) || start.equals(end))) {
            errText.setVisible(true);
            endTimeField.setValue(null);
        }
    }

    private void switchScene(Button button, String fxmlFile, String title, int width, int height) throws IOException {
        Stage stage = (Stage) button.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/c195pa/views/" + fxmlFile));
        Scene newScene = new Scene(root, width, height);
        stage.setTitle(title);
        stage.setScene(newScene);
    }
}
