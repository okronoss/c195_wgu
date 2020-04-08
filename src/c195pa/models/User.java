/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195pa.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author alex
 */
public class User {
    private final int id;
    private final String username;
    private final String password;
    private final Boolean active;
    private final Date createDate;
    private final String createdBy;
    private final Date lastUpdate;
    private final String lastUpdateBy;
    
    /**
     *
     * @param userId
     * @throws SQLException
     */
    public User (int userId) throws SQLException {
        ResultSet rs = Database.connect().createStatement().executeQuery("SELECT * FROM user WHERE userId='" + userId + "';");
        
        rs.next();
        this.id = rs.getInt("userId");
        this.username = rs.getString("userName");
        this.password = rs.getString("password");
        this.active = rs.getBoolean("active");
        this.createDate = rs.getDate("createDate");
        this.createdBy = rs.getString("createdBy");
        this.lastUpdate = rs.getDate("lastUpdate");
        this.lastUpdateBy = rs.getString("lastUpdateBy");
    }
    
//    public User (String un, String pw, Boolean status) throws SQLException {
//        username = un;
//        password = pw;
//        active = status;
//        
//        Database.connect().createStatement().executeUpdate("INSERT INTO user");
//    }
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
    public boolean getActive () {
        return this.active;
    }

    /**
     *
     * @return
     */
    public Date getCreateDate () {
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
    public Date getLastUpdate () {
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
        boolean isValid = false;
        ResultSet rs = Database.connect().createStatement().executeQuery("SELECT * FROM  user WHERE userName='" + username +"' AND password='" + password + "';");

        if (rs.next()) {
            isValid = true;
        }
        
        return isValid;
    }
}
