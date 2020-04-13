/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195pa.models;
import java.sql.*;
import java.sql.DriverManager;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 *
 * @author alex
 */
public class Database {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://3.227.166.251";
    private static final String DB_USER = "U061zg";
    private static final String DB_PASS = "53688672087";
    private static Connection conn = null;
    private static Statement stmnt = null;
    private static final ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static final ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    
    public static Connection connect() throws SQLException {
        if (conn == null || !conn.isValid(0)) {
            conn = DriverManager.getConnection(URL, DB_USER, DB_PASS);
            stmnt = conn.createStatement();
            stmnt.executeQuery("USE U061zg");
        }
        
        return conn;
    }
    
    public static ObservableList<Customer> initAllCusts () throws SQLException {
        ResultSet rs = Database.connect().createStatement().executeQuery("SELECT * FROM customer;");
        
        if (!allCustomers.isEmpty()) allCustomers.clear();
        
        while (rs.next()) {
            int id = rs.getInt("customerId");
            int addressId = rs.getInt("addressId");
            String name = rs.getString("customerName");
            boolean active = rs.getBoolean("active");
            Date createDate = rs.getDate("createDate");
            String createdBy = rs.getString("createdBy");
            Date lastUpdate = rs.getDate("lastUpdate");
            String lastUpdateBy = rs.getString("lastUpdateBy");
            allCustomers.add(new Customer(id, addressId, name, active, createDate, createdBy, lastUpdate, lastUpdateBy));
        }
        
        return allCustomers;
    }

    public static ObservableList<Appointment> initAllAppts () throws SQLException {
        ResultSet rs = Database.connect().createStatement().executeQuery("SELECT * FROM appointment;");
        
        if (!allAppointments.isEmpty()) allAppointments.clear();
        
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
            
            System.out.println(ZonedDateTime.ofInstant(rs.getTimestamp("start").toLocalDateTime().atZone(ZoneId.of("UTC")).toInstant(), ZoneId.systemDefault()));
            
            allAppointments.add(new Appointment(id, customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy));
        }
        
        return allAppointments;
    }
    
    public static ObservableList<Customer> getCustomers(String src, boolean showInactive) {
        FilteredList<Customer> searchResults = new FilteredList<>(allCustomers, p -> true);
        
        boolean srcIsEmpty = src == null || src.isEmpty();
        
        if(srcIsEmpty && showInactive) {
            searchResults.setPredicate(p -> true);
        }
        else if (!srcIsEmpty && showInactive) {
            searchResults.setPredicate(p -> p.getName().toLowerCase().contains(src.toLowerCase()));
        }
        else if (srcIsEmpty && !showInactive) {
            searchResults.setPredicate(p -> p.getStatus());
        } else if (!srcIsEmpty && !showInactive) {
            searchResults.setPredicate(p -> {
                return p.getStatus() && p.getName().toLowerCase().contains(src.toLowerCase());
            });
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
        
        if(src == null || src.isEmpty()) {
            searchResults.setPredicate(p -> {
                return p.getStart().isAfter(min) && p.getStart().isBefore(max);
            });
        }
        else {
            searchResults.setPredicate(p -> {
                return p.getTitle().toLowerCase().contains(src.toLowerCase()) || p.getType().toLowerCase().contains(src.toLowerCase());
            });
        }
        
        return searchResults;
    }

}
