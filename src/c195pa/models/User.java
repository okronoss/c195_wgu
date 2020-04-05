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
    private static final String DB_NAME = "user";
    private int userId;
    private String username;
    private String password;
    private Boolean active;
    private Date createdDate;
    private String createdBy;
    private Date lastUpdate;
    private String lastUpdateBy;
    
    // get
    public static User getUser(String username, String password) throws SQLException {
        User user = null;
        
        if (isUser(username)) {
            
        }
        
        return user;        
    }
    
    public int getUserId() {
        return this.userId;
    }
    
    // set
    
    // misc
    public static boolean isUser(String username) throws SQLException {
        return Database.isValidByColumn(DB_NAME, "userName", username);
    }
}
