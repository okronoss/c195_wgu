/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195pa.models;
import java.sql.*;
import java.sql.DriverManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
}
