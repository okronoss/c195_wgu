/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195pa.models;
import java.sql.*;
import java.sql.DriverManager;

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
    
    private static void connect() throws SQLException {
        conn = DriverManager.getConnection(URL, DB_USER, DB_PASS);
        stmnt = conn.createStatement();
        stmnt.executeQuery("USE U061zg");
        System.out.println(conn);
    }
    
    public static boolean authUser(String username, String password) throws SQLException {
        boolean isValid = false;
        
        Database.connect();
        ResultSet rs = stmnt.executeQuery("SELECT * FROM  user WHERE userName='" + username +"' AND password='" + password + "';");

        if (rs.next()) {
            isValid = true;
        }
        
        return isValid;
    }
    
}
