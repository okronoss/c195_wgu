/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195pa.controllers;

import static c195pa.AMS.getAppointments;
import static c195pa.AMS.getCustomers;
import static c195pa.AMS.initAllAppts;
import static c195pa.AMS.initAllCusts;
import c195pa.models.Appointment;
import static c195pa.models.Appointment.deleteAppointment;
import c195pa.models.Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
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
    public static int MODIFY_CUST_ID;
    public static int MODIFY_APPT_ID;
    
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
    private TableColumn<Appointment, String> apptTitleCol;
    @FXML
    private TableColumn<Appointment, String> apptTypeCol;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> apptDateCol;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> apptTimeCol;
    @FXML
    private Button addApptBtn;
    @FXML
    private Button modifyApptBtn;
    @FXML
    private Button deleteApptBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // set up customer tableview
        custNameCol.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        custPhoneCol.setCellValueFactory(cellData -> cellData.getValue().getPhoneProperty());
        custStatusCol.setCellValueFactory(cellData -> cellData.getValue().getStatusProperty());
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
            custTable.setItems(initAllCusts());
            apptTable.setItems(initAllAppts());
        } catch (SQLException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        custSearch.textProperty().addListener(obs -> filterCusts());
        filterCusts();
        apptSearch.textProperty().addListener(obs -> filterAppt());
        filterAppt();
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
    private void generateApptTypes(ActionEvent event) {
        // switch scene to appointment type report
    }

    @FXML
    private void generateConsultantSch(ActionEvent event) {
        // switch scene to consltant schedule report
    }

    @FXML
    private void generateCustContact(ActionEvent event) {
        // switch scene to customer contact info report
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
            if (result.get() == ButtonType.OK){
                if(Customer.toggleActive(inactiveCust.getId())) {
                    initAllCusts();
                    initAllAppts();
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
    private void modifyAppt(ActionEvent event) {
        // switch to modify appointment screen
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
            if (result.get() == ButtonType.OK){
                if(deleteAppointment(delAppt.getId())) {
                    initAllAppts();
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
