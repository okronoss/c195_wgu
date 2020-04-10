/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195pa.controllers;

import c195pa.models.Appointment;
import c195pa.models.Customer;
import c195pa.models.Database;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
    private RadioButton apptWeekly;
    @FXML
    private ToggleGroup apptTime;
    @FXML
    private RadioButton apptMonthly;
    @FXML
    private TableView<Appointment> apptTable;
    @FXML
    private TableColumn<Appointment, String> apptTitleCol;
    @FXML
    private TableColumn<Appointment, String> apptTypeCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> apptDateCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> apptTimeCol;
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
        apptDateCol.setCellFactory(col -> new TableCell<Appointment, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean isEmpty) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                super.updateItem(item, isEmpty);
                if (isEmpty) {
                    setText(null);
                } else {
                    setText(String.format(item.format(formatter)));
                }
            }
        });
        apptTimeCol.setCellValueFactory(cellData -> cellData.getValue().getStartProperty());
        apptTimeCol.setCellFactory(col -> new TableCell<Appointment, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean isEmpty) {
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
            custTable.setItems(Database.initAllCusts());
            // filter out inactive by default
            apptTable.setItems(Database.initAllAppts());
        } catch (SQLException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        custSearch.textProperty().addListener(obs -> filterCusts());
        filterCusts();
        apptSearch.textProperty().addListener(obs -> filterAppt());
    }    

    @FXML
    private void addCust(ActionEvent event) throws IOException {
        Button button = addCustBtn;
        String fxmlFile = "AddCust.fxml";
        String title = "Appointment Management System";
        int width = 1000;
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
        
        searchResults = Database.getCustomers(custSearch.getText(), showInactive.isSelected());
        
        custTable.setItems(searchResults);
    }
    
    @FXML
    private void filterAppt() {
        ObservableList<Appointment> searchResults;
        
        searchResults = Database.getAppointments(apptSearch.getText());
        
        apptTable.setItems(searchResults);
    }

    @FXML
    private void modifyCust(ActionEvent event) throws IOException {
        Button button = modifyCustBtn;
        String fxmlFile = "ModifyCust.fxml";
        String title = "Appointment Management System";
        int width = 1000;
        int height = 600;
        
        switchScene(button, fxmlFile, title, width, height);
    }

    @FXML
    private void modifyAppt(ActionEvent event) {
        // switch to modify appointment screen
    }

    @FXML
    private void deleteAppt(ActionEvent event) {
        // delete slected appointment
    }
    
    private void switchScene(Button button, String fxmlFile, String title, int width, int height) throws IOException {
        Stage stage = (Stage) button.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/c195pa/views/" + fxmlFile));
        Scene newScene = new Scene(root, width, height);
        stage.setTitle(title);
        stage.setScene(newScene);
    }
}
