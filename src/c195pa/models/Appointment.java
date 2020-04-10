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
public class Appointment {
    private final int id;
    private final int customerId;
    private final int userId;
    private final String title;
    private final String description;
    private final String location;
    private final String contact;
    private final String type;
    private final String url;
    private final Date start;
    private final Date end;
    private final Date createDate;
    private final String createdBy;
    private final Date lastUpdate;
    private final String lastUpdateBy;
    private final String customerName;
    
    public Appointment (int apptId) throws SQLException {
        ResultSet rs = Database.connect().createStatement().executeQuery("SELECT * FROM appointment WHERE appointmentId='" + apptId + "';");
        
        rs.next();
        this.id = rs.getInt("appointmentId");
        this.customerId = rs.getInt("customerId");
        this.userId = rs.getInt("userId");
        this.title = rs.getString("title");
        this.description = rs.getString("description");
        this.location = rs.getString("location");
        this.contact = rs.getString("contact");
        this.type = rs.getString("type");
        this.url = rs.getString("url");
        this.start = rs.getDate("start");
        this.end = rs.getDate("end");
        this.createDate = rs.getDate("createDate");
        this.createdBy = rs.getString("createdBy");
        this.lastUpdate = rs.getDate("lastUpdate");
        this.lastUpdateBy = rs.getString("lastUpdateBy");
        
        ResultSet sub = Database.connect().createStatement().executeQuery("SELECT * FROM customer WHERE customerId='" + this.customerId + "';");
        sub.next();
        this.customerName = sub.getString("customerName");
    }
    
    public Appointment (int id, int customerId, int userId, String title, String description, String location, String contact, String type, String url, Date start, Date end, Date createDate, String createdBy, Date lastUpdate, String lastUpdateBy) throws SQLException {
        this.id = id;
        this.customerId = customerId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        
        ResultSet sub = Database.connect().createStatement().executeQuery("SELECT * FROM customer WHERE customerId='" + customerId + "';");
        sub.next();
        this.customerName = sub.getString("customerName");
    }
    
    public SimpleStringProperty getTypeProperty () {
        return new SimpleStringProperty(this.type);
    }
    
    public SimpleStringProperty getCustProperty () {
        return new SimpleStringProperty(this.customerName);
    }
}
