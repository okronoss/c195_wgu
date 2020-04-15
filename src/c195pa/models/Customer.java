/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195pa.models;

import c195pa.AMS;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author alex
 */
public final class Customer {
    private final int id;
    private final int addressId;
    private final String name;
    private final boolean active;
    private final String phoneNum;
    
    public Customer (int custId) throws SQLException {
        ResultSet rs = AMS.connect().createStatement().executeQuery(""
                + "SELECT c.customerId, c.addressId, c.customerName, c.active, a.phone "
                + "FROM customer c "
                + "INNER JOIN address a "
                + "ON c.addressId = a.addressId "
                + "WHERE c.customerId='" + custId + "';");
        
        rs.next();
        this.id = rs.getInt("customerId");
        this.addressId = rs.getInt("addressId");
        this.name = rs.getString("customerName");
        this.active = rs.getBoolean("active");
        this.phoneNum = rs.getString("phone");
    }
    
    public Customer (int custId, int addressId, String name, boolean active) throws SQLException {
        this.id = custId;
        this.addressId = addressId;
        this.name = name;
        this.active = active;
        this.phoneNum = getPhone(addressId);
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public SimpleStringProperty getNameProperty() {
        return new SimpleStringProperty(this.name);
    }
    
    public Boolean getStatus() {
        return this.active;
    }

    public SimpleStringProperty getStatusProperty() {
        return active ?  new SimpleStringProperty("Active") :  new SimpleStringProperty("Inactive");
    }

    public String getPhone() {
        return this.phoneNum;
    }
    
    public String getPhone(int addressId) throws SQLException {
        ResultSet rs = AMS.connect().createStatement().executeQuery("SELECT phone FROM address WHERE addressId='" + addressId + "';");
        return rs.next() ? rs.getString("phone") : "";
    }

    public SimpleStringProperty getPhoneProperty() {
        return new SimpleStringProperty(this.phoneNum);
    }

    public String getAddress() throws SQLException {
        ResultSet rs = AMS.connect().createStatement().executeQuery("SELECT address FROM address WHERE addressId='" + this.addressId + "';");
        return rs.next() ? rs.getString("address") : "";
    }

    public String getAddress2() throws SQLException {
        ResultSet rs = AMS.connect().createStatement().executeQuery("SELECT address2 FROM address WHERE addressId='" + this.addressId + "';");
        return rs.next() ? rs.getString("address2") : "";
    }

    public String getPostalCode() throws SQLException {
        ResultSet rs = AMS.connect().createStatement().executeQuery("SELECT postalCode FROM address WHERE addressId='" + this.addressId + "';");
        return rs.next() ? rs.getString("postalCode") : "";
    }

    public String getCity() throws SQLException {
        ResultSet rs = AMS.connect().createStatement().executeQuery(""
                + "SELECT c.city "
                + "FROM city c "
                + "INNER JOIN address a "
                + "ON c.cityId = a.cityId "
                + "WHERE a.addressId='" + this.addressId + "';");
        return rs.next() ? rs.getString("city") : "";
    }
    
    public String getCountry() throws SQLException {
        ResultSet rs = AMS.connect().createStatement().executeQuery(""
                + "SELECT co.country "
                + "FROM country co "
                + "INNER JOIN city ci "
                + "ON co.countryId = ci.countryId "
                + "INNER JOIN address a "
                + "ON ci.cityId = a.cityId "
                + "WHERE a.addressId='" + this.addressId + "';");
        
        return rs.next() ? rs.getString("country") : "";
    }

    public static void insertCustomer (String name, String address, String address2, String postalCode, String city, String country, String phoneNum) throws SQLException {
        String un = AMS.USER.getUsername();
        String countryId;
        Connection conn = AMS.connect();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM country WHERE country='" + country + "'; ");
        
        try {
            conn.setAutoCommit(false);
            
            if (rs.next()) {
                countryId = "'" + rs.getString("countryId") + "'";
            } else {
                conn.createStatement().executeUpdate("INSERT INTO country (country, createDate, createdBy, lastUpdate, lastUpdateBy) "
                        + "VALUES('" + country + "', NOW(), '" + un + "', NOW(), '" + un + "');");
                countryId = "LAST_INSERT_ID()";
            }
            conn.createStatement().executeUpdate("INSERT INTO city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) "
                    + "VALUES('" + city + "', " + countryId + ", NOW(), '" + un + "', NOW(), '" + un + "');");
            conn.createStatement().executeUpdate("INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "VALUES('" + address + "', '" + address2 + "', LAST_INSERT_ID(), '" + postalCode + "', '" + phoneNum + "', NOW(), '" + un + "', NOW(), '" + un + "');");
            conn.createStatement().executeUpdate("INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "VALUES('" + name + "', LAST_INSERT_ID(), 1, NOW(), '" + un + "', NOW(), '" + un + "');");
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            conn.rollback();
        }
    }

    public static void updateCustomer (int custId, String name, String address, String address2, String postalCode, String city, String country, String phoneNum) throws SQLException {
        String un = AMS.USER.getUsername();
        Connection conn = AMS.connect();
        String countryId;
        int cityId;
        int addressId;
        
        
        try {
            ResultSet addressRs = conn.createStatement().executeQuery(""
                    + "SELECT a.cityId, a.addressId "
                    + "FROM customer c "
                    + "INNER JOIN address a "
                    + "ON c.addressId = a.addressId "
                    + "WHERE c.customerId='" + custId + "'; ");
            ResultSet countryRs = conn.createStatement().executeQuery("SELECT * FROM country WHERE country='" + country + "'; ");
            
            if (addressRs.next()) {
                cityId = addressRs.getInt("cityId");
                addressId = addressRs.getInt("addressId");
            } else {
                System.err.println("Update failed.");
                throw new SQLException();
            }
            
            conn.setAutoCommit(false);
            
            if (countryRs.next()) {
                countryId = "'" + countryRs.getString("countryId") + "'";
            } else {
                conn.createStatement().executeUpdate("INSERT INTO country (country, createDate, createdBy, lastUpdate, lastUpdateBy) "
                        + "VALUES('" + country + "', NOW(), '" + un + "', NOW(), '" + un + "');");
                countryId = "LAST_INSERT_ID()";
            }
            conn.createStatement().executeUpdate(""
                    + "UPDATE city "
                    + "SET "
                    + "city = '" + city + "', "
                    + "countryId = " + countryId + ", "
                    + "lastUpdate = NOW(), "
                    + "lastUpdateBy = '" + un + "' "
                    + "WHERE cityId = '" + cityId + "';");
            conn.createStatement().executeUpdate(""
                    + "UPDATE address "
                    + "SET "
                    + "address = '" + address + "', "
                    + "address2 = '" + address2 + "', "
                    + "cityId = '" + cityId + "', "
                    + "postalCode = '" + postalCode + "', "
                    + "phone = '" + phoneNum + "', "
                    + "lastUpdate = NOW(), "
                    + "lastUpdateBy = '" + un + "' "
                    + "WHERE addressId = '" + addressId + "';");
            conn.createStatement().executeUpdate(""
                    + "UPDATE customer "
                    + "SET "
                    + "customerName = '" + name + "', "
                    + "addressId = '" + addressId + "', "
                    + "lastUpdate = NOW(), "
                    + "lastUpdateBy = '" + un + "'"
                    + "WHERE customerId = '" + custId + "';");
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("Update failed. Rolling back changes.");
            conn.rollback();
        }
    }
    
    public static boolean toggleActive(int id) throws SQLException {
        String un = AMS.USER.getUsername();
        Connection conn = AMS.connect();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM customer WHERE customerId='" + id + "';");
        boolean saved = false;
        
        if (rs.next()) {
            if (rs.getBoolean("active")) {
                // they are currently active.
                try {
                    // turn off auto-commit
                    conn.setAutoCommit(false);
                    // set customer to inactive
                    conn.createStatement().executeUpdate("UPDATE customer SET active='0' WHERE customerId='" + id + "';");
                    // delete all attached appointments
                    conn.createStatement().executeUpdate("DELETE FROM appointment WHERE customerId='" + id + "';");
                    // commit changes
                    conn.commit();
                    // turn auto-commit back on
                    conn.setAutoCommit(true);
                    
                    saved = true;
                } catch (SQLException e) {
                    // on failure rollback changes
                    conn.rollback();
                }
            } else {
                // they are currently inactive.
                // set to active.
                conn.createStatement().executeUpdate("UPDATE customer SET active='1' WHERE customerId='" + id + "';");
            }
        } else {
            System.err.println("Error: No Customer found.");
        }
        
        return saved;
    }
}
