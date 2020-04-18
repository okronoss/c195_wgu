/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195pa.controllers;

import static c195pa.AMS.getAppointments;
import static c195pa.AMS.getCustomers;
import static c195pa.AMS.MODIFY_APPT_ID;
import static c195pa.AMS.MODIFY_CUST_ID;
import static c195pa.AMS.getAllAppts;
import static c195pa.AMS.getAllCusts;
import static c195pa.AMS.getAllUsers;
import c195pa.models.Appointment;
import static c195pa.models.Appointment.checkUpcomingAppt;
import static c195pa.models.Appointment.deleteAppointment;
import c195pa.models.Customer;
import c195pa.models.User;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author alex
 */
public class MainScreenController implements Initializable {

    @FXML
    private TextField custSearch;
    @FXML
    private CheckBox showInactive;
    @FXML
    private TableView<Customer> custTable;
    @FXML
    private TableColumn<Customer, String> custNameCol;
    @FXML
    private TableColumn<Customer, String> custPhoneCol;
    @FXML
    private TableColumn<Customer, String> custStatusCol;
    @FXML
    private Button addCustBtn;
    @FXML
    private Button modifyCustBtn;
    @FXML
    private Button inactiveCustBtn;
    @FXML
    private TextField apptSearch;
    @FXML
    private ToggleGroup apptTime;
    @FXML
    private RadioButton apptWeekly;
    @FXML
    private RadioButton apptMonthly;
    @FXML
    private TableView<Appointment> apptTable;
    @FXML
    private TableColumn<Appointment, String> apptTypeCol;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> apptDateCol;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> apptStartTimeCol;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> apptEndTimeCol;
    @FXML
    private Button addApptBtn;
    @FXML
    private Button modifyApptBtn;
    @FXML
    private Button deleteApptBtn;

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm a");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Use lambdas to set up customer tableview
        custNameCol.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        custPhoneCol.setCellValueFactory(cellData -> cellData.getValue().getPhoneProperty());
        custStatusCol.setCellValueFactory(cellData -> cellData.getValue().getStatusProperty());
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
            custTable.setItems(getAllCusts());
            apptTable.setItems(getAllAppts());
        } catch (SQLException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

        custSearch.textProperty().addListener(obs -> filterCusts());
        filterCusts();
        apptSearch.textProperty().addListener(obs -> filterAppt());
        filterAppt();

        // check for appointment within 15 mins
        try {
            int upcomingApptId = checkUpcomingAppt(15);
            if (upcomingApptId >= 0) {
                Appointment upcomingAppt = new Appointment(upcomingApptId);
                Customer upcomingCust = new Customer(upcomingAppt.getCustId());
                Alert infoDiag = new Alert(Alert.AlertType.INFORMATION);

                infoDiag.setTitle("Upcoming Appointment");
                infoDiag.setHeaderText("Your appointment with " + upcomingCust + " is coming up.");
                infoDiag.setContentText("You have an appointment with " + upcomingCust + " at " + upcomingAppt.getStart().format(dtf) + ".");
            }
        } catch (SQLException ex) {
            Alert alertFailed = new Alert(Alert.AlertType.ERROR);
            alertFailed.setTitle("Error");
            alertFailed.setHeaderText("Failed to get upcoming Appointments.");
            alertFailed.setContentText("Unable to access upcoming Appointments.");

            alertFailed.showAndWait();
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addCust(ActionEvent event) throws IOException {
        Button button = addCustBtn;
        String fxmlFile = "AddCust.fxml";
        String title = "Appointment Management System";
        int width = 500;
        int height = 600;

        switchScene(button, fxmlFile, title, width, height);
    }

    @FXML
    private void addAppt(ActionEvent event) throws IOException {
        Button button = addApptBtn;
        String fxmlFile = "AddAppt.fxml";
        String title = "Appointment Management System";
        int width = 1000;
        int height = 600;

        switchScene(button, fxmlFile, title, width, height);
    }

    @FXML
    private void exit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    public void generateApptTypes() throws SQLException {
        File report = new File("AppointmentTypesByMonth.csv");

        try {
            if (!report.exists()) {
                report.createNewFile();
            }

            try (PrintWriter writer = new PrintWriter(report.getCanonicalPath())) {
                Map<Month, Integer> countByMonth = new HashMap<>();
                ObservableList<Appointment> allAppts = getAllAppts();

                for (Month month : Month.values()) {
                    countByMonth.put(month, 0);
                }

                for (Month month : Month.values()) {
                    allAppts.stream().filter((appt) -> (appt.getStart().getMonth() == month)).forEachOrdered((_item) -> {
                        countByMonth.put(month, countByMonth.get(month) + 1);
                    });
                }

                writer.append("Month,Count\n");
                for (Month month : Month.values()) {
                    writer.append(month + "," + countByMonth.get(month) + "\n");
                }

                writer.close();

                Alert alertFailed = new Alert(Alert.AlertType.INFORMATION);
                alertFailed.setTitle("Success");
                alertFailed.setHeaderText("Report Generated");
                alertFailed.setContentText("Successfully generated report.");

                alertFailed.showAndWait();
            }
        } catch (IOException e) {
            Alert alertFailed = new Alert(Alert.AlertType.ERROR);
            alertFailed.setTitle("Error");
            alertFailed.setHeaderText("Report Failed");
            alertFailed.setContentText("Unable to generate report.");

            alertFailed.showAndWait();
        }
    }

    @FXML
    private void generateConsultantSch(ActionEvent event) throws SQLException {
        File report = new File("ConsultantSchedule.csv");

        try {
            if (!report.exists()) {
                report.createNewFile();
            }

            try (PrintWriter writer = new PrintWriter(report.getCanonicalPath())) {
                ObservableList<User> allUsers = getAllUsers();
                ObservableList<Appointment> allAppts = getAllAppts();
                Comparator<Appointment> comparator = Comparator.comparing(Appointment::getStart);
                FXCollections.sort(allAppts, comparator);
                
                writer.append("Consultant,Type,Date,Start,End\n");
                
                allUsers.forEach((User user) -> {
                    allAppts.stream().filter((appt) -> (appt.getUserId() == user.getId())).forEachOrdered((appt) -> {
                        writer.append(
                                user.getUsername() + ","
                                        + appt.getType() + ","
                                        + appt.getStart().format(DateTimeFormatter.ofPattern("MM-dd-yy")) + ","
                                        + appt.getStart().format(DateTimeFormatter.ofPattern("hh:mm a")) + ","
                                        + appt.getEnd().format(DateTimeFormatter.ofPattern("hh:mm a")) + "\n"
                        );
                    });
                });

                writer.close();

                Alert alertFailed = new Alert(Alert.AlertType.INFORMATION);
                alertFailed.setTitle("Success");
                alertFailed.setHeaderText("Report Generated");
                alertFailed.setContentText("Successfully generated report.");

                alertFailed.showAndWait();
            }
        } catch (IOException e) {
            Alert alertFailed = new Alert(Alert.AlertType.ERROR);
            alertFailed.setTitle("Error");
            alertFailed.setHeaderText("Report Failed");
            alertFailed.setContentText("Unable to generate report.");

            alertFailed.showAndWait();
        }
    }

    @FXML
    private void generateCustContact() throws SQLException {
        File report = new File("CustomerContact.csv");

        try {
            if (!report.exists()) {
                report.createNewFile();
            }

            try (PrintWriter writer = new PrintWriter(report.getCanonicalPath())) {
                ObservableList<Customer> allCusts = getAllCusts();

                writer.append("Customer,Status,Phone\n");

                allCusts.forEach((Customer cust) -> {
                    String status = cust.getStatus() ? "Active" : "Inactive";
                    writer.append(cust.getName() + "," + status + "," + cust.getPhone() + "\n");
                });

                writer.close();

                Alert alertFailed = new Alert(Alert.AlertType.INFORMATION);
                alertFailed.setTitle("Success");
                alertFailed.setHeaderText("Report Generated");
                alertFailed.setContentText("Successfully generated report.");

                alertFailed.showAndWait();
            }
        } catch (IOException e) {
            Alert alertFailed = new Alert(Alert.AlertType.ERROR);
            alertFailed.setTitle("Error");
            alertFailed.setHeaderText("Report Failed");
            alertFailed.setContentText("Unable to generate report.");

            alertFailed.showAndWait();
        }
    }

    @FXML
    private void filterCusts() {
        ObservableList<Customer> searchResults;

        searchResults = getCustomers(custSearch.getText(), showInactive.isSelected());

        custTable.setItems(searchResults);
    }

    @FXML
    private void filterAppt() {
        ObservableList<Appointment> searchResults;
        RadioButton selectedRb = (RadioButton) apptTime.getSelectedToggle();

        searchResults = getAppointments(apptSearch.getText(), selectedRb.getText());

        apptTable.setItems(searchResults);
    }

    @FXML
    private void inactiveCust(ActionEvent event) throws SQLException {
        if (custTable.getSelectionModel().getSelectedItem() != null) {
            Customer inactiveCust = custTable.getSelectionModel().getSelectedItem();

            Alert confirmDiag = new Alert(Alert.AlertType.CONFIRMATION);

            confirmDiag.setTitle("Set " + inactiveCust.getName() + " to Inactive");
            confirmDiag.setHeaderText("Are you sure?");
            confirmDiag.setContentText("This will set this customer to inactive and remove all appointments.\n "
                    + "Are you sure you want to set this customer to inactive?");

            Optional<ButtonType> result = confirmDiag.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (Customer.toggleActive(inactiveCust.getId())) {
                    getAllCusts();
                    getAllAppts();
                } else {
                    Alert alertFailed = new Alert(Alert.AlertType.ERROR);
                    alertFailed.setTitle("Error");
                    alertFailed.setHeaderText("Update Failed");
                    alertFailed.setContentText("Unable to update database.");

                    alertFailed.showAndWait();
                }
            }
        } else {
            Alert alertFailed = new Alert(Alert.AlertType.ERROR);
            alertFailed.setTitle("Error");
            alertFailed.setHeaderText("Invalid Customer");
            alertFailed.setContentText("Please select a customer to modify.");

            alertFailed.showAndWait();
        }
    }

    @FXML
    private void modifyCust(ActionEvent event) throws IOException {
        Button button = modifyCustBtn;
        String fxmlFile = "ModifyCust.fxml";
        String title = "Appointment Management System";
        int width = 1000;
        int height = 600;

        if (custTable.getSelectionModel().getSelectedItem() != null) {
            MODIFY_CUST_ID = custTable.getSelectionModel().getSelectedItem().getId();
            switchScene(button, fxmlFile, title, width, height);
        } else {
            Alert alertFailed = new Alert(Alert.AlertType.ERROR);
            alertFailed.setTitle("Error");
            alertFailed.setHeaderText("Invalid Customer");
            alertFailed.setContentText("Please select a customer to modify.");

            alertFailed.showAndWait();
        }
    }

    @FXML
    private void modifyAppt(ActionEvent event) throws IOException {
        Button button = modifyApptBtn;
        String fxmlFile = "ModifyAppt.fxml";
        String title = "Appointment Management System";
        int width = 1000;
        int height = 600;

        if (apptTable.getSelectionModel().getSelectedItem() != null) {
            MODIFY_APPT_ID = apptTable.getSelectionModel().getSelectedItem().getId();
            switchScene(button, fxmlFile, title, width, height);
        } else {
            Alert alertFailed = new Alert(Alert.AlertType.ERROR);
            alertFailed.setTitle("Error");
            alertFailed.setHeaderText("Invalid Appointment");
            alertFailed.setContentText("Please select a appointment to modify.");

            alertFailed.showAndWait();
        }
    }

    @FXML
    private void deleteAppt(ActionEvent event) throws SQLException {
        if (apptTable.getSelectionModel().getSelectedItem() != null) {
            Appointment delAppt = apptTable.getSelectionModel().getSelectedItem();

            Alert confirmDiag = new Alert(Alert.AlertType.CONFIRMATION);

            confirmDiag.setTitle("Delete Appointment");
            confirmDiag.setHeaderText("Are you sure?");
            confirmDiag.setContentText("This will delete this appointment.\n "
                    + "Are you sure you want to delete this appointment?");

            Optional<ButtonType> result = confirmDiag.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (deleteAppointment(delAppt.getId())) {
                    getAllAppts();
                } else {
                    Alert alertFailed = new Alert(Alert.AlertType.ERROR);
                    alertFailed.setTitle("Error");
                    alertFailed.setHeaderText("Update Failed");
                    alertFailed.setContentText("Unable to update database.");
                    alertFailed.showAndWait();
                }
            }
        } else {
            Alert alertFailed = new Alert(Alert.AlertType.ERROR);
            alertFailed.setTitle("Error");
            alertFailed.setHeaderText("Invalid Customer");
            alertFailed.setContentText("Please select a customer to modify.");

            alertFailed.showAndWait();
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
