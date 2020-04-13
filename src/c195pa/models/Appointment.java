/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195pa.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javafx.beans.property.SimpleObjectProperty;
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
    private final ZonedDateTime start;
    private final ZonedDateTime end;
    private final ZonedDateTime createDate;
    private final String createdBy;
    private final ZonedDateTime lastUpdate;
    private final String lastUpdateBy;
    
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
        this.start = ZonedDateTime.ofInstant(rs.getTimestamp("start").toLocalDateTime().atZone(ZoneId.of("UTC")).toInstant(), ZoneId.systemDefault());
        this.end = ZonedDateTime.ofInstant(rs.getTimestamp("start").toLocalDateTime().atZone(ZoneId.of("UTC")).toInstant(), ZoneId.systemDefault());
        this.createDate = ZonedDateTime.ofInstant(rs.getTimestamp("start").toLocalDateTime().atZone(ZoneId.of("UTC")).toInstant(), ZoneId.systemDefault());
        this.createdBy = rs.getString("createdBy");
        this.lastUpdate = ZonedDateTime.ofInstant(rs.getTimestamp("start").toLocalDateTime().atZone(ZoneId.of("UTC")).toInstant(), ZoneId.systemDefault());
        this.lastUpdateBy = rs.getString("lastUpdateBy");
    }
    
    public Appointment (int id, int customerId, int userId, String title, String description, String location, String contact, String type, String url, ZonedDateTime start, ZonedDateTime end, ZonedDateTime createDate, String createdBy, ZonedDateTime lastUpdate, String lastUpdateBy) throws SQLException {
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
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getType() {
        return this.type;
    }
    
    public ZonedDateTime getStart() {
        return this.start;
    }
    
    public ZonedDateTime getEnd() {
        return this.end;
    }
    
    public SimpleStringProperty getTitleProperty () {
        return new SimpleStringProperty(this.title);
    }
    
    public SimpleStringProperty getTypeProperty () {
        return new SimpleStringProperty(this.type);
    }
    
    public SimpleObjectProperty getStartProperty () {
        return new SimpleObjectProperty(this.start);
    }
}
