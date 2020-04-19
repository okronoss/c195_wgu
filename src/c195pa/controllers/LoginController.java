/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195pa.controllers;

import static c195pa.models.User.authUser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author alex
 */
public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button loginBtn;
    @FXML
    private Text errorMessage;
    @FXML
    private Text titleText;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(Locale.getDefault().getLanguage());
        if (Locale.getDefault().getLanguage().equals(new Locale("es").getLanguage())) {
            titleText.setText("Sistema de Citas");
            usernameLabel.setText("Nombre de Usuario");
            usernameField.setPromptText("Nombre de Usuario");
            passwordLabel.setText("Contraseña");
            passwordField.setPromptText("Contraseña");
            loginBtn.setText("iniciar sesión");
            cancelBtn.setText("anular");
            errorMessage.setText("");
        }
    }

    @FXML
    private void exit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void login(ActionEvent event) throws SQLException, IOException, InvalidLoginException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        Button button = loginBtn;
        String fxmlFile = "MainScreen.fxml";
        String title = "Appointment Management System";
        int width = 1000;
        int height = 600;

        if (authUser(username, password)) {
            logActivity(username);
            switchScene(button, fxmlFile, title, width, height);
        } else {
            errorMessage.setVisible(true);
            throw new InvalidLoginException("Username and password are invalid");
        }
    }

    private void switchScene(Button button, String fxmlFile, String title, int width, int height) throws IOException {
        Stage stage = (Stage) button.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/c195pa/views/" + fxmlFile));
        Scene newScene = new Scene(root, width, height);
        stage.setTitle(title);
        stage.setScene(newScene);
    }

    private void logActivity(String username) {
        File log = new File("log.txt");

        try {
            if (!log.exists()) {
                log.createNewFile();
            }
            try (FileWriter writer = new FileWriter(log.getCanonicalPath(), true)) {
                writer.append(username + ": " + Timestamp.valueOf(LocalDateTime.now()) + "\n");
            }
        } catch (IOException e) {
            System.err.println("Failed to save user activity log.");
        }
    }

    private static class InvalidLoginException extends Exception {

        public InvalidLoginException(String message) {
            super(message);
        }
    }
}
