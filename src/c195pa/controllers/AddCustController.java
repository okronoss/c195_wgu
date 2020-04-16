/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195pa.controllers;

import c195pa.models.Customer;
import static c195pa.models.Customer.initAllCountries;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author alex
 */
public class AddCustController implements Initializable {

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
    private ComboBox<String> custCountry;
    @FXML
    private Button saveCustBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Text errorText;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            custCountry.setItems(initAllCountries());
        } catch (SQLException ex) {
            Logger.getLogger(AddCustController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void saveCust(ActionEvent event) throws IOException, SQLException {
        String name = custName.toString();
        String address = custAddress.toString();
        String address2 = custAddress2.toString();
        String postalCode = custPostalCode.toString();
        String city = custCity.toString();
        String country = custCountry.toString();
        String phoneNum = custPhone.toString();
        boolean valid = true;
        
        errorText.setVisible(false);
        
        if (name.isEmpty()) {
            errorText.setText("Name is Required.");
            errorText.setVisible(true);
            valid = false;
        }
        
        if (address.isEmpty()) {
            errorText.setText("Address is Required.");
            errorText.setVisible(true);
            valid = false;
        }
        
        if (postalCode.isEmpty()) {
            errorText.setText("Postal Code is Required.");
            errorText.setVisible(true);
            valid = false;
        }
        
        if (city.isEmpty()) {
            errorText.setText("City is Required.");
            errorText.setVisible(true);
            valid = false;
        }
        
        if (country.isEmpty()) {
            errorText.setText("Country is Required.");
            errorText.setVisible(true);
            valid = false;
        }
        
        if (phoneNum.isEmpty()) {
            errorText.setText("Phone Number is Required.");
            errorText.setVisible(true);
            valid = false;
        }
        
        if (valid) {
            Customer.insertCustomer(name, address, address2, postalCode, city, country, phoneNum);
        }
        
        returnToMainScreen();
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
    private void switchScene(Button button, String fxmlFile, String title, int width, int height) throws IOException {
        Stage stage = (Stage) button.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/c195pa/views/" + fxmlFile));
        Scene newScene = new Scene(root, width, height);
        stage.setTitle(title);
        stage.setScene(newScene);
    }
}
