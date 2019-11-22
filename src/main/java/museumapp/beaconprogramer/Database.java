/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumapp.beaconprogramer;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;

/**
 *
 * @author mdu18
 */
public class Database {
    
    public Database(){
    }
    
    public Connection connect(){
        // Connect to database
        String hostName = "alabamamuseum.database.windows.net"; // update me
        String dbName = "MuseumApp"; // update me
        String user = "museumadmin"; // update me
        String password = "Alabama123"; // update me
        String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;"
            + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url);
            String schema = connection.getSchema();
            System.out.println("Successful connection - Schema: " + schema);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return connection; 
    }
}
