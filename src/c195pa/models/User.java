/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195pa.models;

import static c195pa.models.Database.authUser;
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
    public static User getUser(String un, String pass) throws SQLException {
        User user = null;
        
        if (authUser(un, pass)) {
            user = new User();
        }
        
        return user;        
    }
}
