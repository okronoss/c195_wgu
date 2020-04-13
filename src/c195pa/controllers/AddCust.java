/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195pa.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author alex
 */
public class AddCust implements Initializable {

    @FXML
    private TextField custName;
    @FXML
    private TextField custAddress;
    @FXML
    private TextField custAddress2;
    @FXML
    private TextField custPostalCode;
    @FXML
    private TextField custCity;
    @FXML
    private TextField custPhone;
    @FXML
    private ComboBox<?> custCountry;
    @FXML
    private Button saveCustBtn;
    @FXML
    private Button cancelBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
