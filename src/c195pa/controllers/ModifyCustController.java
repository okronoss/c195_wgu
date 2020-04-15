/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195pa.controllers;

import c195pa.AMS;
import static c195pa.controllers.MainScreenController.MODIFY_CUST_ID;
import c195pa.models.Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class ModifyCustController implements Initializable {

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
        Customer modifyCust = null;
        try {
            custCountry.setItems(AMS.initAllCountries());
            modifyCust = new Customer(MODIFY_CUST_ID);
            custName.setText(modifyCust.getName());
            custAddress.setText(modifyCust.getAddress());
            custAddress2.setText(modifyCust.getAddress2());
            custPostalCode.setText(modifyCust.getPostalCode());
            custCity.setText(modifyCust.getCity());
            custPhone.setText(modifyCust.getPhone());
            custCountry.setValue(modifyCust.getCountry());
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
    private void saveCust() throws IOException, SQLException {
        Customer.updateCustomer(MODIFY_CUST_ID, custName.getText(), custAddress.getText(), custAddress2.getText(), custPostalCode.getText(), custCity.getText(), custCountry.getValue(), custPhone.getText());
        
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
