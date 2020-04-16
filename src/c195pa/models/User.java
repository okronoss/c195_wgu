/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195pa.models;

import static c195pa.AMS.USER;
import static c195pa.AMS.connect;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 *
 * @author alex
 */
public class User {
    private final int id;
    private final String username;
    private final String password;
    private final Boolean active;
    private final LocalDateTime createDate;
    private final String createdBy;
    private final LocalDateTime lastUpdate;
    private final String lastUpdateBy;
    
    /**
     *
     * @param userId
     * @throws SQLException
     */
    public User (int userId) throws SQLException {
        ResultSet rs = connect().createStatement().executeQuery("SELECT * FROM user WHERE userId='" + userId + "';");
        
        rs.next();
        this.id = rs.getInt("userId");
        this.username = rs.getString("userName");
        this.password = rs.getString("password");
        this.active = rs.getBoolean("active");
        this.createDate = Instant.ofEpochMilli(rs.getDate("createDate").getTime()).atZone(ZoneId.of("UTC")).toLocalDateTime();
        this.createdBy = rs.getString("createdBy");
        this.lastUpdate = Instant.ofEpochMilli(rs.getDate("lastUpdate").getTime()).atZone(ZoneId.of("UTC")).toLocalDateTime();
        this.lastUpdateBy = rs.getString("lastUpdateBy");
    }
    
    /**
     *
     * @return
     */
    public String getUsername () {
        return this.username;
    }

    /**
     *
     * @return
     */
    public int getId () {
        return this.id;
    }

    /**
     *
     * @return
     */
    public boolean getActive () {
        return this.active;
    }

    /**
     *
     * @return
     */
    public LocalDateTime getCreateDate () {
        return this.createDate;
    }

    /**
     *
     * @return
     */
    public String getCreatedBy () {
        return this.createdBy;
    }

    /**
     *
     * @return
     */
    public LocalDateTime getLastUpdate () {
        return this.lastUpdate;
    }

    /**
     *
     * @return
     */
    public String getLastUpdateBy () {
        return this.lastUpdateBy;
    }
    
    /**
     *
     * @param username
     * @param password
     * @return
     * @throws SQLException
     */
    public static synchronized boolean authUser(String username, String password) throws SQLException {
        boolean valid = false;
        ResultSet rs = connect().createStatement().executeQuery("SELECT * FROM  user WHERE userName='" + username +"' AND password='" + password + "';");

        if (rs.next()) {
            valid = true;
            USER = new User(rs.getInt("userId"));
        }
        
        return valid;
    }
}
