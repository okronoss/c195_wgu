/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195pa.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.InputMethodEvent;

/**
 * FXML Controller class
 *
 * @author alex
 */
public class MainScreenController implements Initializable {

    @FXML
    private TextField clientSearch;
    @FXML
    private CheckBox showInactive;
    @FXML
    private TableView<?> clientTable;
    @FXML
    private Button addClientBtn;
    @FXML
    private Button modifyClientBtn;
    @FXML
    private Button inactiveClientBtn;
    @FXML
    private TextField apptSearch;
    @FXML
    private RadioButton apptWeekly;
    @FXML
    private ToggleGroup apptTime;
    @FXML
    private RadioButton apptMonthly;
    @FXML
    private TableView<?> apptTable;
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
        // populate client table
            // filter out inactive by default
        // populate appointment table
    }    

    @FXML
    private void addClient(ActionEvent event) {
        // switch scene to add client
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
    private void generateClientContact(ActionEvent event) {
        // switch scene to client contact info report
    }

    @FXML
    private void filterClients() {
        // search & filter clients out
    }

    @FXML
    private void modifyClient(ActionEvent event) {
        // switch to modify clients screen
    }

    @FXML
    private void inactiveClient(ActionEvent event) {
        // set selected client status to inactive
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
