/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195pa.controllers;

import c195pa.models.Appointment;
import c195pa.models.Customer;
import c195pa.models.Database;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

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
    private TableColumn<Appointment, Date> apptDateCol;
    @FXML
    private TableColumn<Appointment, Date> apptTimeCol;
    @FXML
    private TableColumn<Appointment, String> apptTypeCol;
    @FXML
    private TableColumn<Appointment, String> apptCustCol;
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

        // populate Customer table
        custNameCol.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        custPhoneCol.setCellValueFactory(cellData -> cellData.getValue().getPhoneProperty());
        custStatusCol.setCellValueFactory(cellData -> cellData.getValue().getStatusProperty());
            // filter out inactive by default
        // populate appointment table
//        apptDateCol.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());
//        apptTimeCol.setCellValueFactory(cellData -> cellData.getValue().getTimeProperty());
//        apptTypeCol.setCellValueFactory(cellData -> cellData.getValue().getTypeProperty());
//        apptCustCol.setCellValueFactory(cellData -> cellData.getValue().getCustProperty());
        try {
            custTable.setItems(Database.initAllCusts());
//            apptTable.setItems(Database.initAllAppts());
        } catch (SQLException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void addCust(ActionEvent event) {
        // switch scene to add customer
    }

    @FXML
    private void addAppt(ActionEvent event) {
        // switch scene to add appointment
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
        // search & filter customers out
    }

    @FXML
    private void modifyCust(ActionEvent event) {
        // switch to modify customers screen
    }

    @FXML
    private void inactiveCust(ActionEvent event) {
        // set selected customer status to inactive
    }

    @FXML
    private void filterAppt() {
        // search & filter appointments
    }

    @FXML
    private void modifyAppt(ActionEvent event) {
        // switch to modify appointment screen
    }

    @FXML
    private void deleteAppt(ActionEvent event) {
        // delete slected appointment
    }
    
}
