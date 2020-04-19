/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195pa.controllers;

import static c195pa.AMS.APPT_INTERVAL;
import static c195pa.AMS.CLOSED_DAYS;
import static c195pa.AMS.CLOSE_HOUR;
import static c195pa.AMS.LOCATIONS;
import static c195pa.AMS.MODIFY_APPT_ID;
import static c195pa.AMS.OPEN_HOUR;
import static c195pa.AMS.getActiveCustNames;
import static c195pa.AMS.getAllAppts;
import static c195pa.AMS.getAppointments;
import c195pa.models.Appointment;
import static c195pa.models.Appointment.apptOverlap;
import static c195pa.models.Appointment.getHours;
import static c195pa.models.Appointment.updateAppointment;
import c195pa.models.Customer;
import static c195pa.models.Customer.getId;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
public class ModifyApptController implements Initializable {

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
    private Text errText;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private TableView<Appointment> apptTable;
    private TableColumn<Appointment, String> apptTitleCol;
    @FXML
    private TableColumn<Appointment, String> apptTypeCol;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> apptDateCol;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> apptStartTimeCol;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> apptEndTimeCol;
    @FXML
    private TextField apptSearch;

    private final Appointment modifyAppt;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm a");

    public ModifyApptController() throws SQLException {
        this.modifyAppt = new Appointment(MODIFY_APPT_ID);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Use lambdas to set up appointment tableview
        apptTypeCol.setCellValueFactory(cellData -> cellData.getValue().getTypeProperty());
        apptDateCol.setCellValueFactory(cellData -> cellData.getValue().getStartProperty());
        apptDateCol.setCellFactory(col -> new TableCell<Appointment, ZonedDateTime>() {
            @Override
            protected void updateItem(ZonedDateTime item, boolean isEmpty) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                super.updateItem(item, isEmpty);
                if (isEmpty) {
                    setText(null);
                } else {
                    setText(String.format(item.format(dateFormatter)));
                }
            }
        });
        apptStartTimeCol.setCellValueFactory(cellData -> cellData.getValue().getStartProperty());
        apptStartTimeCol.setCellFactory(col -> new TableCell<Appointment, ZonedDateTime>() {
            @Override
            protected void updateItem(ZonedDateTime item, boolean isEmpty) {
                DateTimeFormatter formatter = dtf;
                super.updateItem(item, isEmpty);
                if (isEmpty) {
                    setText(null);
                } else {
                    setText(String.format(item.format(formatter)));
                }
            }
        });
        apptEndTimeCol.setCellValueFactory(cellData -> cellData.getValue().getEndProperty());
        apptEndTimeCol.setCellFactory(col -> new TableCell<Appointment, ZonedDateTime>() {
            @Override
            protected void updateItem(ZonedDateTime item, boolean isEmpty) {
                DateTimeFormatter formatter = dtf;
                super.updateItem(item, isEmpty);
                if (isEmpty) {
                    setText(null);
                } else {
                    setText(String.format(item.format(formatter)));
                }
            }
        });
        
        try {
            apptTable.setItems(getAllAppts());
            custField.setItems(getActiveCustNames());
        } catch (SQLException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

        apptSearch.textProperty().addListener(obs -> filterAppt());
        filterAppt();

        locationField.setItems(LOCATIONS);
        startTimeField.setItems(getHours(OPEN_HOUR, CLOSE_HOUR.minusMinutes(APPT_INTERVAL)));
        endTimeField.setItems(getHours(OPEN_HOUR.plusMinutes(APPT_INTERVAL), CLOSE_HOUR));
        dateField.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean isEmpty) {
                super.updateItem(item, isEmpty);
                LocalDate today = LocalDate.now();
                setDisable(isEmpty || item.compareTo(today) < 0);
                for (int i = 0; i < CLOSED_DAYS.length; ++i) {
                    setDisable(isDisable() || item.getDayOfWeek() == CLOSED_DAYS[i]);
                }
            }
        });

        // fill in current values
        try {
            custField.setValue(Customer.getName(modifyAppt.getCustId()));
            titleField.setText(modifyAppt.getTitle());
            descriptionField.setText(modifyAppt.getDescription());
            locationField.setValue(modifyAppt.getLocation());
            contactField.setText(modifyAppt.getContact());
            typeField.setText(modifyAppt.getType());
            urlField.setText(modifyAppt.getUrl());
            dateField.setValue(LocalDate.from(modifyAppt.getStart()));
            startTimeField.setValue(modifyAppt.getStart().format(dtf));
            endTimeField.setValue(modifyAppt.getEnd().format(dtf));
        } catch (SQLException ex) {
            Logger.getLogger(AddCustController.class.getName()).log(Level.SEVERE, null, ex);
            try {
                returnToMainScreen();
            } catch (IOException ex1) {
                Logger.getLogger(ModifyCustController.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    @FXML
    private void validateTimes(ActionEvent event) {
        LocalTime start = startTimeField.getValue() != null ? LocalTime.parse(startTimeField.getValue(), dtf) : null;
        LocalTime end = endTimeField.getValue() != null ? LocalTime.parse(endTimeField.getValue(), dtf) : null;

        errText.setVisible(false);

        if (start != null && end == null) {
            endTimeField.setItems(getHours(LocalTime.parse(startTimeField.getValue(), dtf), CLOSE_HOUR));
        }

        if (start == null && end != null) {
            startTimeField.setItems(getHours(OPEN_HOUR, LocalTime.parse(endTimeField.getValue(), dtf)));
        }

        if (start != null && end != null && (start.isAfter(end) || start.equals(end))) {
            errText.setText("Start time cannot be be after or equal to end time.");
            errText.setVisible(true);
            endTimeField.setValue(null);
            startTimeField.setItems(getHours(OPEN_HOUR, CLOSE_HOUR.minusMinutes(APPT_INTERVAL)));
            endTimeField.setItems(getHours(OPEN_HOUR.plusMinutes(APPT_INTERVAL), CLOSE_HOUR));
        }
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
            startTime = LocalTime.parse(startTimeField.getValue(), dtf);
            endTime = LocalTime.parse(endTimeField.getValue(), dtf);

            if (startTime.isBefore(OPEN_HOUR) || endTime.isAfter(CLOSE_HOUR)) {
                errText.setText("Appointment must be within business hours");
                errText.setVisible(true);
                valid = false;
            }
            
            start = ZonedDateTime.of(date, startTime, ZoneId.systemDefault());
            end = ZonedDateTime.of(date, endTime, ZoneId.systemDefault());
            
            if (apptOverlap(modifyAppt.getId(), start, end)) {
                errText.setText("Appointment must not overlap with existing appointments.");
                errText.setVisible(true);
                valid = false;                
            }

        }

        if (valid) {
            if (updateAppointment(modifyAppt.getId(), customerId, title, description, location, contact, type, url, start, end)) {
                returnToMainScreen();
            } else {
                errText.setText("Error connecting to the database.");
                errText.setVisible(true);
            }
        }
    }

    @FXML
    private void returnToMainScreen() throws IOException {
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

    private void switchScene(Button button, String fxmlFile, String title, int width, int height) throws IOException {
        Stage stage = (Stage) button.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/c195pa/views/" + fxmlFile));
        Scene newScene = new Scene(root, width, height);
        stage.setTitle(title);
        stage.setScene(newScene);
    }
}
