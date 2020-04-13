/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195pa.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author alex
 */
public class Customer {
    private final int id;
    private final int addressId;
    private final String name;
    private final boolean active;
    private final Date createDate;
    private final String createdBy;
    private final Date lastUpdate;
    private final String lastUpdateBy;
    private final String phoneNum;
    
    public Customer (int custId) throws SQLException {
        ResultSet rs = Database.connect().createStatement().executeQuery(""
                + "SELECT * "
                + "FROM customer "
                + "WHERE customerId='" + custId + "'"
                + "INNER JOIN address"
                + "ON customer.addressId = address.addressId;");
        
        rs.next();
        this.id = rs.getInt("customerId");
        this.addressId = rs.getInt("addressId");
        this.name = rs.getString("customerName");
        this.active = rs.getBoolean("active");
        this.createDate = rs.getDate("createDate");
        this.createdBy = rs.getString("createdBy");
        this.lastUpdate = rs.getDate("lastUpdate");
        this.lastUpdateBy = rs.getString("lastUpdateBy");
        this.phoneNum = rs.getString("phone");
    }
    
    public Customer (int custId, int addressId, String name, boolean active, Date createDate, String createdBy, Date lastUpdate, String lastUpdateBy) throws SQLException {
        this.id = custId;
        this.addressId = addressId;
        this.name = name;
        this.active = active;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        
        ResultSet rs = Database.connect().createStatement().executeQuery("SELECT * FROM address WHERE addressId='" + this.addressId + "';");
        
        rs.next();
        this.phoneNum = rs.getString("phone");
    }
    
    public void createCustomer (String name, String address, String address2, String postalCode, String city, String country, String phoneNum) {
        // name 
        // address
        // address 2
        // postal code
        // city
        // country
        // phone number
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getPhone() {
        return this.phoneNum;
    }
    
    public Boolean getStatus() {
        return this.active;
    }
    
    public SimpleStringProperty getNameProperty() {
        return new SimpleStringProperty(this.name);
    }

    public SimpleStringProperty getStatusProperty() {
        return active ?  new SimpleStringProperty("Active") :  new SimpleStringProperty("Inactive");
    }

    public SimpleStringProperty getPhoneProperty() {
        return new SimpleStringProperty(this.phoneNum);
    }
    
}
