/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195pa;

import c195pa.models.Appointment;
import c195pa.models.Customer;
import c195pa.models.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author alex
 */
public class AMS extends Application {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://3.227.166.251";
    private static final String DB_USER = "U061zg";
    private static final String DB_PASS = "53688672087";
    public static User USER = null;
    public static ObservableList<String> LOCATIONS = FXCollections.observableArrayList("Phoenix, Arizona", "New York, New York", "London, England");
    public static LocalTime OPEN_HOUR = LocalTime.of(8, 0, 0, 0);
    public static LocalTime CLOSE_HOUR = LocalTime.of(17, 0, 0, 0);
    public static int APPT_INTERVAL = 30;
    public static DayOfWeek[] CLOSED_DAYS = {DayOfWeek.SUNDAY, DayOfWeek.SATURDAY};
    public static int MODIFY_CUST_ID;
    public static int MODIFY_APPT_ID;
    private static Connection conn = null;
    private static final ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static final ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/c195pa/views/Login.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static Connection connect() throws SQLException {
        if (conn == null || !conn.isValid(0)) {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            conn.createStatement().executeQuery("USE U061zg");
        }

        return conn;
    }

    public static ObservableList<Customer> initAllCusts() throws SQLException {
        ResultSet rs = connect().createStatement().executeQuery(""
                + "SELECT * "
                + "FROM customer;");

        if (!allCustomers.isEmpty()) allCustomers.clear();

        while (rs.next()) {
            int id = rs.getInt("customerId");
            int addressId = rs.getInt("addressId");
            String name = rs.getString("customerName");
            boolean active = rs.getBoolean("active");
            allCustomers.add(new Customer(id, addressId, name, active));
        }

        return allCustomers;
    }

    public static ObservableList<String> initActiveCustNames() throws SQLException {
        ObservableList<String> activeCustomers = FXCollections.observableArrayList();
        ResultSet rs = connect().createStatement().executeQuery("SELECT customerName FROM customer WHERE active=1;");

        while (rs.next()) {
            activeCustomers.add(rs.getString("customerName"));
        }

        return activeCustomers;
    }

    public static ObservableList<Appointment> initAllAppts() throws SQLException {
        ResultSet rs = connect().createStatement().executeQuery(""
                + "SELECT * "
                + "FROM appointment;");

        if (!allAppointments.isEmpty()) {
            allAppointments.clear();
        }

        while (rs.next()) {
            int id = rs.getInt("appointmentId");
            int customerId = rs.getInt("customerId");
            int userId = rs.getInt("userId");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String location = rs.getString("location");
            String contact = rs.getString("contact");
            String type = rs.getString("type");
            String url = rs.getString("url");
            ZonedDateTime start = ZonedDateTime.ofInstant(rs.getTimestamp("start").toLocalDateTime().atZone(ZoneId.of("UTC")).toInstant(), ZoneId.systemDefault());
            ZonedDateTime end = ZonedDateTime.ofInstant(rs.getTimestamp("end").toLocalDateTime().atZone(ZoneId.of("UTC")).toInstant(), ZoneId.systemDefault());
            ZonedDateTime createDate = ZonedDateTime.ofInstant(rs.getTimestamp("createDate").toLocalDateTime().atZone(ZoneId.of("UTC")).toInstant(), ZoneId.systemDefault());
            String createdBy = rs.getString("createdBy");
            ZonedDateTime lastUpdate = ZonedDateTime.ofInstant(rs.getTimestamp("lastUpdate").toLocalDateTime().atZone(ZoneId.of("UTC")).toInstant(), ZoneId.systemDefault());
            String lastUpdateBy = rs.getString("lastUpdateBy");

            allAppointments.add(new Appointment(id, customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy));
        }

        return allAppointments;
    }

    public static ObservableList<Customer> getCustomers(String src, boolean showInactive) {
        FilteredList<Customer> searchResults = new FilteredList<>(allCustomers, p -> true);

        if (src == null || src.isEmpty()) {
            if (showInactive) {
                searchResults.setPredicate(p -> true);
            } else {
                searchResults.setPredicate(p -> p.getStatus());
            }
        } else {
            if (showInactive) {
                searchResults.setPredicate(p -> p.getName().toLowerCase().contains(src.toLowerCase()));
            } else {
                searchResults.setPredicate(p -> {
                    return p.getStatus() && p.getName().toLowerCase().contains(src.toLowerCase());
                });
            }
        }

        return searchResults;
    }

    public static ObservableList<Appointment> getAppointments(String src, String toggle) {
        FilteredList<Appointment> searchResults = new FilteredList<>(allAppointments, p -> true);
        int daysOut;

        switch (toggle) {
            case "Weekly":
                daysOut = 7;
                break;
            case "Monthly":
                daysOut = 31;
                break;
            default:
                daysOut = 7;
                break;
        }

        ZonedDateTime min = ZonedDateTime.now();
        ZonedDateTime max = min.plusDays(daysOut);

        if (src == null || src.isEmpty()) {
            searchResults.setPredicate(p -> {
                return p.getStart().isAfter(min) && p.getStart().isBefore(max);
            });
        } else {
            searchResults.setPredicate(p -> {
                return p.getTitle().toLowerCase().contains(src.toLowerCase()) || p.getType().toLowerCase().contains(src.toLowerCase());
            });
        }

        return searchResults;
    }

    public static ObservableList<Appointment> getAppointments(String src) {
        FilteredList<Appointment> searchResults = new FilteredList<>(allAppointments, p -> true);

        ZonedDateTime min = ZonedDateTime.now();

        if (src == null || src.isEmpty()) {
            searchResults.setPredicate(p -> {
                return p.getStart().isAfter(min);
            });
        } else {
            searchResults.setPredicate(p -> {
                return p.getTitle().toLowerCase().contains(src.toLowerCase()) || p.getType().toLowerCase().contains(src.toLowerCase());
            });
        }

        return searchResults;
    }
}
